package com.projet.gestionetudiant;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
public class AddCoursController implements Initializable {

    @FXML
    private TextField description_field;

    @FXML
    private ComboBox<String> enseignant_Cbox;

    @FXML
    private TextField nom_field;

    Integer enseignant_id;
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
//        get_ens();
    }

    public void combox(){
        String kuery = "Select * from enseignants";
        try {
            PreparedStatement prepare = conn.prepareStatement(kuery);
            ResultSet rs = prepare.executeQuery();
            ObservableList enseignants = FXCollections.observableArrayList();
            while (rs.next()) {
                String name = rs.getString("nom");
                enseignants.add(name);
            }
            enseignant_Cbox.setItems(enseignants);
        }catch (Exception e) {e.printStackTrace();}

    }

    public Integer get_ens(String enseignant) {
        try {
            PreparedStatement get_ens = conn.prepareStatement("SELECT * FROM enseignants where nom=?");
            get_ens.setString(1, enseignant);
            ResultSet rs = get_ens.executeQuery();
            if (rs.next()) {
                return enseignant_id = rs.getInt("enseignant_id");
            } else {
                System.out.println("No enseignant found with name: " + enseignant);
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @FXML
    void add_Cours(ActionEvent event) {
        String nom = nom_field.getText();
        String description = description_field.getText();
        String enseignant = "";
        Object selectedItem = enseignant_Cbox.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            enseignant = selectedItem.toString();
        }
        Integer id = get_ens(enseignant);

        // Check if any of the fields are empty
        if (nom.isEmpty() || description.isEmpty() || enseignant.isEmpty()) {
            showAlert("Veuillez remplir tous les champs !","Warning", Alert.AlertType.WARNING);
            return;
        }

        try {
            ps = conn.prepareStatement("INSERT INTO cours(nom, description, enseignant_id) VALUES (?, ?, ?)");
            ps.setString(1, nom);
            ps.setString(2, description);
            ps.setInt(3, id);
            ps.executeUpdate();

            System.out.println("Cours ajouté "); // Print message to console
            Stage stage = (Stage) nom_field.getScene().getWindow();
            stage.close(); // Close the window
            showAlert("Cours ajouté avec succès","succès", Alert.AlertType.INFORMATION);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Une erreur s'est produite lors de l'ajout du cours !","erreur", Alert.AlertType.ERROR);
        }
    }


    private void showAlert(String message,String title, Alert.AlertType alertType) {
    Alert alert = new Alert(alertType); // Create a new Alert with the specified AlertType
    alert.setTitle(title); // Set the title of the Alert dialog
    alert.setHeaderText(null); // Set the header text (null for no header)
    alert.setContentText(message); // Set the content text of the Alert dialog

    // Show the Alert dialog and wait for the user to close it
    alert.showAndWait();
}
}
