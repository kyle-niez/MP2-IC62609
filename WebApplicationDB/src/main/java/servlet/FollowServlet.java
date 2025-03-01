package servlet;

import java.io.IOException;
import java.sql.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/FollowServlet")
public class FollowServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String loggedInUser = (String) session.getAttribute("username");
        String followUser = request.getParameter("followUser");

        if (loggedInUser == null || followUser == null || followUser.equals(loggedInUser)) {
            response.sendRedirect("users.jsp");
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement rolePs = conn.prepareStatement("SELECT user_role FROM account WHERE user_name = ?");
            rolePs.setString(1, followUser);
            ResultSet roleRs = rolePs.executeQuery();

            if (roleRs.next()) {
                String userRole = roleRs.getString("user_role");
                if ("admin".equalsIgnoreCase(userRole) || "super_admin".equalsIgnoreCase(userRole)) {
                    response.sendRedirect("users.jsp?error=Cannot follow an admin or superadmin");
                    return;
                }
            } else {
                response.sendRedirect("users.jsp?error=User not found");
                return;
            }

            PreparedStatement ps = conn.prepareStatement("SELECT follow1, follow2, follow3 FROM follows WHERE user_name = ?");
            ps.setString(1, loggedInUser);
            ResultSet rs = ps.executeQuery();

            String[] follows = new String[3];
            boolean alreadyFollowing = false;

            if (rs.next()) {
                for (int i = 0; i < 3; i++) {
                    follows[i] = rs.getString(i + 1);
                    if (followUser.equals(follows[i])) {
                        alreadyFollowing = true;
                    }
                }
            }

            if (alreadyFollowing) {
                response.sendRedirect("users.jsp?error=Already following this user");
                return;
            }

            if (follows[0] != null && follows[1] != null && follows[2] != null) {
                response.sendRedirect("users.jsp?error=Limit reached");
                return;
            }

            for (int i = 0; i < 3; i++) {
                if (follows[i] == null) {
                    String column = "follow" + (i + 1);
                    PreparedStatement updatePs = conn.prepareStatement("UPDATE follows SET " + column + " = ? WHERE user_name = ?");
                    updatePs.setString(1, followUser);
                    updatePs.setString(2, loggedInUser);
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
