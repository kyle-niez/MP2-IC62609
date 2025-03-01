package servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import servlet.DatabaseConnection;

@WebServlet("/CreateAdminServlet")
public class CreateAdminServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String user_role = request.getParameter("user_role");
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "INSERT INTO account (user_name, password, user_role) VALUES (?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, user_role);
            ps.executeUpdate();

            response.sendRedirect("admin.jsp");
        } catch (Exception e) {
            throw new ServletException("Database error", e);
        }
    }
}
