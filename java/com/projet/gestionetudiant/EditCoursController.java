package com.projet.gestionetudiant;

import com.projet.gestionetudiant.models.Cours;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class EditCoursController implements Initializable {
    @FXML
    private TextField description_field;

    @FXML
    private ComboBox<String> enseignant_Cbox;

    @FXML
    private TextField nom_field;

    private Integer enseignant_id;

    private Cours cour;

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
    public void setCours(Cours cours) {
        this.cour = cours;
        nom_field.setText(cour.getNom());
        description_field.setText(cour.getDescription());
        enseignant_Cbox.setValue(cour.getenseignant());
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
    void edit_Cours(ActionEvent event) {
        String nom = nom_field.getText();
        String description = description_field.getText();
        Integer enseignant_id = get_ens(enseignant_Cbox.getValue());
        try {
            ps = conn.prepareStatement("UPDATE cours SET nom =?, description =?, enseignant_id =? WHERE cour_id =?");
            ps.setString(1, nom);
            ps.setString(2, description);
            ps.setInt(3, enseignant_id);
            ps.setInt(4, cour.getCour_id());
            ps.executeUpdate();
            Stage stage = (Stage) nom_field.getScene().getWindow();
            stage.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
