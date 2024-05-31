package com.projet.gestionetudiant;

import com.projet.gestionetudiant.models.Cours;
import com.projet.gestionetudiant.models.Notes;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.zip.InflaterInputStream;

public class EditNoteController implements Initializable {

    @FXML
    private ComboBox<String> Cours_Cbox;

    @FXML
    private Label id_label;

    @FXML
    private Label nom_label;

    @FXML
    private TextField note_field;

    @FXML
    private Label prenom_label;

    private Integer cour_id;
    private Integer note_id;

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

    public void combox(){
        String kuery = "Select * from cours";
        try {
            PreparedStatement prepare = conn.prepareStatement(kuery);
            ResultSet rs = prepare.executeQuery();
            ObservableList enseignants = FXCollections.observableArrayList();
            while (rs.next()) {
                String name = rs.getString("nom");
                enseignants.add(name);
            }
            Cours_Cbox.setItems(enseignants);
        }catch (Exception e) {e.printStackTrace();}

    }

    public void setNote(Notes note) {
        id_label.setText("L'id: " + String.valueOf(note.getEtudiant_id()));
        note_id = note.getNote_id();
        nom_label.setText("Le nom: " + note.getNom_etudiant());
        prenom_label.setText("Le prenom: " + note.getPrenom_etudiant());
        Cours_Cbox.setValue(note.getCours());
        note_field.setText(String.valueOf(note.getNote()));
    }


    public Integer get_cour_id(String cour) {
        try {
            PreparedStatement get_ens = conn.prepareStatement("SELECT * FROM cours where nom=?");
            get_ens.setString(1, cour);
            ResultSet rs = get_ens.executeQuery();

            if (rs.next()) {
                return cour_id = rs.getInt("cour_id");
            } else {
                System.out.println("No enseignant found with name: " + cour);
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    @FXML
    void Edit_note(ActionEvent event) {
        Integer cour_id = get_cour_id(Cours_Cbox.getValue());
        Integer note = Integer.valueOf(note_field.getText());
        try {
            ps = conn.prepareStatement("UPDATE notes SET cours_id=?,note=? WHERE note_id =?");
            ps.setInt(1, cour_id);
            ps.setInt(2, note);
            ps.setInt(3, note_id);
            ps.executeUpdate();
            Stage stage = (Stage) note_field.getScene().getWindow();
            stage.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
