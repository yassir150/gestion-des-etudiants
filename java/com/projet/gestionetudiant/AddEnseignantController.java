package com.projet.gestionetudiant;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AddEnseignantController implements Initializable {
    @FXML
    private TextField adresse_field;

    @FXML
    private TextField email_field;

    @FXML
    private TextField nom_field;

    @FXML
    private TextField prenom_field;

    @FXML
    private TextField telephone_field;

    private static Connection conn;
    PreparedStatement ps;
    public void getConnect(){
        try {
            conn = DriverManager.getConnection("jdbc:mysql://**put your database here**","**put your database username here**","**put your database password here**");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    @Override
    public void initialize(URL url, ResourceBundle rb){
        getConnect();
    }



    private void showAlert(String message,String title, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType); // Create a new Alert with the specified AlertType
        alert.setTitle(title); // Set the title of the Alert dialog
        alert.setHeaderText(null); // Set the header text (null for no header)
        alert.setContentText(message); // Set the content text of the Alert dialog

        // Show the Alert dialog and wait for the user to close it
        alert.showAndWait();
    }

    @FXML
    void ajouter_enseignant_action(ActionEvent event) {
        String nom = nom_field.getText();
        String prenom = prenom_field.getText();
        String adresse = adresse_field.getText();
        String telephone = telephone_field.getText();
        String email = email_field.getText();

        // Check if any of the fields are empty
        if (nom.isEmpty() || prenom.isEmpty() || adresse.isEmpty() || telephone.isEmpty() || email.isEmpty()) {
            showAlert("Veuillez remplir tous les champs !","warning", Alert.AlertType.WARNING);
            return;
        }

        try {
            ps = conn.prepareStatement("INSERT INTO enseignants(nom, prenom, adresse, telephone, email) VALUES (?, ?, ?, ?, ?)");
            ps.setString(1, nom);
            ps.setString(2, prenom);
            ps.setString(3, adresse);
            ps.setString(4, telephone);
            ps.setString(5, email);
            ps.executeUpdate();

            showAlert("Enseignant " + nom + " " + prenom + " ajouté avec succès", "succès",Alert.AlertType.INFORMATION); // Show success message
            Stage stage = (Stage) nom_field.getScene().getWindow();
            stage.close();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Une erreur s'est produite lors de l'ajout de l'enseignant !","erreur", Alert.AlertType.ERROR);
        }
    }

}