package ir.joboona.configurations;

import Solutions.Data.ApplicationDataBaseConnectionProvider;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class HikariCPDataSource implements ApplicationDataBaseConnectionProvider {
     
    private HikariConfig config = new HikariConfig();
    private HikariDataSource ds;
     
    public HikariCPDataSource() {
        HikariConfig config = new HikariConfig();
        config.setPoolName("SQLitePool");
        config.setDriverClassName("org.sqlite.JDBC");
        config.setJdbcUrl("jdbc:sqlite:/home/mryf/IdeaProjects/jobonja/identifier.sqlite");
        config.setConnectionTestQuery("SELECT * FROM User");
        config.setMaxLifetime(60000); // 60 Sec
        config.setIdleTimeout(45000); // 45 Sec
        config.setMaximumPoolSize(50); // 50 Connections (including idle connections)
        ds = new HikariDataSource(config);
    }

     
    public Connection getConnection() throws SQLException {
        return ds.getConnection();
    }
}