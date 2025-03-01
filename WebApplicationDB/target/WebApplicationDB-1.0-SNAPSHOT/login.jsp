<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.util.*" %>
<html>
    <head>
        <title>Login</title>
    </head>
    <body style="font-family: Arial, sans-serif; background-color: #f0f2f5; text-align: center; margin: 0; padding: 0;">
        <div style="background: #1877f2; color: white; padding: 15px; font-size: 18px;">
            Welcome! Please log in.
        </div>
        <div style="background: white; width: 90%; max-width: 400px; margin: 50px auto; padding: 20px; border-radius: 8px; box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.1);">
            <h2>Login</h2>

            <% if (request.getParameter("error") != null) { %>
            <p style="color: red;">Invalid username or password. Please try again.</p>
            <% }%>
            <form action="login" method="post">
                <input type="text" name="username" placeholder="Username" required
                       style="width: 100%; padding: 10px; border: 1px solid #ccc; border-radius: 5px; margin-bottom: 10px;">
                <input type="password" name="password" placeholder="Password" required
                       style="width: 100%; padding: 10px; border: 1px solid #ccc; border-radius: 5px; margin-bottom: 10px;">
                <button type="submit" style="background: #1877f2; color: white; border: none; padding: 10px 15px; font-size: 16px; cursor: pointer; border-radius: 5px;">Login</button>
            </form>
            <p style="margin-top: 15px;">Don't have an account? <a href="signup.jsp" style="color: #1877f2;">Sign up</a></p>
        </div>
    </body>
</html>
