package servlet;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String url = "jdbc:mysql://localhost:3306/WebApplicationDB?useSSL=false&allowPublicKeyRetrieval=true";
    private static final String user = "root"; 
    private static final String password = "1234"; 

    public static Connection getConnection() throws SQLException {
        Connection conn = null; 
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); 
            conn = DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new SQLException("JDBC Driver not found", e);
        }
        return conn; 
    }
}
