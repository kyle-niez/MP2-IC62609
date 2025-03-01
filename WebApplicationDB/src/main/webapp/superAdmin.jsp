<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.sql.*" %>
<%@ page import="servlet.DatabaseConnection" %>

<%
    try {
        Connection conn = DatabaseConnection.getConnection();
        
        String sql = "SELECT user_name, message, timestamp FROM messages ORDER BY timestamp DESC";
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
%>

<!DOCTYPE html>
<html>
<head>
    <title>Admin Dashboard</title>
</head>
<body>
    <h2>Admin Panel</h2>
    <h3>User Management</h3>
    <a href="create.jsp">Create User</a> | 
    <a href="updateAdmin.jsp">Update User</a> | 
    <a href="deleteAdmin.jsp">Delete User</a>
    
    <h3>Messages to Admin</h3>
<table border="1">
    <tr>
        <th>User</th>
        <th>Message</th>
        <th>Timestamp</th>
    </tr>
    <%
        while (rs.next()) {
    %>
    <tr>
        <td><%= rs.getString("user_name") %></td>
        <td><%= rs.getString("message") %></td>
        <td><%= rs.getTimestamp("timestamp") %></td>
    </tr>
    <%
        }
        rs.close();
        stmt.close();
        conn.close();
    } catch (Exception e) {
        out.println("<p style='color:red;'>Error: " + e.getMessage() + "</p>");
    }
%>
</table>

  <a href="logout">Logout</a>
</body>
</html>
