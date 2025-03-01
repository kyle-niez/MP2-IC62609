package controllers;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class TestConnection {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3310/WebApplicationDB";
        String user = "root"; 
        String password = "1234";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, user, password);
            System.out.println("Database connection successful!");
            conn.close();
        } catch (ClassNotFoundException e) {
            System.out.println("JDBC Driver not found! " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Database connection failed! " + e.getMessage());
        }
    
    }
            
}
