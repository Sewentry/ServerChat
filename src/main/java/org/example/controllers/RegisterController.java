package org.example.controllers;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.example.App;
import org.example.models.Network;

public class RegisterController {
    private Network network;
    private App startClient;

    @FXML
    private TextField loginField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button register;

    @FXML
    private Label username;

    @FXML
    private PasswordField usernameField;

    @FXML
    void register() {
        String login = loginField.getText().trim();
        String password = passwordField.getText().trim();
        String username = usernameField.getText().trim();
        if(login.length()==0 || password.length()==0 || username.length()==0){
            startClient.errorAlert("emptyAlert","Login or password or username are empty");
            return;
        }
        String registerErrorMessage = network.sendRegisterMessage(login,password,username);
        if(registerErrorMessage==null){
            startClient.openChatDialog();
        }else  {
            startClient.errorAlert("wrong date",registerErrorMessage);
        }
    }

    public void setNetwork(Network network) {
        this.network = network;
    }

    public void setStartClient(App startClient) {
        this.startClient = startClient;
    }

}