<%@ page import="java.sql.*" %>
<%@ page import="servlet.DatabaseConnection" %>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>

<%
    if ("POST".equalsIgnoreCase(request.getMethod())) {
        String message = request.getParameter("message");
        String userName = (String) session.getAttribute("username"); 

        if (userName != null && message != null && !message.trim().isEmpty()) {
            Connection conn = null;
            PreparedStatement stmt = null;
            try {
                conn = DatabaseConnection.getConnection();
                String sql = "INSERT INTO messages (user_name, message) VALUES (?, ?)";
                stmt = conn.prepareStatement(sql);
                stmt.setString(1, userName);
                stmt.setString(2, message);

                int rows = stmt.executeUpdate();
                if (rows > 0) {
                    out.println("<p style='color:green;'>Message sent successfully!</p>");
                } else {
                    out.println("<p style='color:red;'>Failed to send message.</p>");
                }
            } catch (SQLException e) {
                out.println("<p style='color:red;'>Error: " + e.getMessage() + "</p>");
            } finally {
                if (stmt != null) {
                    try {
                        stmt.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                if (conn != null) {
                    try {
                        conn.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            out.println("<p style='color:red;'>Please enter a message.</p>");
        }
    }
%>

<html>
<head>
    <title>Help - Contact Admin</title>
</head>
<body>
    <h2>Contact Admin</h2>
    <form method="post">
        <textarea name="message" rows="5" cols="50" placeholder="Enter your message here"></textarea><br>
        <input type="submit" value="Send">
    </form>
</body>
</html>
