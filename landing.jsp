<%@ page import="java.util.*" %>
<%
    String loggedInUser = (String) request.getAttribute("loggedInUser");
    Map<String, List<String>> userPosts = (Map<String, List<String>>) request.getAttribute("userPosts");
%>

<html>
<head>
    <title>Landing Page</title>
</head>
<body style="font-family: Arial, sans-serif; background-color: #f0f2f5; text-align: center; margin: 0; padding: 0;">
    
    <!-- NAVBAR -->
    <div style="background: #1877f2; color: white; padding: 15px;">
        <a href="LandingServlet" style="color: white; text-decoration: none; margin: 0 15px;">Home</a>
        <a href="profile" style="color: white; text-decoration: none; margin: 0 15px;">Profile</a>
        <a href="users.jsp" style="color: white; text-decoration: none; margin: 0 15px;">Followed Users</a>
        <a href="help.jsp" style="color: white; text-decoration: none; margin: 0 15px;">Help</a>
        <a href="login.jsp" style="color: white; text-decoration: none; margin: 0 15px;">Logout</a>
    </div>

    <!-- CONTENT BOX -->
    <div style="background: white; width: 90%; max-width: 600px; margin: 20px auto; padding: 20px; 
                border-radius: 8px; box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.1); text-align: left;">
        <h2 style="text-align: center;">Welcome, <%= loggedInUser %>!</h2>
        <p style="text-align: center;">Here are posts from users you follow:</p>

        <% if (userPosts != null && !userPosts.isEmpty()) { %>
            <% for (Map.Entry<String, List<String>> entry : userPosts.entrySet()) { %>
                <h3 style="margin-top: 20px; color: #1877f2;"><%= entry.getKey() %>'s Posts:</h3>

                <% for (String post : entry.getValue()) { %>
                    <div style="background: #fff; padding: 15px; border-radius: 6px; margin-bottom: 10px; 
                                box-shadow: 0px 2px 6px rgba(0, 0, 0, 0.1);">
                        <p style="margin: 0; padding: 0; word-wrap: break-word;"><%= post %></p>
                    </div>
                <% } %>

            <% } %>
        <% } else { %>
            <p style="text-align: center;">You are not following any users or they have not posted anything yet.</p>
        <% } %>
    </div>

</body>
</html>
