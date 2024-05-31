package com.projet.gestionetudiant;

import com.projet.gestionetudiant.models.Etudiant;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class MarquerAbsenceController implements Initializable {
    @FXML
    private DatePicker Date_Enter;

    @FXML
    private TextField Statu_Enter;

    @FXML
    private Label id_label;

    @FXML
    private Label nom_label;

    @FXML
    private Label prenom_label;

    private static Connection conn;
    PreparedStatement ps;
    private Integer id;

    public void getConnect() {
        try {
            conn = DriverManager.getConnection("jdbc:mysql://**put your database here**", "**put your database username here**", "**put your database password here**");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        getConnect();
    }


    public void setEtudiant(Etudiant etudiant) {
        this.id_label.setText("L'id: " + String.valueOf(etudiant.getId()));
        this.id = etudiant.getId();
        this.nom_label.setText("Le nom: " + etudiant.getNom());
        this.prenom_label.setText("Le prenom: " + etudiant.getPrenom());
    }

    @FXML
    void Submit(ActionEvent event) {
        Integer ID = this.id;
        LocalDate Date = Date_Enter.getValue();
        String Statu = Statu_Enter.getText();
        try {
            ps = conn.prepareStatement("insert into absences (date,etudiant_id,statut) values(?,?,?)");
            ps.setDate(1, java.sql.Date.valueOf(Date));
            ps.setInt(2, ID);
            ps.setString(3,Statu);
            ps.executeUpdate();
            System.out.println("insertion reussie");
            Stage stage = (Stage) nom_label.getScene().getWindow();
            stage.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}