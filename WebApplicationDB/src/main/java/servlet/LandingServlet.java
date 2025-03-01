import java.io.IOException;
import java.sql.*;
import java.util.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/LandingServlet")
public class LandingServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String loggedInUser = (String) session.getAttribute("username");
        List<String> followedUsers = new ArrayList<>();
        Map<String, List<String>> userPosts = new HashMap<>();

        try (Connection conn = servlet.DatabaseConnection.getConnection()) {
            // Retrieve followed users
            String sql = "SELECT follow1, follow2, follow3 FROM follows WHERE user_name = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, loggedInUser);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        for (int i = 1; i <= 3; i++) {
                            String followedUser = rs.getString(i);
                            if (followedUser != null && !followedUser.isEmpty()) {
                                followedUsers.add(followedUser);
                            }
                        }
                    }
                }
            }

            // Retrieve posts from followed users
            if (!followedUsers.isEmpty()) {
                sql = "SELECT user_name, post1, post2, post3, post4, post5 FROM posts WHERE user_name = ? OR user_name = ? OR user_name = ?";
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    for (int i = 0; i < 3; i++) {
                        if (i < followedUsers.size()) {
                            ps.setString(i + 1, followedUsers.get(i));
                        } else {
                            ps.setNull(i + 1, java.sql.Types.VARCHAR);
                        }
                    }

                    try (ResultSet rs = ps.executeQuery()) {
                        while (rs.next()) {
                            String user = rs.getString("user_name");
                            List<String> posts = new ArrayList<>();
                            for (int i = 1; i <= 5; i++) {
                                String post = rs.getString("post" + i);
                                if (post != null && !post.isEmpty()) {
                                    posts.add(post);
                                }
                            }
                            userPosts.put(user, posts);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        request.setAttribute("userPosts", userPosts);
        request.setAttribute("loggedInUser", loggedInUser);
        request.getRequestDispatcher("landing.jsp").forward(request, response);
    }
}
