package servlet;

import servlet.UserDAO;
import servlet.Post;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

@WebServlet("/ProfileServlet")
public class ProfileServlet extends HttpServlet {
    private final UserDAO userDAO = new UserDAO();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String username = (String) session.getAttribute("username");

        // Retrieve posts, ensuring a non-null Post object
        Post userPost = userDAO.getPostsByUser(username);
        if (userPost == null) {
            userPost = new Post(username, new ArrayList<>(Arrays.asList(null, null, null, null, null)));
        }

        request.setAttribute("userPost", userPost);
        request.getRequestDispatcher("profile.jsp").forward(request, response);
    }
}