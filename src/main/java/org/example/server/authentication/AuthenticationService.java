package org.example.server.authentication;

import java.sql.SQLException;

public interface AuthenticationService {


    String getUsernameByLoginAndPassword(String login, String password) throws SQLException;

    void startAuthentication();
    void endAuthentication();



}
