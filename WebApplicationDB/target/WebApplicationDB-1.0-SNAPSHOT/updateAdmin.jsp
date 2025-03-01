<%@ page import="java.sql.*" %>
<%@ page import="servlet.DatabaseConnection" %>
<html>
<head>
    <title>Update Admin - Manage User Accounts</title>
</head>
<body>
    <h2>Manage User Accounts</h2>
    <form method="POST" action="UpdateAdminServlet">
        <table border="1" cellpadding="5">
            <tr>
                <th>Current Username</th>
                <th>Current Password</th>
                <th>Current Role</th>
                <th>New Username</th>
                <th>New Password</th>
                <th>New Role</th>
            </tr>
            <%
                Connection con = null;
                PreparedStatement ps = null;
                ResultSet rs = null;
                try {
                    con = DatabaseConnection.getConnection();
                    String sql = "SELECT user_name, password, user_role FROM account WHERE user_role != 'super_admin'";
                    ps = con.prepareStatement(sql);
                    rs = ps.executeQuery();
                    
                    while(rs.next()){
                        String userName = rs.getString("user_name");
                        String password = rs.getString("password");
                        String userRole = rs.getString("user_role");
            %>
            <tr>
                <td><%= userName %></td>
                <td><%= password %></td>
                <td><%= userRole %></td>
                <td>
                    <input type="hidden" name="oldUsernames" value="<%= userName %>">
                    <input type="text" name="newUsernames">
                </td>
                <td>
                    <input type="text" name="newPasswords">
                </td>
                <td>
                    <select name="newRoles">
                        <option value="">Select Role</option>
                        <option value="admin">Admin</option>
                        <option value="user">User</option>
                    </select>
                </td>
            </tr>
            <%
                    }
                } catch(Exception e){
                    out.println("Display Error: " + e.getMessage());
                } finally {
                    if(rs != null) try { rs.close(); } catch(Exception e) { }
                    if(ps != null) try { ps.close(); } catch(Exception e) { }
                    if(con != null) try { con.close(); } catch(Exception e) { }
                }
            %>
        </table>
        <br>
        <input type="submit" value="Update Accounts">
    </form>
</body>
</html>
