package com.projet.gestionetudiant;

import com.projet.gestionetudiant.models.Absence;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.image.Image;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Optional;
import java.util.ResourceBundle;

public class AbsenceTable implements Initializable {

    @FXML
    private TableView<Absence> absencetable;

    @FXML
    private TableColumn<Absence, String> date;

    @FXML
    private TableColumn<Absence, Number> etudiant_id;

    @FXML
    private TableColumn<Absence, String> nom_etudiant;

    @FXML
    private TableColumn<Absence, String> prenom_etudiant;

    @FXML
    private TableColumn<Absence, String> statu;

    @FXML
    private AnchorPane main_core;
    @FXML
    void Modifier_Absence(ActionEvent event) throws IOException {
        main_core.getChildren().clear();
        main_core.getChildren().add(FXMLLoader.load(getClass().getResource("AffectationAbsenceTable.fxml")));
    }

    String query = null;
    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;


    @Override
    public void initialize(URL url, ResourceBundle rb){
        absencetable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        getConnect();
        table();

    }

    public void getConnect() {
        try {
            conn = DriverManager.getConnection("jdbc:mysql://**put your database here**", "**put your database username here**", "**put your database password here**");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public void table(){
        String sql = "SELECT  * FROM absences INNER JOIN etudiant ON absences.etudiant_id = etudiant.etudiant_id";
        try{
            ResultSet rs = conn.createStatement().executeQuery(sql);
            ObservableList<Absence> List_Absence = FXCollections.observableArrayList();
            while (rs.next()){
                Absence abs = new Absence();
                abs.setAbsence_id(rs.getInt("absence_id"));
                abs.setDate(rs.getString("date"));
                abs.setEtudiant_id(rs.getInt("etudiant_id"));
                abs.setNom_etudiant(rs.getString("nom"));
                abs.setPrenom_etudiant(rs.getString("prenom"));
                abs.setStatut(rs.getString("statut"));
                List_Absence.add(abs);
            }
            absencetable.setItems(List_Absence);
            date.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
            etudiant_id.setCellValueFactory(cellData -> cellData.getValue().etudiant_idProperty());
            nom_etudiant.setCellValueFactory(cellData -> cellData.getValue().nom_etudiantProperty());
            prenom_etudiant.setCellValueFactory(cellData -> cellData.getValue().prenom_etudiantProperty());
            statu.setCellValueFactory(cellData -> cellData.getValue().statutProperty());
        }catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }

    }
    @FXML
    void Modifier_absence(ActionEvent event) {
        Absence abs = absencetable.getSelectionModel().getSelectedItem();
        if (abs!= null) {
            try {
                FXMLLoader fxmlLoa = new FXMLLoader(getClass().getResource("ModifierAbsence.fxml"));
                Parent root = (Parent)fxmlLoa.load();
                ModifierAbsenceController controller = fxmlLoa.getController();
                controller.setAbsence(abs);
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Modifier note");
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
    void refresh_table(MouseEvent event) {
        table();
    }

    @FXML
    void Delete_absence(ActionEvent event) {
        Absence abs = absencetable.getSelectionModel().getSelectedItem();
        if (abs!= null) {
            try {
                Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
                confirmationAlert.setTitle("Confirmation");
                confirmationAlert.setHeaderText(" Confirmation de suppression d'absence de "+abs.getNom_etudiant()+" "+abs.getPrenom_etudiant() +" a "+abs.getDate());
                confirmationAlert.setContentText("Êtes-vous sûr de vouloir supprimer cet absence ?");
                Optional<ButtonType> result = confirmationAlert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    String sql = "DELETE FROM absences WHERE absence_id =?";
                    ps = conn.prepareStatement(sql);
                    ps.setInt(1, abs.getAbsence_id());
                    ps.executeUpdate();
                    table();
                }
            } catch (SQLException ps) {
                System.out.println("Error: " + ps.getMessage());
            }
        }

    }

    @FXML
    void GO_back(ActionEvent event) throws IOException {
        main_core.getChildren().clear();
        main_core.getChildren().add(FXMLLoader.load(getClass().getResource("AbsenceHome.fxml")));
    }
    public void ExportExel(ActionEvent e) {
        try {

            exportTable(absencetable);

        } catch (IOException ex) {

            // Handle any exceptions that occur during the export process
            System.out.println("Couldn't export table data.");
            throw new RuntimeException(ex);

        }

    }
    private void exportTable(TableView<Absence> absencetable) throws IOException {

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));

        File file = fileChooser.showSaveDialog(absencetable.getScene().getWindow());

        if (file != null) {
            ExcelExporter.exportToExcel(absencetable, "FXUserData", file.getAbsolutePath());
        }
    }
}
