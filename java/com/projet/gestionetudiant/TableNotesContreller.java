package com.projet.gestionetudiant;

import com.projet.gestionetudiant.models.Etudiant;
import com.projet.gestionetudiant.models.Notes;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Optional;
import java.util.ResourceBundle;

public class TableNotesContreller implements Initializable {
    @FXML
    private TableView<Notes> notetable;

    @FXML
    private TableColumn<Notes, Number> etudiant_id;

    @FXML
    private TableColumn<Notes, String> nom_etudiant;

    @FXML
    private TableColumn<Notes, String> prenom_etudiant;

    @FXML
    private TableColumn<Notes, String> cours;

    @FXML
    private TableColumn<Notes, Number> noteid;

    @FXML
    private TableColumn<Notes, Number> note;


    @FXML
    private AnchorPane main;

    String query = null;
    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    @Override
    public void initialize(URL url, ResourceBundle rb){
        notetable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
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
        String sql = "SELECT  * FROM notes INNER JOIN etudiant ON notes.etudiant_id = etudiant.etudiant_id INNER JOIN cours ON notes.cours_id=cours.cour_id";
        try{
            ResultSet rs = conn.createStatement().executeQuery(sql);
            ObservableList<Notes> List_notes = FXCollections.observableArrayList();
            while (rs.next()){
                Notes note = new Notes();
                note.setEtudiant_id(rs.getInt("etudiant.etudiant_id"));
                note.setNom_etudiant(rs.getString("etudiant.nom"));
                note.setPrenom_etudiant(rs.getString("etudiant.prenom"));
                note.setCours(rs.getString("cours.nom"));
                note.setNote(rs.getInt("notes.note"));
                note.setNote_id(rs.getInt("notes.note_id"));
                List_notes.add(note);
            }
            notetable.setItems(List_notes);
            etudiant_id.setCellValueFactory(cellData -> cellData.getValue().etudiant_idProperty());
            nom_etudiant.setCellValueFactory(cellData -> cellData.getValue().nom_etudiantProperty());
            prenom_etudiant.setCellValueFactory(cellData -> cellData.getValue().prenom_etudiantProperty());
            cours.setCellValueFactory(cellData -> cellData.getValue().coursProperty());
            noteid.setCellValueFactory(cellData -> cellData.getValue().note_idProperty());
            note.setCellValueFactory(cellData -> cellData.getValue().noteProperty());
            rs.close();
        }catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

    }
    @FXML
    void Modifier_Note(ActionEvent event) throws IOException {
        main.getChildren().clear();
        main.getChildren().add(FXMLLoader.load(getClass().getResource("AffectationNoteTable.fxml")));
    }
    @FXML
    void Modifier_note(ActionEvent event) {
        Notes note = notetable.getSelectionModel().getSelectedItem();
        if (note != null) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("EditNote.fxml"));
                Parent root = (Parent) fxmlLoader.load();
                EditNoteController  controller = fxmlLoader.getController();
                controller.setNote(note);

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Modifier note");
                stage.setFullScreen(false);
                stage.resizableProperty().setValue(false);
                Image img = new Image("student.png");
                stage.getIcons().add(img);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
// catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        }
    }

    @FXML
    void refresh_table(MouseEvent event) {
        table();
    }

    @FXML
    void Delete_note(ActionEvent event) {
        Notes note = notetable.getSelectionModel().getSelectedItem();
        if (note != null) {
            try {
                Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
                confirmationAlert.setTitle("Confirmation");
                confirmationAlert.setHeaderText(" Confirmation de suppression");
                confirmationAlert.setContentText("Êtes-vous sûr de vouloir supprimer cet enformation ?");
                Optional<ButtonType> result = confirmationAlert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    String sql = "DELETE FROM notes WHERE note_id =?";
                    ps = conn.prepareStatement(sql);
                    ps.setInt(1, note.getNote_id());
                    ps.executeUpdate();
                    table();
                }
            } catch (SQLException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    @FXML
    void GO_back(ActionEvent event) throws IOException {
        main.getChildren().clear();
        main.getChildren().add(FXMLLoader.load(getClass().getResource("NoteHome.fxml")));
    }
    public void ExportExel(ActionEvent e) {
        try {

            exportTable(notetable);

        } catch (IOException ex) {

            // Handle any exceptions that occur during the export process
            System.out.println("Couldn't export table data.");
            throw new RuntimeException(ex);

        }

    }
    private void exportTable(TableView<Notes> notetable) throws IOException {

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));

        File file = fileChooser.showSaveDialog(notetable.getScene().getWindow());

        if (file != null) {
            ExcelExporter.exportToExcel(notetable, "FXUserData", file.getAbsolutePath());
        }
    }
}
