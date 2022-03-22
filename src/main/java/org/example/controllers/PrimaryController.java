package org.example.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.scene.control.*;
import org.example.models.Network;

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
    }
    private Network network;

    public void setNetwork(Network network) {
        this.network = network;
    }

    private void showAddUserError() {
        Alert alert = new Alert (Alert.AlertType.ERROR);
        alert.setTitle("AddUserError");
        alert.setHeaderText("This nickname is taken");
        alert.setContentText("Please, choose another one");
        alert.show();
    }

    @FXML
    void clearAll() {
        messageField.clear();
    }
    @FXML
    void getAbout() {
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
            if(!message.getMessage().isBlank()){
                network.sendMessage(message);
                appendMessage(message);
            }
        }catch (NullPointerException e){
            e.printStackTrace();
            showErrorLengthAlert();
        }
        textField.clear();
    }

    public void appendMessage(Message message) {
        messageField.appendText(message.getMessage());
        messageField.appendText(System.lineSeparator());
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