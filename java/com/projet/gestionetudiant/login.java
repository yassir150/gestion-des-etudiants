package com.projet.gestionetudiant;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;


public class login extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(login.class.getResource("Login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
        stage.setTitle("login");
        stage.setFullScreen(false);
        stage.resizableProperty().setValue(false);
        Image img = new Image("student.png");
        stage.getIcons().add(img);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}