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
    List followedUsers = new ArrayList();

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
<body>
    <h2>Welcome, <%= loggedInUser %></h2>
    
    <h3>Find Users to Follow</h3>
    <form action="follow" method="post">
        <input type="text" name="followUser" placeholder="Enter username" required>
        <input type="submit" value="Follow">
    </form>

    <h3>Followed Users</h3>
    <ul>
        <% for (Iterator iterator = followedUsers.iterator(); iterator.hasNext();) { 
            String user = (String) iterator.next(); %>
            <li>
                <%= user %> 
                <form action="unfollow" method="post" style="display:inline;">
                    <input type="hidden" name="unfollowUser" value="<%= user %>">
                    <input type="submit" value="Unfollow">
                </form>
            </li>
        <% } %>
    </ul>
</body>
</html>
