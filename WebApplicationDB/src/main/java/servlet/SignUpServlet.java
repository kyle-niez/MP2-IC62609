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

@WebServlet("/SignUpServlet")
public class SignUpServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        Connection conn = null;
        PreparedStatement psAccount = null;
        PreparedStatement psFollows = null;

        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); 

            String sqlAccount = "INSERT INTO account (user_name, password, user_role) VALUES (?, ?, 'user')";
            psAccount = conn.prepareStatement(sqlAccount);
            psAccount.setString(1, username);
            psAccount.setString(2, password);
            psAccount.executeUpdate();

            String sqlFollows = "INSERT INTO follows (user_name, follow1, follow2, follow3) VALUES (?, ?, ?, ?)";
            psFollows = conn.prepareStatement(sqlFollows);
            psFollows.setString(1, username);
            psFollows.setNull(2, java.sql.Types.VARCHAR);
            psFollows.setNull(3, java.sql.Types.VARCHAR);
            psFollows.setNull(4, java.sql.Types.VARCHAR);
            psFollows.executeUpdate();

            conn.commit();
            response.sendRedirect("login.jsp");
        } catch (Exception e) {
            if (conn != null) {
                try {
                    conn.rollback(); 
                } catch (Exception rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            throw new ServletException("Database error", e);
        } finally {
            try {
                if (psAccount != null) psAccount.close();
                if (psFollows != null) psFollows.close();
                if (conn != null) conn.setAutoCommit(true); 
            } catch (Exception closeEx) {
                closeEx.printStackTrace();
            }
        }
    }
}
