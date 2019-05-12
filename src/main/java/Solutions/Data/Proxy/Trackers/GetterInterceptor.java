package Solutions.Data.Proxy.Trackers;


import Solutions.Data.Annotations.CollectionTable;
import Solutions.Data.Annotations.JoinColumn;
import Solutions.Data.Annotations.ManyToOne;
import Solutions.Data.Annotations.OneToMany;
import Solutions.Data.BeanMapper;
import Solutions.Data.Cache;
import Solutions.Data.Entity;
import Solutions.Data.EntityManager;
import Solutions.Data.Proxy.Manipulation.ModelProxyFieldAccessors;
import net.bytebuddy.implementation.bind.annotation.*;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashSet;
import java.util.Set;

import static Solutions.Utils.ReflectionUtil.getFieldValue;
import static Solutions.Utils.ReflectionUtil.getIdField;
import static Solutions.Utils.ReflectionUtil.setField;
import static Solutions.Utils.ObjectConverter.stringToSerializable;
import static java.util.Arrays.stream;
import static org.apache.commons.lang3.reflect.FieldUtils.getAllFields;


public class GetterInterceptor {

    private static EntityManager entityManager = EntityManager.getInstance();
    private static Cache cache = Cache.getInstance();

    @RuntimeType
    @BindingPriority(1)
    public static Object get(@This Object proxy, @Origin Method getter) throws Exception{

        if (Set.class.isAssignableFrom(getter.getReturnType()))
            return getSet(proxy, getter);
        else
            return getForObject(proxy, getter);

    }

    @SuppressWarnings("unchecked")
    @IgnoreForBinding
    private static Set getSet(@This Object proxy, @Origin Method getter) throws Exception {
        PropertyDescriptor pd = getFieldDescriptor(getter, proxy.getClass().getSuperclass());
        Field field = stream(getAllFields(proxy.getClass()))
                .filter(f -> f.getName().equals(pd.getName())).findFirst().orElseThrow(() ->  new RuntimeException("Fake proxy"));

        ParameterizedType stringListType = (ParameterizedType) field.getGenericType();
        Class<?> contentType = (Class<?>) stringListType.getActualTypeArguments()[0];
        return entityManager.queryForObject(connection ->  {

            BeanMapper beanMapper = new BeanMapper();
            Set set = new HashSet();
            if (getFieldValue(field, proxy) == null) {
                PersistenceSet persistenceSet = null;
                if (field.isAnnotationPresent(CollectionTable.class)) {
                    CollectionTable a = field.getAnnotation(CollectionTable.class);
                    String sql = "SELECT * FROM %s WHERE %s = ?";
                    sql = String.format(sql, a.name(), a.joinColumn());
                    PreparedStatement statement = connection.prepareStatement(sql);
                    Object id = getFieldValue(getIdField(proxy.getClass()), proxy);
                    statement.setObject(1, id);

                    ResultSet rs = statement.executeQuery();
                    while (rs.next())
                        set.add(beanMapper.getForObject(rs, a.name(), contentType));
                    rs.close();
                    persistenceSet = new ValueTypePersistenceSet(set, field, proxy);
                } else if (field.isAnnotationPresent(OneToMany.class)) {

                    if (field.isAnnotationPresent(JoinColumn.class)) {
                        JoinColumn joinColumn = field.getAnnotation(JoinColumn.class);
                        String sql = "SELECT * FROM %s WHERE %s = ?";
                        sql = String.format(sql, contentType.getSimpleName(), joinColumn.name());
                        PreparedStatement statement = connection.prepareStatement(sql);
                        Field joinField = stream(getAllFields(proxy.getClass())).filter(f -> f.getName().equals(joinColumn.referencedColumnName())).findFirst().orElse(null);
                        Object joinOn = getFieldValue(joinField, proxy);
                        statement.setObject(1, joinOn);

                        ResultSet rs = statement.executeQuery();

                        while (rs.next()) {
                            Entity item = (Entity) beanMapper.getForObject(rs, contentType.getSimpleName(), contentType);
                            set.add(cache.assertObject(
                                    (Class<Entity>)contentType,
                                    (Serializable) getFieldValue(getIdField(contentType), item),
                                    item
                            ));

                        }
                        rs.close();
                        persistenceSet = new OneToManyPersistenceSet(set, field, proxy);
                    }
                }
                setField(field, proxy, persistenceSet);

            }
            return (Set) getFieldValue(field, proxy);
        });
    }

    @IgnoreForBinding
    @SuppressWarnings("unchecked")
    private static Object getForObject(@This Object proxy, @Origin Method getter) throws Exception {
        PropertyDescriptor pd = getFieldDescriptor(getter, proxy.getClass().getSuperclass());
        Field field = stream(getAllFields(proxy.getClass()))
                .filter(f -> f.getName().equals(pd.getName())).findFirst().orElseThrow(() ->  new RuntimeException("Fake proxy"));
        if (getFieldValue(field, proxy) == null){
            if(field.isAnnotationPresent(ManyToOne.class)) {
                String columnValue = ModelProxyFieldAccessors.getColumns(proxy).get(pd.getName());
                if (columnValue != null) {
                    Serializable idValue = stringToSerializable(columnValue, (Class<Serializable>) getIdField(field.getType()).getType());
                    Object o = entityManager.find((Class<? extends Entity>) field.getType(), idValue).orElse(null);
                    setField(field, proxy, o);
                }
            }
        }
        return getFieldValue(field, proxy);
    }

    @IgnoreForBinding
    private static PropertyDescriptor getFieldDescriptor(Method getter, Class<?> clazz) throws IntrospectionException {
        return stream(Introspector.getBeanInfo(clazz).getPropertyDescriptors())
                .filter(pd -> pd.getReadMethod() != null && pd.getReadMethod().equals(getter)).findFirst().orElse(null);
    }


}
