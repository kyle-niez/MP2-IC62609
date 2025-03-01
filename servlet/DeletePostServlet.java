package servlet;

import servlet.UserDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class DeletePostServlet extends HttpServlet {
    private final UserDAO userDAO = new UserDAO();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String username = (String) session.getAttribute("username");
        String postIndexStr = request.getParameter("post_index");

        if (postIndexStr != null) {
            try {
                int postIndex = Integer.parseInt(postIndexStr);
                boolean success = userDAO.deletePost(username, postIndex);

                if (success) {
                    System.out.println("Post " + postIndex + " deleted successfully.");
                } else {
                    System.out.println("Failed to delete post.");
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        // Redirect back to the profile page
        response.sendRedirect("ProfileServlet");
    }
}