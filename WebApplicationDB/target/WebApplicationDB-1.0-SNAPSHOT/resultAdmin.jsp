<%@ page import="java.util.List" %>
<html>
<head>
    <title>Updated Admin Records</title>
</head>
<body>
    <%
       List updatedRecords = (List) request.getAttribute("updatedRecords");
    %>
    
    <% if (updatedRecords != null && !updatedRecords.isEmpty()) { %>
       <h2>Updated User Accounts</h2>
       <table border="1" cellpadding="5">
           <tr>
               <th>Username</th>
               <th>Role</th>
               <th>Password</th>
           </tr>
           <%
              for (Object record : updatedRecords) {
                  String[] rec = (String[]) record;
           %>
           <tr>
               <td><%= rec[0] %></td>
               <td><%= rec[1] %></td>
               <td><%= rec[2] %></td>
           </tr>
           <%
              }
           %>
       </table>
    <% } else { %>
       <p>No accounts were updated.</p>
    <% } %>
    
    <br>
    <form action="superAdmin.jsp" method="get">
        <input type="submit" value="Return to Admin Page">
    </form>
</body>
</html>
