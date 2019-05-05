package Solutions.Data.Proxy.Trackers;

import Solutions.Data.EntityManager;
import Solutions.Data.Proxy.PersistenceProxy;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;


public class PersistenceSet extends HashSet implements PersistenceProxy {

    protected Field field;
    protected Object owner;

    public PersistenceSet() {
    }

    public PersistenceSet(Collection c, Field field, Object owner) throws SQLException {
        super(c);
        this.field = field;
        this.owner = owner;
    }
}
