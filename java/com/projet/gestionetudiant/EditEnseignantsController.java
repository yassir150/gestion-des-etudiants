package com.projet.gestionetudiant;

import com.projet.gestionetudiant.models.Enseignants;
import com.projet.gestionetudiant.models.Etudiant;
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

public class EditEnseignantsController implements Initializable {
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

    private Enseignants enseignant;

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

    public void setEnseignant(Enseignants enseignant) {
        this.enseignant = enseignant;
        this.nom_field.setText(enseignant.getNom());
        this.prenom_field.setText(enseignant.getPrenom());
        this.telephone_field.setText(enseignant.getTelehone());
        this.email_field.setText(enseignant.getEmail());
        this.adresse_field.setText(enseignant.getAdresse());
    }

    @FXML
    void edit_enseignant() {
        Integer id = enseignant.getId();
        String nom = nom_field.getText();
        String prenom = prenom_field.getText();
        String adresse = adresse_field.getText();
        String telephone = telephone_field.getText();
        String email = email_field.getText();
        try {
            ps = conn.prepareStatement("update enseignants set nom= ?,prenom= ?,adresse= ?,telephone= ?,email= ? where enseignant_id=?");
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
        System.out.println("Enseignant update avec succès");
    }
}
