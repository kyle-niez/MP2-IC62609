<%@ page import="servlet.Post, java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String userName = (String) session.getAttribute("username");
    if (userName == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    Post userPost = (Post) request.getAttribute("userPost");
    List<String> userPosts = (userPost != null) ? userPost.getPosts() : null;
%>
<!DOCTYPE html>
<html>
<head>
    <title>Profile</title>
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
        <h2>Welcome, <%= userName %></h2>

        <form action="createPost" method="post" style="margin-bottom: 20px;">
            <textarea name="content" required 
                style="width: 100%; height: 80px; padding: 10px; border: 1px solid #ccc; border-radius: 6px; resize: none;"></textarea><br>
            <button type="submit" 
                style="background: #1877f2; color: white; border: none; padding: 10px 15px; border-radius: 5px; cursor: pointer; margin-top: 10px;">
                Post
            </button>
        </form>

        <h2>Your Posts</h2>
        <div style="text-align: left;">
            <% if (userPosts != null && !userPosts.isEmpty()) { %>
                <% for (int i = 0; i < userPosts.size(); i++) { %>
                    <% if (userPosts.get(i) != null) { %>
                        <div style="background: #fff; padding: 15px; border-radius: 6px; margin-bottom: 10px; 
                                    box-shadow: 0px 2px 6px rgba(0, 0, 0, 0.1); position: relative;">
                            <p style="margin: 0; padding: 0 0 10px; word-wrap: break-word;"><%= userPosts.get(i) %></p>
                            <form action="deletePost" method="post" style="display:inline;">
                                <input type="hidden" name="post_index" value="<%= i + 1 %>">
                                <button type="submit" 
                                    style="background: red; color: white; border: none; padding: 5px 10px; 
                                           border-radius: 4px; cursor: pointer; position: absolute; right: 10px; bottom: 10px;">
                                    Delete
                                </button>
                            </form>
                        </div>
                    <% } %>
                <% } %>
            <% } else { %>
                <p>No posts yet.</p>
            <% } %>
        </div>
    </div>
</body>
</html>
