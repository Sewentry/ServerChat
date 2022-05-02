package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.controllers.AuthController;
import org.example.controllers.PrimaryController;
import org.example.controllers.RegisterController;
import org.example.models.Network;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    private Network network;
    private Stage stage;
    private Stage authStage;
    private Stage registerStage;
    private PrimaryController primaryController;
    private RegisterController registerController;

    @Override
    public void start(Stage stage) throws IOException {
        network = new Network();
        network.connect();
        this.stage = stage;


       openAuthDialog();
       createRegDialog();
       createChatDialog();

    }

    private void openAuthDialog() throws IOException {
        FXMLLoader authLoader = new FXMLLoader(App.class.getResource("auth-view.fxml"));
        authStage = new Stage();
        scene = new Scene(authLoader.load());
        authStage.setScene(scene);
        authStage.setTitle("Authentication");
        authStage.initModality(Modality.WINDOW_MODAL);
        authStage.initOwner(stage);
        authStage.show();

        AuthController primaryController = authLoader.getController();
        primaryController.setNetwork(network);
        primaryController.setStartClient(this);
    }

    private void createChatDialog() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("primary.fxml"));
        scene = new Scene(fxmlLoader.load(),640, 480);
        stage.setScene(scene);
        primaryController = fxmlLoader.getController();
        primaryController.setNetwork(network);

    }
    private void createRegDialog() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("reg-view.fxml"));
        registerStage = new Stage();
        scene = new Scene(fxmlLoader.load());
        registerStage.setScene(scene);
        RegisterController primaryController = fxmlLoader.getController();
        primaryController.setNetwork(network);
        primaryController.setStartClient(this);

    }
    public static void main(String[] args) {
        launch();
    }
    public void openRegDialog(){
        authStage.close();
        registerStage.show();
        registerStage.setTitle("Registration");
    }
    public void openChatDialog() {
        authStage.close();
        registerStage.close();

        stage.show();
        stage.setTitle(network.getUsername());
        network.waitMessage(primaryController);
        primaryController.setUsernameTitle(network.getUsername());
        primaryController.downloadPreviousMessages();
    }
    public void errorAlert(String titleAlert, String headerAlert){
        Alert alert = new Alert (Alert.AlertType.ERROR);
        alert.setTitle(titleAlert);
        alert.setHeaderText(headerAlert);

        alert.show();
    }




}