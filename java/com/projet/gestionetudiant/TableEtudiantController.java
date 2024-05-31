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




public class TableEtudiantController implements Initializable {
    @FXML
    private Button button;

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

    String query = null;
    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;


    @Override
    public void initialize(URL url, ResourceBundle rb){
        etudiantTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
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

    @FXML
    void ADD_etudiant(ActionEvent event)  {
        try {
        FXMLLoader fxmlLoa = new FXMLLoader(getClass().getResource("AddEtudiant.fxml"));
        Parent root = (Parent)fxmlLoa.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Ajouter etudiant");
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
    void refresh_eutdiant(MouseEvent event) {
        table();
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

    public void EditEtudiant(ActionEvent event){
                    Etudiant etudiant = etudiantTable.getSelectionModel().getSelectedItem();
                    if (etudiant!= null) {
                        try {
                        FXMLLoader fxmlLoa = new FXMLLoader(getClass().getResource("EditEtudiant.fxml"));
                        Parent root = (Parent)fxmlLoa.load();
                        EditEtudiantController controller = fxmlLoa.getController();
                        controller.setEtudiant(etudiant);
                            Stage stage = new Stage();
                            stage.setScene(new Scene(root));
                            stage.setTitle("Modifier etudiant");
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
    public void DeleteEtudiant(ActionEvent event) {
        Etudiant e = etudiantTable.getSelectionModel().getSelectedItem();
        if (e != null) {
            try {
                boolean existsInNotes = checkIfExistsInTable("notes", e.getId());
                boolean existsInAbsences = checkIfExistsInTable("absences", e.getId());

                if (existsInNotes || existsInAbsences) {
                    String tables = "";
                    if (existsInNotes) {
                        tables += "notes ";
                    }
                    if (existsInAbsences) {
                        tables += "absences";
                    }
                    showAlert("La ligne est référencée par une clé étrangère dans la table(s): " + tables + ". Suppression impossible.","Suppression impossible");
                } else {
                    Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
                    confirmationAlert.setTitle("Confirmation");
                    confirmationAlert.setHeaderText(" Confirmation de suppression d'etudiant"+e.getNom()+" "+e.getPrenom());
                    confirmationAlert.setContentText("Êtes-vous sûr de vouloir supprimer cet étudiant ?");
                    Optional<ButtonType> result = confirmationAlert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                    String deleteQuery = "DELETE FROM etudiant WHERE etudiant_id = ?";
                    PreparedStatement deleteStmt = conn.prepareStatement(deleteQuery);
                    deleteStmt.setInt(1, e.getId()); // Setting the ID of the selected student
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
        String query = "SELECT COUNT(*) FROM " + tableName + " WHERE etudiant_id = ?";
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
    public void ExportExel(ActionEvent e) {
        try {

            exportTable(etudiantTable);

        } catch (IOException ex) {

            // Handle any exceptions that occur during the export process
            System.out.println("Couldn't export table data.");
            throw new RuntimeException(ex);

        }

    }
    private void exportTable(TableView<Etudiant> etudiantTable) throws IOException {

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));

        File file = fileChooser.showSaveDialog(etudiantTable.getScene().getWindow());

        if (file != null) {
            ExcelExporter.exportToExcel(etudiantTable, "FXUserData", file.getAbsolutePath());
        }
    }
}
