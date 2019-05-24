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
        config.setPoolName("IE");
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setJdbcUrl("jdbc:mysql://localhost:3306/IE");
        config.setUsername("root");
        config.setPassword("Abcd12345");
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