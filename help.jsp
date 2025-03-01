<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%
    String sentMessage = request.getParameter("sent");
%>
<html>
<head>
    <title>Help - Contact Admin</title>
    <style>
        @keyframes shake {
            0% { transform: translateX(0); }
            25% { transform: translateX(-5px); }
            50% { transform: translateX(5px); }
            75% { transform: translateX(-5px); }
            100% { transform: translateX(0); }
        }

        .shake-effect {
            color: green;
            font-weight: bold;
            display: inline-block;
            animation: shake 0.5s ease-in-out;
        }
    </style>
    <script>
        function applyShakeEffect() {
            let message = document.getElementById("sentMessage");
            if (message) {
                message.classList.remove("shake-effect"); // Reset animation
                void message.offsetWidth; // Force reflow to restart animation
                message.classList.add("shake-effect");
            }
        }

        window.onload = function() {
            let urlParams = new URLSearchParams(window.location.search);
            if (urlParams.has('sent')) {
                let message = document.getElementById("sentMessage");
                if (message) {
                    message.style.display = "block"; // Show message
                    applyShakeEffect();
                }

                history.replaceState(null, null, window.location.pathname); // Remove ?sent=true from URL
            }
        };
    </script>
</head>
<body style="font-family: Arial, sans-serif; background-color: #f0f2f5; text-align: center; margin: 0; padding: 0;">

    <div style="background: #1877f2; color: white; padding: 15px;">
        <a href="LandingServlet" style="color: white; text-decoration: none; margin: 0 15px;">Home</a>
        <a href="profile" style="color: white; text-decoration: none; margin: 0 15px;">Profile</a>
        <a href="users.jsp" style="color: white; text-decoration: none; margin: 0 15px;">Followed Users</a>
        <a href="help.jsp" style="color: white; text-decoration: none; margin: 0 15px;">Help</a>
        <a href="login.jsp" style="color: white; text-decoration: none; margin: 0 15px;">Logout</a>
    </div>

    <div style="background: white; width: 90%; max-width: 600px; margin: 20px auto; padding: 20px; 
                border-radius: 8px; box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.1);">
        <h2>Contact Admin</h2>

        <% if ("true".equals(sentMessage)) { %>
            <p id="sentMessage" style="display: none;">Sent Successfully!</p>
        <% } %>

        <form method="post" action="help.jsp?sent=true">
            <textarea name="message" rows="5" cols="50" placeholder="Enter your message here" required
                style="width: 100%; padding: 10px; border: 1px solid #ccc; border-radius: 5px;"></textarea><br>
            <input type="submit" value="Send" 
                style="background: #1877f2; color: white; border: none; padding: 10px 15px; border-radius: 5px; cursor: pointer; margin-top: 10px;">
        </form>
    </div>

</body>
</html>
