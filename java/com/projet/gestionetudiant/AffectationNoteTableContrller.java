package com.projet.gestionetudiant;

import com.projet.gestionetudiant.models.Etudiant;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class AffectationNoteTableContrller implements Initializable {
    public TableView<Etudiant> etudiantTable;

    @FXML
    private TableColumn<Etudiant, Number> etudiant_id;

    @FXML
    private TableColumn<Etudiant, String> nom;

    @FXML
    private TableColumn<Etudiant, String> prenom;

    @FXML
    private TableColumn<Etudiant, String> telephone;

    @FXML
    private TableColumn<Etudiant, String> email;

    @FXML
    private TableColumn<Etudiant, String> adresse;

    @FXML
    private AnchorPane main;
    @FXML
    void fichier_notes(ActionEvent event) throws IOException {
        main.getChildren().clear();
        main.getChildren().add(FXMLLoader.load(getClass().getResource("TableNotes.fxml")));
    }

    String query = null;
    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;


    @Override
    public void initialize(URL url, ResourceBundle rb){
        getConnect();
        table();

    }

    public void getConnect(){
        try {
            conn = DriverManager.getConnection("jdbc:mysql://**put your database here**","**put your database username here**","**put your database password here**");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void table(){
        String sql  = "SELECT  * FROM etudiant";
        try {
            ResultSet rs = conn.createStatement().executeQuery(sql);
            ObservableList<Etudiant> List_Etudiant = FXCollections.observableArrayList();
            while (rs.next()) {
                Etudiant e = new Etudiant();
                e.setId(rs.getInt("etudiant_id"));
                e.setNom(rs.getString("nom"));
                e.setPrenom(rs.getString("prenom"));
                e.setTelehone(rs.getString("telephone"));
                e.setEmail(rs.getString("email"));
                e.setAdresse(rs.getString("adresse"));
                List_Etudiant.add(e);
//                System.out.println(rs.getInt("etudiant_id"));
            }
            etudiantTable.setItems(List_Etudiant);
            etudiant_id.setCellValueFactory(f -> f.getValue().idProperty());
            nom.setCellValueFactory(f -> f.getValue().nomProperty());
            prenom.setCellValueFactory(f -> f.getValue().prenomProperty());
            telephone.setCellValueFactory(f -> f.getValue().telehoneProperty());
            email.setCellValueFactory(f -> f.getValue().emailProperty());
            adresse.setCellValueFactory(f -> f.getValue().adresseProperty());
        }
        catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    @FXML
    void ADD_note(ActionEvent event) {
        Etudiant etudiant = etudiantTable.getSelectionModel().getSelectedItem();
        if (etudiant!= null) {
            try {
                FXMLLoader fxmlLoa = new FXMLLoader(getClass().getResource("AddNote.fxml"));
                Parent root = (Parent)fxmlLoa.load();
                AddNoteController controller = fxmlLoa.getController();
                controller.setEtudiant(etudiant);
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Ajouter note");
                stage.setFullScreen(false);
                stage.resizableProperty().setValue(false);
                Image img = new Image("student.png");
                stage.getIcons().add(img);
                stage.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @FXML
    void GO_back(ActionEvent event) throws IOException {
        main.getChildren().clear();
        main.getChildren().add(FXMLLoader.load(getClass().getResource("NoteHome.fxml")));
    }

    @FXML
    void refresh_table(MouseEvent event) {
        table();
    }
}
