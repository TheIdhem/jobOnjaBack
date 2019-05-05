package Solutions.Data.Proxy.Trackers;

import Solutions.Data.Annotations.CollectionTable;
import Solutions.Data.EntityManager;

import java.beans.IntrospectionException;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;

import static Solutions.Utils.ReflectionUtil.getFieldValue;
import static Solutions.Utils.ReflectionUtil.getIdField;

public class ValueTypePersistenceSet extends PersistenceSet {



    ValueTypePersistenceSet(Collection c, Field field, Object owner) throws Exception {
        super(c, field, owner);
    }

    @Override
    public boolean remove(Object o) {
        if(!super.remove(o)) {
            return false;
        }
        CollectionTable collectionTable = field.getAnnotation(CollectionTable.class);

        try {
            EntityManager.getInstance().queryForObject(connection -> {
                String sql = "DELETE FROM %s WHERE %s = ?";
                sql = String.format(sql, collectionTable.name(), collectionTable.joinColumn());
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setObject(1, getFieldValue(getIdField(owner.getClass()), owner));
                statement.executeUpdate();
                return null;
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }
}
