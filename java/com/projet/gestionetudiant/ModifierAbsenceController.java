package com.projet.gestionetudiant;

import com.projet.gestionetudiant.models.Absence;
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
import java.util.ResourceBundle;

public class ModifierAbsenceController implements Initializable {

    @FXML
    private DatePicker Date_Enter1;

    @FXML
    private TextField Statu_Enter1;

    @FXML
    private Label id_label1;

    @FXML
    private Label nom_label1;

    @FXML
    private Label prenom_label1;

    Integer id;


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
    @FXML
    void Modifier(ActionEvent event) {
        if (Date_Enter1.getValue() != null) {
            String date = Date_Enter1.getValue().toString();
            String statu = Statu_Enter1.getText();
            try {
                ps = conn.prepareStatement("update absences set date =?, statut =? where absence_id =?");
                ps.setString(1, date);
                ps.setString(2, statu);
                ps.setInt(3, this.id);
                ps.executeUpdate();
                Stage stage = (Stage) prenom_label1.getScene().getWindow();
                stage.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.println("Please select a date.");
        }
    }


    public void setAbsence(Absence abs) {
        id_label1.setText("L'id: "+abs.getAbsence_id().toString());
        this.id = abs.getAbsence_id();
        Date_Enter1.getEditor().setText(abs.getDate());
        nom_label1.setText("Le nom: "+abs.getNom_etudiant());
        prenom_label1.setText("Le prenom: "+abs.getPrenom_etudiant());
        Statu_Enter1.setText(abs.getStatut());

    }
}
