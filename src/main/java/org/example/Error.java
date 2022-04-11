package org.example;

public enum Error {
    AUTH_CMD_PREFIX("/auth"),
    AUTHOK_CMD_PREFIX ("/authok"),
    AUTHERR_CMD_PREFIX ("/autherr"),
    CLIENT_MSG_CMD_PREFIX ("/cMsg"),
    SERVER_MSG_CMD_PREFIX ("/sMsg"),
    PRIVAT_MSG_CMD_PREFIX ("/pMsg"),
    STOP_SERVER_CMD_PREFIX ("/stop"),
    SERVER_ADD_USER_ONLINE_PREFIX("/userADD"),
    SERVER_REMOVE_USER_ONLINE_PREFIX("/userREMOVE"),
    SERVER_CHANGE_USERNAME_PREFIX("/usernameChange"),
    END_CLIENT_CMD_PREFIX ("/end");



    String currency;

    Error(String currency) {
        this.currency = currency;
    }
    public String getText (){
        return this.currency;
    }
}

