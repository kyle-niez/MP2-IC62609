<form action="createUser" method="post">
    <label>Username:</label> 
    <input type="text" name="username" required><br>

    <label>Password:</label> 
    <input type="password" name="password" required><br>

    <label>User Role:</label>
    <select name="user_role" required>
        <option value="user">User</option>
        <option value="admin">Admin</option>
    </select><br>

    <input type="submit" value="Create User/Admin">
</form>

<a href="superAdmin.jsp">Back</a>
