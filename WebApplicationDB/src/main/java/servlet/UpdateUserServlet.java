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

@WebServlet("/UpdateUserServlet")
public class UpdateUserServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String[] oldUsernames = request.getParameterValues("oldUsername");
        String[] newUsernames = request.getParameterValues("newUsername");
        String[] newPasswords = request.getParameterValues("newPassword");

        List<String> updatedUsernames = new ArrayList<>();
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = DatabaseConnection.getConnection();

            for (int i = 0; i < oldUsernames.length; i++) {
                String oldName = oldUsernames[i];
                String newName = (newUsernames[i] != null) ? newUsernames[i].trim() : "";
                String newPass = (newPasswords[i] != null) ? newPasswords[i].trim() : "";
                
                if (!newName.equals("") || !newPass.equals("")) {
                    String sql = "UPDATE account SET ";
                    boolean firstField = true;
                    
                    if (!newName.equals("")) {
                        sql += "user_name = ?";
                        firstField = false;
                    }
                    if (!newPass.equals("")) {
                        if (!firstField) {
                            sql += ", ";
                        }
                        sql += "password = ?";
                    }
                    sql += " WHERE user_name = ?";
                    
                    ps = con.prepareStatement(sql);
                    int paramIndex = 1;
                    if (!newName.equals("")) {
                        ps.setString(paramIndex++, newName);
                    }
                    if (!newPass.equals("")) {
                        ps.setString(paramIndex++, newPass);
                    }
                    ps.setString(paramIndex, oldName);
                    
                    int rowsAffected = ps.executeUpdate();
                    ps.close();

                    if (rowsAffected > 0) {
                        String effectiveUsername = (!newName.equals("")) ? newName : oldName;
                        updatedUsernames.add(effectiveUsername);
                    }
                }
            }
            

            List<String[]> updatedRecords = new ArrayList<>();
            if (!updatedUsernames.isEmpty()) {
                StringBuilder queryBuilder = new StringBuilder(
                        "SELECT user_name, user_role, password FROM account WHERE user_name IN (");
                for (int i = 0; i < updatedUsernames.size(); i++) {
                    queryBuilder.append("?");
                    if (i < updatedUsernames.size() - 1) {
                        queryBuilder.append(",");
                    }
                }
                queryBuilder.append(")");
                
                ps = con.prepareStatement(queryBuilder.toString());
                for (int i = 0; i < updatedUsernames.size(); i++) {
                    ps.setString(i + 1, updatedUsernames.get(i));
                }
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    String userName = rs.getString("user_name");
                    String userRole = rs.getString("user_role");
                    String password = rs.getString("password");
                    updatedRecords.add(new String[]{userName, userRole, password});
                }
                rs.close();
                ps.close();
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
        request.getRequestDispatcher("result.jsp").forward(request, response);
    }
}
