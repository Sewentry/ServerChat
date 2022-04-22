package org.example.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.example.App;
import org.example.Error;
import org.example.models.Network;



public class AuthController {
    private Network network;
    private App startClient;

    @FXML
    private TextField loginField;

    @FXML
    private PasswordField passwordField;


    @FXML
    private Button signIn;

    @FXML
    void addNewAccountButton() {
        startClient.openRegDialog();
    }
    @FXML
    public void checkAuth() {
        String login = loginField.getText().trim();
        String password = passwordField.getText().trim();
        if(login.length()==0 || password.length()==0){
           startClient.errorAlert("emptyAlert","Login or password are empty");
           return;
        }

        String authErrorMessage = network.sendAuthMessage(login,password);
        if(authErrorMessage==null){
            startClient.openChatDialog();
        }else  {
            startClient.errorAlert("wrong date",authErrorMessage);
        }
    }


    public void setNetwork(Network network) {
        this.network = network;
    }

    public void setStartClient(App startClient) {
        this.startClient = startClient;
    }
}
