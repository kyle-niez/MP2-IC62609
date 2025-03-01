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
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f0f2f5;
            text-align: center;
            margin: 0;
            padding: 0;
        }
        .navbar {
            background: #1877f2;
            color: white;
            padding: 15px;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }
        .navbar a {
            color: white;
            text-decoration: none;
            margin: 0 15px;
        }
        .logout {
            background: red;
            color: white;
            padding: 10px 15px;
            text-decoration: none;
            border-radius: 5px;
        }
        .container {
            background: white;
            width: 90%;
            max-width: 800px;
            margin: 20px auto;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.1);
            text-align: left;
        }
        h2, h3 {
            color: #333;
            text-align: center;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 15px;
        }
        th, td {
            border: 1px solid #ddd;
            padding: 8px;
            text-align: left;
        }
        th {
            background-color: #1877f2;
            color: white;
        }
    </style>
</head>
<body>
    <div class="navbar">
        <div>
            <a href="create.jsp">Create User</a>
            <a href="update.jsp">Update User</a>
            <a href="delete.jsp">Delete User</a>
        </div>
        <a href="logout" class="logout">Logout</a>
    </div>
    
    <div class="container">
        <h2>Admin Panel</h2>
        <h3>Messages to Admin</h3>
        <table>
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
    </div>
</body>
</html>
