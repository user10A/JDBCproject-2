package Congif;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Config {
    public static Connection getConnection(){
        Connection connection;
        try {
            connection= DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/postgres1.2",
                    "postgres",
                    "erkin");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return connection;
    }
}
