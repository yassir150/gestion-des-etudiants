package com.projet.gestionetudiant;

import com.projet.gestionetudiant.models.Cours;
import com.projet.gestionetudiant.models.Enseignants;
import com.projet.gestionetudiant.models.Etudiant;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Optional;
import java.util.ResourceBundle;

public class TableCoursController implements Initializable {
    @FXML
    private TableView<Cours> CoursTable;

    @FXML
    private TableColumn<Cours, String> nom_cours;

    @FXML
    private TableColumn<Cours, Number> cours_id;

    @FXML
    private TableColumn<Cours, String> description_cours;

    @FXML
    private TableColumn<Cours, String> enseignant_cours;

    String query = null;
    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    @Override
    public void initialize(URL url, ResourceBundle rb){
        CoursTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

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

    public String get_ens(Integer enseignant) {
        try {
            PreparedStatement get_ens = conn.prepareStatement("SELECT * FROM enseignants where enseignant_id=?");
            get_ens.setInt(1, enseignant);
            ResultSet rs = get_ens.executeQuery();
            if (rs.next()) {
                return rs.getString("nom");
            } else {
                System.out.println("No enseignant found with name: " + enseignant);
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    public void table(){
        String sql  = "SELECT  * FROM cours";
        try {
            ResultSet rs = conn.createStatement().executeQuery(sql);
            ObservableList<Cours> List_Cours = FXCollections.observableArrayList();
            while (rs.next()) {
                Cours cr = new Cours();
                cr.setCour_id(rs.getInt("cour_id"));
                cr.setNom(rs.getString("nom"));
                cr.setDescription(rs.getString("description"));
                String ense = get_ens(rs.getInt("enseignant_id"));
                cr.setenseignant(ense);
                List_Cours.add(cr);
            }
            CoursTable.setItems(List_Cours);
            nom_cours.setCellValueFactory(cellData -> cellData.getValue().nomProperty());
            cours_id.setCellValueFactory(cellData -> cellData.getValue().cour_idProperty());
            description_cours.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());
            enseignant_cours.setCellValueFactory(cellData -> cellData.getValue().enseignantProperty());
        }
        catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }



    @FXML
    void ADD_cours(ActionEvent event) {
        try {
            FXMLLoader fxmlLoa = new FXMLLoader(getClass().getResource("AddCours.fxml"));
            Parent root = (Parent)fxmlLoa.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Ajouter cour");
            stage.setFullScreen(false);
            stage.resizableProperty().setValue(false);
            Image img = new Image("student.png");
            stage.getIcons().add(img);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void DeleteCours(ActionEvent event) {
        Cours e = CoursTable.getSelectionModel().getSelectedItem();
        if (e != null) {
            try {
                boolean existsInNotes = checkIfExistsInTable("notes", e.getCour_id());

                if (existsInNotes) {
                    showAlert("La ligne est référencée par une clé étrangère dans la table(s): Note, Suppression impossible.","Suppression impossible");
                } else {
                    Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
                    confirmationAlert.setTitle("Confirmation");
                    confirmationAlert.setHeaderText(" Confirmation de suppression de cour "+e.getNom()+" qui est occupe par Mr"+e.getenseignant());
                    confirmationAlert.setContentText("Êtes-vous sûr de vouloir supprimer cet cour ?");
                    Optional<ButtonType> result = confirmationAlert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        String deleteQuery = "DELETE FROM cours WHERE cour_id = ?";
                        PreparedStatement deleteStmt = conn.prepareStatement(deleteQuery);
                        deleteStmt.setInt(1, e.getCour_id()); // Setting the ID of the selected student
                        deleteStmt.executeUpdate();
                        table(); // Actualiser la table
                    }
                }
            } catch (SQLException ex) {
                System.out.println("Erreur: " + ex.getMessage());
                showAlert("Erreur: " + ex.getMessage()+" ","Erreur");
            }
        }
    }

    private boolean checkIfExistsInTable(String tableName, int studentId) throws SQLException {
        String query = "SELECT COUNT(*) FROM " + tableName + " WHERE cours_id = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, studentId);
        ResultSet resultSet = stmt.executeQuery();
        resultSet.next();
        int count = resultSet.getInt(1);
        return count > 0;
    }
    private void showAlert(String message,String title) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(20), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                alert.close();
            }
        }));
        timeline.play();
    }



@FXML
    void EditCours(ActionEvent event) {
        Cours cours = CoursTable.getSelectionModel().getSelectedItem();
        if (cours!= null) {
            try {
                FXMLLoader fxmlLoa = new FXMLLoader(getClass().getResource("EditCours.fxml"));
                Parent root = (Parent)fxmlLoa.load();
                EditCoursController controller = fxmlLoa.getController();
                controller.setCours(cours);
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Modifier cour");
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
    void refresh_cours(MouseEvent event) {
        table();
    }
    public void ExportExel(ActionEvent e) {
        try {

            exportTable(CoursTable);

        } catch (IOException ex) {

            // Handle any exceptions that occur during the export process
            System.out.println("Couldn't export table data.");
            throw new RuntimeException(ex);

        }

    }
    private void exportTable(TableView<Cours> CoursTable) throws IOException {

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));

        File file = fileChooser.showSaveDialog(CoursTable.getScene().getWindow());

        if (file != null) {
            ExcelExporter.exportToExcel(CoursTable, "FXUserData", file.getAbsolutePath());
        }
    }
}
