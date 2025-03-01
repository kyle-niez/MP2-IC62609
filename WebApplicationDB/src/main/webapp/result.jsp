<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<html>
<head>
    <title>Result Page</title>
</head>
<body>
    <%
       List updatedRecords = (List) request.getAttribute("updatedRecords");
       List deletedRecords = (List) request.getAttribute("deletedRecords");
    %>
    
    <% if (updatedRecords != null && !updatedRecords.isEmpty()) { %>
       <h2>Updated User Information</h2>
       <table border="1" cellpadding="5">
           <tr>
               <th>Username</th>
               <th>User Role</th>
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
    <% } else if (deletedRecords != null && !deletedRecords.isEmpty()) { %>
       <h2>Deleted User Information</h2>
       <table border="1" cellpadding="5">
           <tr>
               <th>Username</th>
               <th>User Role</th>
               <th>Password</th>
           </tr>
           <%
              for (Object record : deletedRecords) {
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
       <p>No records were updated or deleted.</p>
    <% } %>
    
    <br>
    <form action="admin.jsp" method="get">
        <input type="submit" value="Return to Admin Page">
    </form>
    
    <%
       session.removeAttribute("updatedRecords");
       session.removeAttribute("deletedRecords");
    %>
</body>
</html>
