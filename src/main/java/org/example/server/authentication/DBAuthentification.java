package org.example.server.authentication;
import java.sql.*;

public class DBAuthentification implements AuthenticationService {
    private static Connection connection;
    private static Statement stmt;
    private static ResultSet rs;

    @Override
    public String getUsernameByLoginAndPassword(String login, String password) throws SQLException {
        String passwordDB = null;
        String username = null;

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM auth WHERE login = ?");
            preparedStatement.setString(1, login);
            rs = preparedStatement.executeQuery();
            if (rs.isClosed()) {
                return null;
            }

            username = rs.getString("username");
            passwordDB = rs.getString("password");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ((passwordDB != null) && (passwordDB.equals(password))) ? username : null;
    }

    @Override
    public void startAuthentication()
    {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:src/main/resources/org/mainDB.db");
            System.out.println("Connection is good");
            stmt = connection.createStatement();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void endAuthentication() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
