package servlet;

import servlet.DatabaseHandler;
import servlet.Post;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserDAO {

    public boolean createUser(String username, String password, String role) {
        if (userExists(username)) {
            System.out.println("User creation failed: Username already exists.");
            return false;
        }

        String query = "INSERT INTO account (user_name, password, user_role) VALUES (?, ?, ?)";
        try (Connection connection = DatabaseHandler.getConnection(); PreparedStatement statement = connection.prepareStatement(query)) {

            System.out.println("Attempting to insert: " + username + ", " + role);

            statement.setString(1, username);
            statement.setString(2, password);
            statement.setString(3, role);

            int rowsAffected = statement.executeUpdate();
            System.out.println("Rows affected: " + rowsAffected);

            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("SQL Error: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean userExists(String username) {
        String query = "SELECT user_name FROM account WHERE user_name = ?";
        try (Connection connection = DatabaseHandler.getConnection(); PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            System.err.println("SQL Error: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean isUsernameTaken(String username, String currentUsername) {
        String query = "SELECT user_name FROM account WHERE user_name = ? AND user_name <> ?";
        try (Connection connection = DatabaseHandler.getConnection(); PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, username);
            statement.setString(2, currentUsername);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            System.err.println("SQL Error: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public String loginUser(String username, String password) {
        String query = "SELECT user_role FROM account WHERE user_name = ? AND password = ?";
        try (Connection conn = DatabaseHandler.getConnection(); PreparedStatement pre = conn.prepareStatement(query)) {

            pre.setString(1, username);
            pre.setString(2, password);
            ResultSet res = pre.executeQuery();

            if (res.next()) {
                return res.getString("user_role"); // Return the user role
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;  // Login failed
    }

    public String updateUser(String currentUsername, String newUsername, String newPassword, String newRole) {
        if (newUsername != null && !newUsername.isEmpty() && isUsernameTaken(newUsername, currentUsername)) {
            return "Username is already taken";
        }

        StringBuilder query = new StringBuilder("UPDATE account SET ");
        boolean hasUpdate = false;

        if (newUsername != null && !newUsername.isEmpty()) {
            query.append("user_name = ?, ");
            hasUpdate = true;
        }
        if (newPassword != null && !newPassword.isEmpty()) {
            query.append("password = ?, ");
            hasUpdate = true;
        }

        // Ensure user_role is always included
        query.append("user_role = ?, ");
        hasUpdate = true;

        if (!hasUpdate) {
            return "No changes provided";
        }

        query.setLength(query.length() - 2);
        query.append(" WHERE user_name = ?");

        System.out.println("Generated SQL: " + query.toString());

        try (Connection conn = DatabaseHandler.getConnection(); PreparedStatement stmt = conn.prepareStatement(query.toString())) {

            int paramIndex = 1;
            if (newUsername != null && !newUsername.isEmpty()) {
                stmt.setString(paramIndex++, newUsername);
                System.out.println("Parameter " + (paramIndex - 1) + ": " + newUsername);
            }
            if (newPassword != null && !newPassword.isEmpty()) {
                stmt.setString(paramIndex++, newPassword);
                System.out.println("Parameter " + (paramIndex - 1) + ": " + newPassword);
            }

            // If newRole is null, keep the existing role
            if (newRole == null || newRole.isEmpty()) {
                newRole = getCurrentRole(currentUsername);  // Fetch the existing role
            }
            stmt.setString(paramIndex++, newRole);
            System.out.println("Parameter " + (paramIndex - 1) + ": " + newRole);

            stmt.setString(paramIndex, currentUsername);
            System.out.println("Parameter " + paramIndex + ": " + currentUsername);

            int rowsAffected = stmt.executeUpdate();
            return (rowsAffected > 0) ? "Update successful" : "Failed to update user";
        } catch (SQLException e) {
            System.err.println("SQL Error (Update): " + e.getMessage());
            e.printStackTrace();
            return "Database error occurred";
        }
    }

    private String getCurrentRole(String username) {
        String role = null;
        try (Connection conn = DatabaseHandler.getConnection(); PreparedStatement stmt = conn.prepareStatement("SELECT user_role FROM account WHERE user_name = ?")) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                role = rs.getString("user_role");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return (role != null) ? role : "user";
    }

    public boolean deleteUsers(String[] usernames) {
        if (usernames == null || usernames.length == 0) {
            return false;
        }

        String query = "DELETE FROM account WHERE user_name = ?";
        try (Connection conn = DatabaseHandler.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {

            for (String username : usernames) {
                stmt.setString(1, username);
                stmt.addBatch();
            }

            int[] rowsAffected = stmt.executeBatch();
            return rowsAffected.length > 0;
        } catch (SQLException e) {
            System.err.println("SQL Error (Delete): " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean createPost(String username, String content) {
        String checkQuery = "SELECT COUNT(*) FROM posts WHERE user_name = ?";
        String updateQuery = "UPDATE posts SET post5 = post4, post4 = post3, post3 = post2, post2 = post1, post1 = ? WHERE user_name = ?";
        String insertQuery = "INSERT INTO posts (user_name, post1) VALUES (?, ?)";

        try (Connection connection = DatabaseHandler.getConnection(); PreparedStatement checkStmt = connection.prepareStatement(checkQuery); PreparedStatement updateStmt = connection.prepareStatement(updateQuery); PreparedStatement insertStmt = connection.prepareStatement(insertQuery)) {

            // Check if user already has posts
            checkStmt.setString(1, username);
            ResultSet rs = checkStmt.executeQuery();
            rs.next();
            boolean userExists = rs.getInt(1) > 0;

            if (userExists) {
                // Update existing posts (shift old ones)
                updateStmt.setString(1, content);
                updateStmt.setString(2, username);
                updateStmt.executeUpdate();
            } else {
                // Insert a new row
                insertStmt.setString(1, username);
                insertStmt.setString(2, content);
                insertStmt.executeUpdate();
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Post getPostsByUser(String userName) {
        List<String> posts = new ArrayList<>(Arrays.asList(null, null, null, null, null)); // Ensure fixed size list
        String query = "SELECT post1, post2, post3, post4, post5 FROM posts WHERE user_name = ?";

        try (Connection conn = DatabaseHandler.getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, userName);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    for (int i = 1; i <= 5; i++) {
                        posts.set(i - 1, rs.getString("post" + i));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new Post(userName, posts);
    }

    public boolean deletePost(String username, int postNumber) {
        if (postNumber < 1 || postNumber > 5) {
            return false; // Validate input
        }

        String queryCheck = "SELECT COUNT(*) FROM posts WHERE user_name = ?";
        StringBuilder updateQuery = new StringBuilder("UPDATE posts SET post" + postNumber + " = NULL");

        // Shift posts up if the deleted post is not the last one
        for (int i = postNumber; i < 5; i++) {
            updateQuery.append(", post").append(i).append(" = post").append(i + 1);
        }
        updateQuery.append(", post5 = NULL WHERE user_name = ?");

        try (Connection conn = DatabaseHandler.getConnection(); PreparedStatement checkStmt = conn.prepareStatement(queryCheck); PreparedStatement ps = conn.prepareStatement(updateQuery.toString())) {

            checkStmt.setString(1, username);
            ResultSet rs = checkStmt.executeQuery();
            rs.next();

            if (rs.getInt(1) == 0) {
                return false; // No posts to delete
            }

            ps.setString(1, username);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean sendMessage(String username, String messageContent) {
        String insertQuery = "INSERT INTO messages (user_name, message_content) VALUES (?, ?)";
        String deleteOldQuery = "DELETE FROM messages WHERE message_id NOT IN (SELECT message_id FROM (SELECT message_id FROM messages ORDER BY message_date DESC LIMIT 5) AS temp)";

        try (Connection connection = DatabaseHandler.getConnection(); PreparedStatement insertStmt = connection.prepareStatement(insertQuery); PreparedStatement deleteStmt = connection.prepareStatement(deleteOldQuery)) {

            // Insert new message
            insertStmt.setString(1, username);
            insertStmt.setString(2, messageContent);
            insertStmt.executeUpdate();

            // Delete old messages beyond the latest 5
            deleteStmt.executeUpdate();

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}