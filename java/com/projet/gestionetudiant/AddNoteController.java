package com.projet.gestionetudiant;

import com.projet.gestionetudiant.models.Etudiant;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.*;
import javafx.event.ActionEvent;
import javafx.event.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Alert;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class AddNoteController implements Initializable {

    @FXML
    private ComboBox<?> Cours_Cbox;

    @FXML
    private Label id_label;

    @FXML
    private Label nom_label;

    @FXML
    private TextField note_field;

    @FXML
    private Label prenom_label;
    Integer etu_id ;
    Integer cour_id ;
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
        combox();
    }

    public void combox(){
        String kuery = "Select * from cours";
        try {
            PreparedStatement prepare = conn.prepareStatement(kuery);
            ResultSet rs = prepare.executeQuery();
            ObservableList cours = FXCollections.observableArrayList();
            while (rs.next()) {
                String name = rs.getString("nom");
                cours.add(name);
            }
            Cours_Cbox.setItems(cours);
        }catch (Exception e) {e.printStackTrace();}

    }

    public void setEtudiant(Etudiant etudiant) {
        this.id_label.setText("L'id: " + String.valueOf(etudiant.getId()));
        this.etu_id = etudiant.getId();
        this.nom_label.setText("Le nom: " + etudiant.getNom());
        this.prenom_label.setText("Le prenom: " + etudiant.getPrenom());
    }

    public Integer get_cours_id(String cours) {
        try {
            PreparedStatement get_ens = conn.prepareStatement("SELECT * FROM cours where nom=?");
            get_ens.setString(1, cours);
            ResultSet rs = get_ens.executeQuery();
            if (rs.next()) {
                return cour_id = rs.getInt("cour_id");
            } else {
                System.out.println("No enseignant found with name: " + cours);
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void showAlert(String message, int durationSeconds) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Succès");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(durationSeconds), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                alert.close();
            }
        }));
        timeline.play();
    }

    @FXML
    void Add_note(ActionEvent event) {
        // Check if the note field is empty
        if (note_field.getText().isEmpty()) {
            showAlert("Veuillez entrer une note !", Alert.AlertType.WARNING);
            return;
        }

        // Check if an item is selected in the ComboBox
        if (Cours_Cbox.getSelectionModel().isEmpty()) {
            showAlert("Sélectionnez d'abord un cours !", Alert.AlertType.WARNING);
            return;
        }

        try {
            Integer note = Integer.valueOf(note_field.getText());
            String cours = Cours_Cbox.getSelectionModel().getSelectedItem().toString();
            Integer cours_id = get_cours_id(cours);

            if (cours_id != null) {
                // Vérifier si une note pour cet étudiant sur ce cours existe déjà
                if (noteExists(etu_id, cours_id)) {
                    // Récupérer le nom de l'étudiant et le nom du cours
                    String nomEtudiant = nom_label.getText().split(":")[1].trim();
                    String nomCours = cours;

                    // Afficher une alerte personnalisée avec le nom de l'étudiant et le nom du cours
                    showAlert("La note de " + nomEtudiant + " pour le cours " + nomCours + " existe déjà !", Alert.AlertType.ERROR);
                } else {
                    // Ajouter la note si elle n'existe pas déjà
                    addNoteToDatabase(etu_id, cours_id, note);
                    showAlert("La note de " + nom_label.getText().split(":")[1].trim() + " pour le cours " + cours + " a été ajoutée avec succès !", Alert.AlertType.INFORMATION);
                    Stage stage=(Stage) nom_label.getScene().getWindow();
                    stage.close();
                }
            } else {
                showAlert("Impossible de récupérer l'ID du cours.", Alert.AlertType.ERROR);
            }
        } catch (NumberFormatException e) {
            showAlert("Veuillez entrer une note valide !", Alert.AlertType.ERROR);
        }
    }

    private boolean noteExists(Integer etudiant_id, Integer cours_id) {
        try {
            PreparedStatement checkNote = conn.prepareStatement("SELECT * FROM notes WHERE etudiant_id = ? AND cours_id = ?");
            checkNote.setInt(1, etudiant_id);
            checkNote.setInt(2, cours_id);
            ResultSet rs = checkNote.executeQuery();
            return rs.next(); // Retourne vrai si une note existe, faux sinon
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // En cas d'erreur, supposons qu'une note n'existe pas
        }
    }

    private void addNoteToDatabase(Integer etudiant_id, Integer cours_id, Integer note) {
        try {
            ps = conn.prepareStatement("INSERT INTO notes (etudiant_id, cours_id, note) VALUES (?, ?, ?)");
            ps.setInt(1, etudiant_id);
            ps.setInt(2, cours_id);
            ps.setInt(3, note);
            ps.executeUpdate();
            System.out.println("Note ajoutée avec succès !");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
private void showAlert(String message, Alert.AlertType alertType) {
    Alert alert = new Alert(alertType);
    alert.setTitle("Information");
    alert.setHeaderText(null);
    alert.setContentText(message);

    // Pour fermer automatiquement l'alerte après un certain temps (3 secondes)
    Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(5), event -> {
        alert.close();
    }));
    timeline.play();

    alert.showAndWait();
}
}