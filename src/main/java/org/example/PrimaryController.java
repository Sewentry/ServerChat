package org.example;

import java.io.IOException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

import javafx.event.ActionEvent;
import javafx.scene.control.*;

public class PrimaryController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button Enter;

    @FXML
    private Menu File;

    @FXML
    private Menu Help;
    @FXML
    private MenuItem About;
    @FXML
    private Button addUserButton;

    @FXML
    private Button clearAllButton;

    @FXML
    private TextArea messageField;


    @FXML
    private TextField textField;

    @FXML
    private ListView<String> userField;

    private final ObservableList<String> userList = FXCollections.observableArrayList();

    @FXML
    void addUser() {

        User user = new User(textField.getText());
        if(!userList.contains(user.getNickname()))
        {
            userField.getItems().add(user.getNickname());
            userList.add(user.getNickname());
            textField.clear();
        }
        else{
            showAddUserError();
        }
    }

    private void showAddUserError() {
        Alert alert = new Alert (Alert.AlertType.ERROR);
        alert.setTitle("AddUserError");
        alert.setHeaderText("This nickname is taken");
        alert.setContentText("Please, choose another one");
        alert.show();
    }

    @FXML
    void clearAll(ActionEvent event) {
        messageField.clear();
    }
    @FXML
    void getAbout(ActionEvent event) {
        Alert alert = new Alert (Alert.AlertType.INFORMATION);
        alert.setTitle("Author");
        alert.setHeaderText("About");
        alert.setContentText("Made by Sewentry");
        alert.show();
    }
    @FXML
    void getHelp(ActionEvent event) {

    }

    @FXML
    void sendMessage() {
        Message message = new Message(textField.getText());
        try {
            if(message.getMessage().length()!=0){
                messageField.setText(messageField.getText()+"\n"+message.getMessage());
            }
        }catch (NullPointerException e){
            e.printStackTrace();
            showErrorLengthAlert();
        }
        textField.clear();
    }

    private void showErrorLengthAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("The length of message can't be zero");
        alert.show();
    }

    @FXML
    void initialize() {
        assert About != null : "fx:id=\"About\" was not injected: check your FXML file 'primary.fxml'.";
        assert Enter != null : "fx:id=\"Enter\" was not injected: check your FXML file 'primary.fxml'.";
        assert File != null : "fx:id=\"File\" was not injected: check your FXML file 'primary.fxml'.";
        assert Help != null : "fx:id=\"Help\" was not injected: check your FXML file 'primary.fxml'.";
        assert addUserButton != null : "fx:id=\"addUserButton\" was not injected: check your FXML file 'primary.fxml'.";
        assert clearAllButton != null : "fx:id=\"clearAllButton\" was not injected: check your FXML file 'primary.fxml'.";
        assert messageField != null : "fx:id=\"messageField\" was not injected: check your FXML file 'primary.fxml'.";
        assert textField != null : "fx:id=\"textField\" was not injected: check your FXML file 'primary.fxml'.";
        assert userField != null : "fx:id=\"userField\" was not injected: check your FXML file 'primary.fxml'.";
    }
}