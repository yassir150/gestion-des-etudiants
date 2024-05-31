package com.projet.gestionetudiant;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class EditProfileController implements Initializable {

    @FXML
    private PasswordField newPass;

    @FXML
    private PasswordField oldPas;

    @FXML
    private TextField usernameField;

    private static Connection conn;
    private PreparedStatement ps;

    // Méthode pour établir la connexion à la base de données
    public void getConnect() {
        try {
            conn = DriverManager.getConnection("jdbc:mysql://**put your database here**", "**put your database username here**", "**put your database password here**");
        } catch (SQLException e) {
            throw new RuntimeException("Échec de la connexion à la base de données", e);
        }
    }

    // Initialisation du contrôleur
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        getConnect();
    }

    // Méthode appelée lors du clic sur le bouton "Modifier le profil"
    @FXML
    void Edit_profile(ActionEvent event) {
        String username = usernameField.getText();
        String newPassword = newPass.getText();
        String oldPassword = oldPas.getText();
        try {
            // Préparation de la requête SQL pour mettre à jour le profil
            ps = conn.prepareStatement("UPDATE users SET username=?, password=? WHERE password=? ");
            ps.setString(1, username);
            ps.setString(2, newPassword);
            ps.setString(3, oldPassword);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Profil mis à jour avec succès");
            } else {
                showAlert(Alert.AlertType.ERROR, "Échec de la mise à jour du profil", "Ancien mot de passe ou nom d'utilisateur invalide");
            }
            // Fermeture de la fenêtre de l'application
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.close();
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur de base de données", "Échec de la mise à jour du profil : " + e.getMessage());
        }
    }

    // Méthode pour afficher une alerte à l'utilisateur
    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
