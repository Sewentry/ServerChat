package org.example.controllers;

import javafx.fxml.FXML;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.util.*;

import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import org.example.models.Network;

public class PrimaryController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;
    @FXML
    private Button changeUsername;
    @FXML
    private Button Enter;

    @FXML
    private Menu File;

    @FXML
    private Menu Help;
    @FXML
    private MenuItem About;

    @FXML
    private Button clearAllButton;

    @FXML
    private TextArea messageField;

    @FXML
    private Label userNameField;

    @FXML
    private TextField textField;

    @FXML
    private ListView<String> userField;


    private String selectedRecipient;
    private Set<String> usersOnlineSet;

    public void setNetwork(Network network) {
        this.network = network;
    }

    @FXML
    void initialize() {
        assert About != null : "fx:id=\"About\" was not injected: check your FXML file 'primary.fxml'.";
        assert Enter != null : "fx:id=\"Enter\" was not injected: check your FXML file 'primary.fxml'.";
        assert File != null : "fx:id=\"File\" was not injected: check your FXML file 'primary.fxml'.";
        assert Help != null : "fx:id=\"Help\" was not injected: check your FXML file 'primary.fxml'.";
        assert clearAllButton != null : "fx:id=\"clearAllButton\" was not injected: check your FXML file 'primary.fxml'.";
        assert messageField != null : "fx:id=\"messageField\" was not injected: check your FXML file 'primary.fxml'.";
        assert textField != null : "fx:id=\"textField\" was not injected: check your FXML file 'primary.fxml'.";
        assert userField != null : "fx:id=\"userField\" was not injected: check your FXML file 'primary.fxml'.";

        userField.setCellFactory(lv -> {
            MultipleSelectionModel<String> selectionModel = userField.getSelectionModel();
            ListCell<String> cell = new ListCell<>();
            cell.textProperty().bind(cell.itemProperty());
            cell.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
                userField.requestFocus();
                if (!cell.isEmpty()) {
                    int index = cell.getIndex();
                    if (selectionModel.getSelectedIndices().contains(index)) {
                        selectionModel.clearSelection(index);
                        selectedRecipient = null;
                    } else {

                            selectionModel.select(index);
                            selectedRecipient = cell.getItem();
                        if(selectedRecipient.equals(userNameField.getText())){
                            selectedRecipient=null;
                        }

                    }
                    event.consume();
                }
            });
            return cell;
        });
    }
    private Network network;

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
        String message = textField.getText();

            if(message.isBlank()){
               return;
            }
            if(selectedRecipient!=null){
                network.sendPrivateMessage(selectedRecipient,message);
            }else{
                network.sendMessage(message);
            }
            appendMessage("Я: " + message);

        textField.clear();
    }
    @FXML
    public void changeUsername () throws IOException {
        String username = textField.getText();
        network.sendNewUsername(username);
        textField.clear();
        userNameField.setText(username);
    }
    public void changeUsersOnlineList(String oldUser, String newUser){
        userField.getItems().remove(oldUser);
        userField.getItems().add(newUser);
    }


    public void appendMessage(String message) {
        String timeStamp = DateFormat.getInstance().format(new Date());
        messageField.appendText(timeStamp);
        messageField.appendText(System.lineSeparator());
        messageField.appendText(message);
        messageField.appendText(System.lineSeparator());
        messageField.appendText(System.lineSeparator());
    }

    public void appendServerMessage(String serverMessage) {
        messageField.appendText(serverMessage);
        messageField.appendText(System.lineSeparator());
        messageField.appendText(System.lineSeparator());
    }


    public void setUsernameTitle (String username){
        userNameField.setText(username);
    }
    public void setAddUsersOnline(String userOnline) {
        if(!userField.getItems().contains(userOnline))
       userField.getItems().add(userOnline);
    }
    public void setRemoveUserOnline (String userOnline){
        userField.getItems().remove(userOnline);
    }
}