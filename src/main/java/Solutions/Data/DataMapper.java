package Solutions.Data;

import java.sql.Connection;

public interface DataMapper<T> {
    T map(Connection connection) throws Exception;
}
