package Solutions.Data;

import java.sql.Connection;
import java.sql.SQLException;

public interface ApplicationDataBaseConnectionProvider {

    public Connection getConnection() throws SQLException;
}
