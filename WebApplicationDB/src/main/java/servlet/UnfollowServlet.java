package servlet;

import java.io.IOException;
import java.sql.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/UnfollowServlet")
public class UnfollowServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String loggedInUser = (String) session.getAttribute("username");
        String unfollowUser = request.getParameter("unfollowUser");

        if (loggedInUser == null || unfollowUser == null) {
            response.sendRedirect("users.jsp");
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("SELECT follow1, follow2, follow3 FROM follows WHERE user_name = ?");
            ps.setString(1, loggedInUser);
            ResultSet rs = ps.executeQuery();

            String[] follows = new String[3];
            if (rs.next()) {
                follows[0] = rs.getString(1);
                follows[1] = rs.getString(2);
                follows[2] = rs.getString(3);
            }

            for (int i = 0; i < 3; i++) {
                if (follows[i] != null && follows[i].equals(unfollowUser)) {
                    String column = "follow" + (i + 1);
                    PreparedStatement updatePs = conn.prepareStatement("UPDATE follows SET " + column + " = NULL WHERE user_name = ?");
                    updatePs.setString(1, loggedInUser);
                    updatePs.executeUpdate();
                    break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        response.sendRedirect("users.jsp");
    }
}
