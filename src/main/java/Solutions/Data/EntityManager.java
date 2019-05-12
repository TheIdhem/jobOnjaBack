package Solutions.Data;

import Solutions.Core.ApplicationProperties;
import Solutions.Data.Annotations.*;
import Solutions.Data.Proxy.Manipulation.ModelProxyFieldAccessors;
import Solutions.Data.Proxy.ModelProxy;
import org.reflections.Reflections;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.*;

import static Solutions.Data.Proxy.Manipulation.ModelProxyFieldAccessors.getColumns;
import static Solutions.Utils.ReflectionUtil.getFieldValue;
import static Solutions.Utils.ReflectionUtil.getIdField;
import static Solutions.Utils.ReflectionUtil.setField;
import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import static java.util.Collections.singleton;
import static org.apache.commons.lang3.reflect.FieldUtils.getAllFields;

public class EntityManager {

    private static EntityManager instance;
    private ApplicationDataBaseConnectionProvider dbConnectionProvider;
    private final Cache cache;

    private EntityManager() throws ReflectiveOperationException {
        String baseConfig = ApplicationProperties.getInstance().getProperty("solutions.application_config.base_package");
        Reflections connectionConfig = new Reflections(baseConfig);
        Optional<Class<? extends ApplicationDataBaseConnectionProvider>> dbConnection = connectionConfig.getSubTypesOf(ApplicationDataBaseConnectionProvider.class).stream().findFirst();
        Class<? extends ApplicationDataBaseConnectionProvider> clazz = dbConnection.orElseThrow(() -> new RuntimeException("No DBConnectionProvider."));
        dbConnectionProvider = clazz.newInstance();
        cache = Cache.getInstance();
    }

    public static void init() throws ReflectiveOperationException {
        if (instance == null)
            instance = new EntityManager();
    }

    public <T> T queryForObject(DataMapper<T> mapper) throws Exception{
        try(Connection connection = dbConnectionProvider.getConnection()) {
            return mapper.map(connection);
        }
    }

    public static EntityManager getInstance() {
        return instance;
    }

    @SuppressWarnings("unchecked")
    public <T extends Entity> Optional<T> find(Class<T> clazz, Serializable id) throws Exception {

        if(cache.getObject(clazz, id) != null)
            return Optional.of(cache.getObject(clazz, id));

        Field idField = stream(clazz.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Id.class))
                .findFirst().orElseThrow(() -> new RuntimeException("Entity does not have id"));

        return this.queryForObject(connection -> {
            String sql = "SELECT * FROM %s WHERE %s = ?";
            sql = String.format(sql, clazz.getSimpleName(), idField.getName());

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setObject(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                resultSet.close();
                return Optional.empty();
            }
            T o = new BeanMapper().getForObject(resultSet, clazz.getSimpleName(), clazz);
            resultSet.close();
            return Optional.of(cache.assertObject(clazz, id, o));
        });

    }

    public <T  extends Entity> T save(T o) throws Exception {
        String tableName = o instanceof ModelProxy ? ModelProxyFieldAccessors.getTable(o) : o.getClass().getSimpleName();
        List<Object> values =  new ArrayList<>();
        String sql = persist(o, tableName, new ArrayList<>(), values);

        return this.queryForObject(connection ->  {
            PreparedStatement statement;
            if (getIdField(o.getClass()).isAnnotationPresent(AutoIncrement.class))
                statement = connection.prepareStatement(sql);
            else
                statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            for (int i = 1; i <= values.size(); i++)
                statement.setObject(i, values.get(i - 1));

            int rowsAffected = statement.executeUpdate();
            assert rowsAffected == 1;

            if (getIdField(o.getClass()).isAnnotationPresent(AutoIncrement.class))
                try(ResultSet r = statement.getGeneratedKeys()) {
                    r.next();
                    setField(getIdField(o.getClass()), o, r.getInt(1));
                }

            for (Field f : getAllFields(o.getClass())) {
                if (f.getName().startsWith("__"))
                    continue;
                if (f.isAnnotationPresent(OneToMany.class)) {
                    OneToMany oneToMany = f.getAnnotation(OneToMany.class);
                    if (asList(oneToMany.cascade()).contains(CascadeType.PERSIST)) {
                        if (f.isAnnotationPresent(JoinColumn.class)) {
                            Object entityList = getFieldValue(f, o);
                            if (entityList != null)
                                for(Object e : (Collection) entityList)
                                    save((Entity) e);
                        }
                    }
                } else if (f.isAnnotationPresent(CollectionTable.class)) {
                    CollectionTable collectionTable = f.getAnnotation(CollectionTable.class);
                    if (getFieldValue(f, o) == null)
                        continue;
                    for (Object item : (Collection) getFieldValue(f, o)) {
                        List<Object> v = new ArrayList<>(singleton(getFieldValue(getIdField(o.getClass()), o)));
                        String s = persist(item,
                                collectionTable.name(),
                                new ArrayList<>(singleton(collectionTable.joinColumn())),
                                v
                        );
                        PreparedStatement ps = connection.prepareStatement(s);
                        for (int i = 1; i <= v.size(); i++)
                            ps.setObject(i, v.get(i - 1));
                        ps.executeUpdate();
                    }
                }
            }
            return o;
        });
    }

    public <T extends Entity> Set<T> getAll(Class<T> clazz) throws Exception {

        return this.queryForObject(connection ->  {
            String sql = "SELECT * FROM %s";
            Statement statement = connection.createStatement();
            sql = String.format(sql, clazz.getSimpleName());
            ResultSet resultSet = statement.executeQuery(sql);
            BeanMapper beanMapper = new BeanMapper();

            Set<T> r = new HashSet<>();
            while (resultSet.next()) {
                T o = beanMapper.getForObject(resultSet, clazz.getSimpleName(), clazz);
                o = cache.assertObject(clazz, (Serializable) getFieldValue(getIdField(o.getClass()), o), o);
                r.add(o);
            }
            resultSet.close();
            return r;
        });
    }

    public <T  extends Entity> Set<T> saveAll(Set<T> all) throws Exception {
        Set<T> r = new HashSet<>();
        for (T item : all){
            r.add(save(item));
        }
        return r;
    }


    private String persist(Object o, String table, List<String> fields, List<Object> values) throws Exception {
        StringBuilder sb = new StringBuilder("INSERT OR REPLACE INTO ").append(table).append(" (");
        for (Field f : getAllFields(o.getClass())){
            if (f.getName().startsWith("__"))
                continue;
            if (Serializable.class.isAssignableFrom(f.getType())){
                fields.add(f.getName());
                values.add(getFieldValue(f, o));
            }
            else if (f.isAnnotationPresent(ManyToOne.class)){
                fields.add(f.getName());
                Object m = getFieldValue(f, o);
                Map cols = getColumns(o);
                if (cols != null)
                    values.add(cols.get(f.getName()));
                else if (m != null)
                    values.add(getFieldValue(getIdField(m.getClass()), m));
                else
                    values.add(null);
            }
        }
        assert fields.size() == values.size();
        for (int i = 0; i < fields.size(); i++){
            sb.append(fields.get(i));
            if (i < fields.size() - 1)
                sb.append(", ");
            else
                sb.append(")");
        }
        sb.append(" VALUES (");
        for (int i = 0; i < values.size(); i++){
            if (i < values.size() - 1)
                sb.append("?, ");
            else
                sb.append("?)");
        }
        return sb.toString();
    }
}
