package servlet;

import servlet.UserDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class CreatePostServlet extends HttpServlet {
    private final UserDAO userDAO = new UserDAO();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String username = (String) session.getAttribute("username");
        String content = request.getParameter("content");

        if (content == null || content.trim().isEmpty()) {
            request.setAttribute("message", "Post content cannot be empty.");
        } else {
            boolean success = userDAO.createPost(username, content);
            request.setAttribute("message", success ? "Post created successfully." : "Failed to create post.");
        }

        response.sendRedirect("ProfileServlet");
    }
}