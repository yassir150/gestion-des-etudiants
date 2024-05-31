package com.projet.gestionetudiant;

import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import static javafx.application.Application.launch;
import static javafx.fxml.FXMLLoader.load;

public class HomeController implements Initializable {
    @FXML
    private BorderPane main;

    @FXML
    private Button AbsenceBtn;

    @FXML
    private Button CoursBtn;

    @FXML
    private Button EtudiantBtn;

    @FXML
    private Button NoteBtn;

    @FXML
    private Button EnseignantBtn;

    @FXML
    private Label name_label;
    @FXML
    private Label date;
    String pass;

    public void setName(String name) {
        name_label.setText(name);
    }
    public void setpassword(String password) {
        pass= password;
    }

    @FXML
    void Edit_profile(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader= new FXMLLoader(getClass().getResource("EditProfile.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setTitle("profile Update");
        stage.setFullScreen(false);
        stage.resizableProperty().setValue(false);
        Image img = new Image("student.png");
        stage.getIcons().add(img);
        stage.show();
        EditProfileController controller = fxmlLoader.getController();
    }
    @FXML
    void Change_profile(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader= new FXMLLoader(getClass().getResource("login.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("login");
            stage.setFullScreen(false);
            stage.resizableProperty().setValue(false);
            Image img = new Image("student.png");
            stage.getIcons().add(img);
            stage.show();
            Stage Stage = (Stage) main.getScene().getWindow();
            Stage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void Display_Etudiant(ActionEvent event) throws IOException {
        AnchorPane view = load(getClass().getResource("TableEtudiant.fxml"));
        main.setCenter(view);
    }
    @FXML
    void Display_Cours(ActionEvent event) throws IOException {
        AnchorPane view = load(getClass().getResource("TableCours.fxml"));
        main.setCenter(view);
    }
    @FXML
    void Display_absence(ActionEvent event) throws IOException {
        AnchorPane view = load(getClass().getResource("AbsenceHome.fxml"));
        main.setCenter(view);
    }
    @FXML
    void Display_notes(ActionEvent event) throws IOException {
        AnchorPane view = load(getClass().getResource("NoteHome.fxml"));
        main.setCenter(view);
    }
    @FXML
    void Display_Enseignants(ActionEvent event) throws IOException {
        AnchorPane view = load(getClass().getResource("TableEnseignants.fxml"));
        main.setCenter(view);
    }
    Stage stage ;
    @FXML
    protected void logout(ActionEvent e) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText("do you want to log out?");
        alert.setContentText("do you want to save before logging out?");
        if (alert.showAndWait().get()== ButtonType.OK){
            stage = (Stage) main.getScene().getWindow();
            System.out.println("you are logged out");
            stage.close();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        AnchorPane view = null;
        try {
            view = load(getClass().getResource("TableEtudiant.fxml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        main.setCenter(view);
        timer.start();
    }
    AnimationTimer timer = new AnimationTimer() {
        @Override
        public void handle(long now) {
            date.setText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        }
    };

    public void setName_label(Label name_label) {
        this.name_label = name_label;
    }


}
