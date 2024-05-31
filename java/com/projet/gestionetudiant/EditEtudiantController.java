package com.projet.gestionetudiant;

import com.projet.gestionetudiant.models.Enseignants;
import com.projet.gestionetudiant.models.Etudiant;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class EditEtudiantController implements Initializable {
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

    private Etudiant etu;

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

    public void setEtudiant(Etudiant etudiant) {
        this.etu = etudiant;
        this.nom_field.setText(etudiant.getNom());
        this.prenom_field.setText(etudiant.getPrenom());
        this.telephone_field.setText(etudiant.getTelehone());
        this.email_field.setText(etudiant.getEmail());
        this.adresse_field.setText(etudiant.getAdresse());
    }

    @FXML
    void edit_etudiant() {
        Integer id = etu.getId();
        String nom = nom_field.getText();
        String prenom = prenom_field.getText();
        String adresse = adresse_field.getText();
        String telephone = telephone_field.getText();
        String email = email_field.getText();
        try {
            ps = conn.prepareStatement("update etudiant set nom= ?,prenom= ?,adresse= ?,telephone= ?,email= ? where etudiant_id=?");
            ps.setString(1,nom);
            ps.setString(2,prenom);
            ps.setString(3,adresse);
            ps.setString(4,telephone);
            ps.setString(5,email);
            ps.setInt(6,id);
            ps.executeUpdate();
            Stage stage = (Stage) nom_field.getScene().getWindow();
            stage.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Etudiant update avec succès");
    }
}
