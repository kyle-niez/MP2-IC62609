package servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/UpdateAdminServlet")
public class UpdateAdminServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String[] oldUsernames = request.getParameterValues("oldUsernames");
        String[] newUsernames = request.getParameterValues("newUsernames");
        String[] newPasswords = request.getParameterValues("newPasswords");
        String[] newRoles = request.getParameterValues("newRoles");

        List<String[]> updatedRecords = new ArrayList<>();
        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = DatabaseConnection.getConnection();

            for (int i = 0; i < oldUsernames.length; i++) {
                String oldUsername = oldUsernames[i];
                String newUsername = (newUsernames[i] != null && !newUsernames[i].trim().isEmpty()) ? newUsernames[i].trim() : "";
                String newPassword = (newPasswords[i] != null && !newPasswords[i].trim().isEmpty()) ? newPasswords[i].trim() : "";
                String newRole = (newRoles[i] != null && !newRoles[i].trim().isEmpty()) ? newRoles[i].trim() : "";

                if (!newUsername.equals("") || !newPassword.equals("") || !newRole.equals("")) {
                    String sql = "UPDATE account SET ";
                    boolean firstField = true;

                    if (!newUsername.equals("")) {
                        sql += "user_name = ?";
                        firstField = false;
                    }
                    if (!newPassword.equals("")) {
                        if (!firstField) sql += ", ";
                        sql += "password = ?";
                        firstField = false;
                    }
                    if (!newRole.equals("")) {
                        if (!firstField) sql += ", ";
                        sql += "user_role = ?";
                    }
                    sql += " WHERE user_name = ?";

                    ps = con.prepareStatement(sql);
                    int paramIndex = 1;

                    if (!newUsername.equals("")) {
                        ps.setString(paramIndex++, newUsername);
                    }
                    if (!newPassword.equals("")) {
                        ps.setString(paramIndex++, newPassword);
                    }
                    if (!newRole.equals("")) {
                        ps.setString(paramIndex++, newRole);
                    }
                    ps.setString(paramIndex, oldUsername);

                    int rowsAffected = ps.executeUpdate();
                    ps.close();

                    if (rowsAffected > 0) {
                        String effectiveUsername = (!newUsername.equals("")) ? newUsername : oldUsername;
                        String effectiveRole = (!newRole.equals("")) ? newRole : "No Change";
                        String effectivePassword = (!newPassword.equals("")) ? newPassword : "Not Updated";

                        updatedRecords.add(new String[]{effectiveUsername, effectiveRole, effectivePassword});
                    }
                }
            }

            request.setAttribute("updatedRecords", updatedRecords);
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Error updating records: " + e.getMessage());
        } finally {
            if (con != null) {
                try { con.close(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
        }
        request.getRequestDispatcher("resultAdmin.jsp").forward(request, response);
    }
}
