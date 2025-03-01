<%@ page import="java.sql.*" %>
<%@ page import="servlet.DatabaseConnection" %>
<html>
<head>
    <title>Bulk Delete Users</title>
</head>
<body>
    <h2>Bulk Delete Users</h2>
    <form method="POST" action="deleteUser">
        <table border="1" cellpadding="5">
            <tr>
                <th>Username</th>
                <th>User Role</th>
                <th>Password</th>
                <th>Select for Delete</th>
            </tr>
            <%
                Connection con = null;
                Statement stmt = null;
                ResultSet rs = null;
                try {
                    con = DatabaseConnection.getConnection();
                    stmt = con.createStatement();
                    rs = stmt.executeQuery("SELECT user_name, user_role, password FROM account WHERE user_role= 'user' OR user_role= 'admin'");
                    while(rs.next()){
                        String userName = rs.getString("user_name");
                        String userRole = rs.getString("user_role");
                        String password = rs.getString("password");
            %>
            <tr>
                <td><%= userName %></td>
                <td><%= userRole %></td>
                <td><%= password %></td>
                <td>
                    <input type="checkbox" name="selectedUsers" value="<%= userName %>">
                </td>
            </tr>
            <%
                    }
                } catch(Exception e) {
                    out.println("Display Error: " + e.getMessage());
                } finally {
                    if(rs != null) try { rs.close(); } catch(Exception e) { }
                    if(stmt != null) try { stmt.close(); } catch(Exception e) { }
                    if(con != null) try { con.close(); } catch(Exception e) { }
                }
            %>
        </table>
        <br>
        <input type="submit" value="Delete Selected Users">
    </form>
</body>
</html>
