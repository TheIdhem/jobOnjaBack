package Solutions.Data.Proxy.Trackers;

import Solutions.Data.Annotations.JoinColumn;
import Solutions.Data.Annotations.OneToMany;
import Solutions.Data.CascadeType;
import Solutions.Data.EntityManager;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;

import static Solutions.Data.Proxy.Manipulation.ModelProxyFieldAccessors.getTable;
import static Solutions.Utils.ReflectionUtil.getFieldValue;
import static Solutions.Utils.ReflectionUtil.getIdField;
import static java.util.Arrays.asList;

public class OneToManyPersistenceSet extends PersistenceSet {


    OneToManyPersistenceSet(Collection c, Field field, Object owner) throws SQLException {
        super(c, field, owner);
    }

    @Override
    public boolean remove(Object o) {

        if(!super.remove(o)) {
            return false;
        }
        OneToMany oneToMany = field.getAnnotation(OneToMany.class);
        if (field.isAnnotationPresent(JoinColumn.class)){
            if(asList(oneToMany.cascade()).contains(CascadeType.DELETE)){
                try {
                    EntityManager.getInstance().queryForObject(connection -> {
                        String sql = "DELETE FROM %s WHERE %s = ?";
                        sql = String.format(sql, getTable(o), getIdField(o.getClass()).getName());
                        PreparedStatement statement = connection.prepareStatement(sql);
                        statement.setObject(1, getFieldValue(getIdField(o.getClass()), o));
                        statement.executeUpdate();
                        return null;
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }
}
