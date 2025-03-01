package servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import servlet.DatabaseConnection;

@WebServlet("/PostServlet")
public class PostServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");
        String content = request.getParameter("content");

        if (username == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            String sqlFetch = "SELECT post1, post2, post3, post4, post5 FROM posts WHERE user_name = ?";
            PreparedStatement psFetch = conn.prepareStatement(sqlFetch);
            psFetch.setString(1, username);
            ResultSet rs = psFetch.executeQuery();

            String post1 = content, post2 = "", post3 = "", post4 = "", post5 = "";
            if (rs.next()) {
                post2 = rs.getString("post1");
                post3 = rs.getString("post2");
                post4 = rs.getString("post3");
                post5 = rs.getString("post4");
            }
            String sqlUpdate = "REPLACE INTO posts (user_name, post1, post2, post3, post4, post5) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement psUpdate = conn.prepareStatement(sqlUpdate);
            psUpdate.setString(1, username);
            psUpdate.setString(2, post1);
            psUpdate.setString(3, post2);
            psUpdate.setString(4, post3);
            psUpdate.setString(5, post4);
            psUpdate.setString(6, post5);
            psUpdate.executeUpdate();

            response.sendRedirect("profile.jsp?success=Post added");
        } catch (Exception e) {
            throw new ServletException("Database error", e);
        }
    }
}
