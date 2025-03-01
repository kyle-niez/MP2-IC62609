<%@ page import="java.sql.*" %>
<%@ page import="java.util.*" %>
<%
    String loggedInUser = (String) session.getAttribute("username");
    if (loggedInUser == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    List<String> followedUsers = new ArrayList<String>(); // Fixed: Removed diamond operator

    try {
        Class.forName("com.mysql.cj.jdbc.Driver");
        conn = servlet.DatabaseConnection.getConnection();
        String sql = "SELECT follow1, follow2, follow3 FROM follows WHERE user_name = ?";
        ps = conn.prepareStatement(sql);
        ps.setString(1, loggedInUser);
        rs = ps.executeQuery();

        if (rs.next()) {
            for (int i = 1; i <= 3; i++) {
                String followedUser = rs.getString(i);
                if (followedUser != null) followedUsers.add(followedUser);
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        if (rs != null) rs.close();
        if (ps != null) ps.close();
        if (conn != null) conn.close();
    }
%>
<html>
<head>
    <title>User Follow System</title>
</head>
<body style="font-family: Arial, sans-serif; background-color: #f0f2f5; text-align: center; margin: 0; padding: 0;">

    <div style="background: #1877f2; color: white; padding: 15px;">
        <a href="LandingServlet" style="color: white; text-decoration: none; margin: 0 15px;">Home</a>
        <a href="profile" style="color: white; text-decoration: none; margin: 0 15px;">Profile</a>
        <a href="users.jsp" style="color: white; text-decoration: none; margin: 0 15px;">Followed Users</a>
        <a href="help.jsp" style="color: white; text-decoration: none; margin: 0 15px;">Help</a>
        <a href="login.jsp" style="color: white; text-decoration: none; margin: 0 15px;">Logout</a>
    </div>

    <div style="background: white; width: 90%; max-width: 600px; margin: 20px auto; padding: 20px; border-radius: 8px; box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.1);">
        <h2>Welcome, <%= loggedInUser %>!</h2>
        
        <h3>Find Users to Follow</h3>
        <form action="follow" method="post">
            <input type="text" name="followUser" placeholder="Enter username" required 
                   style="width: 80%; padding: 8px; margin: 10px 0; border: 1px solid #ccc; border-radius: 4px;">
            <input type="submit" value="Follow" 
                   style="background: #1877f2; color: white; border: none; padding: 8px 12px; border-radius: 4px; cursor: pointer;">
        </form>

        <h3>Followed Users</h3>
        <ul style="list-style: none; padding: 0;">
            <% for (String user : followedUsers) { %>
                <li style="background: #f8f9fa; padding: 10px; margin: 5px 0; border-radius: 4px;">
                    <%= user %> 
                    <form action="unfollow" method="post" style="display:inline;">
                        <input type="hidden" name="unfollowUser" value="<%= user %>">
                        <input type="submit" value="Unfollow" 
                               style="background: red; color: white; border: none; padding: 5px 10px; border-radius: 4px; cursor: pointer;">
                    </form>
                </li>
            <% } %>
        </ul>
    </div>

</body>
</html>
