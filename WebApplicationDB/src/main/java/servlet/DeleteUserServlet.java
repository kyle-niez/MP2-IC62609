package servlet;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/DeleteUserServlet")
public class DeleteUserServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String[] selectedUsers = request.getParameterValues("selectedUsers");

        List<String[]> deletedRecords = new ArrayList<>();
        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = DatabaseConnection.getConnection();

            if (selectedUsers != null && selectedUsers.length > 0) {
                StringBuilder selectQuery = new StringBuilder(
                        "SELECT user_name, user_role, password FROM account WHERE user_name IN (");
                for (int i = 0; i < selectedUsers.length; i++) {
                    selectQuery.append("?");
                    if (i < selectedUsers.length - 1) {
                        selectQuery.append(",");
                    }
                }
                selectQuery.append(")");

                ps = con.prepareStatement(selectQuery.toString());
                for (int i = 0; i < selectedUsers.length; i++) {
                    ps.setString(i + 1, selectedUsers[i]);
                }
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    String userName = rs.getString("user_name");
                    String userRole = rs.getString("user_role");
                    String password = rs.getString("password");
                    deletedRecords.add(new String[]{userName, userRole, password});
                }
                rs.close();
                ps.close();

                StringBuilder updateFollowsQuery = new StringBuilder(
                        "UPDATE follows SET " +
                        "follow1 = CASE WHEN follow1 IN (");
                for (int i = 0; i < selectedUsers.length; i++) {
                    updateFollowsQuery.append("?");
                    if (i < selectedUsers.length - 1) {
                        updateFollowsQuery.append(",");
                    }
                }
                updateFollowsQuery.append(") THEN NULL ELSE follow1 END, " +
                        "follow2 = CASE WHEN follow2 IN (");
                for (int i = 0; i < selectedUsers.length; i++) {
                    updateFollowsQuery.append("?");
                    if (i < selectedUsers.length - 1) {
                        updateFollowsQuery.append(",");
                    }
                }
                updateFollowsQuery.append(") THEN NULL ELSE follow2 END, " +
                        "follow3 = CASE WHEN follow3 IN (");
                for (int i = 0; i < selectedUsers.length; i++) {
                    updateFollowsQuery.append("?");
                    if (i < selectedUsers.length - 1) {
                        updateFollowsQuery.append(",");
                    }
                }
                updateFollowsQuery.append(") THEN NULL ELSE follow3 END");

                ps = con.prepareStatement(updateFollowsQuery.toString());
                int paramIndex = 1;
                for (String user : selectedUsers) {
                    ps.setString(paramIndex++, user);
                }
                for (String user : selectedUsers) {
                    ps.setString(paramIndex++, user);
                }
                for (String user : selectedUsers) {
                    ps.setString(paramIndex++, user);
                }
                ps.executeUpdate();
                ps.close();
                
                StringBuilder deleteQuery = new StringBuilder(
                        "DELETE FROM account WHERE user_name IN (");
                for (int i = 0; i < selectedUsers.length; i++) {
                    deleteQuery.append("?");
                    if (i < selectedUsers.length - 1) {
                        deleteQuery.append(",");
                    }
                }
                deleteQuery.append(")");

                ps = con.prepareStatement(deleteQuery.toString());
                for (int i = 0; i < selectedUsers.length; i++) {
                    ps.setString(i + 1, selectedUsers[i]);
                }
                ps.executeUpdate();
                ps.close();
            }
            request.setAttribute("deletedRecords", deletedRecords);
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "Error deleting records: " + e.getMessage());
        } finally {
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
        request.getRequestDispatcher("result.jsp").forward(request, response);
    }
}
