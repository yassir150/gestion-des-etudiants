package com.projet.gestionetudiant;

import com.projet.gestionetudiant.models.Enseignants;
import com.projet.gestionetudiant.models.Etudiant;
import com.projet.gestionetudiant.models.Notes;
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




public class TableEnseignantsController implements Initializable {
    @FXML
    private Button button;

    public TableView<Enseignants> enseignantsTableView;

    @FXML
    private TableColumn<Enseignants, Number> enseignant_id;

    @FXML
    private TableColumn<Enseignants, String> nom;

    @FXML
    private TableColumn<Enseignants, String> prenom;

    @FXML
    private TableColumn<Enseignants, String> telephone;

    @FXML
    private TableColumn<Enseignants, String> email;

    @FXML
    private TableColumn<Enseignants, String> adresse;

    String query = null;
    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        enseignantsTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

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

    @FXML
    void ADD_enseignant(ActionEvent event) {
        try {
            FXMLLoader fxmlLoa = new FXMLLoader(getClass().getResource("AddEnseignant.fxml"));
            Parent root = (Parent) fxmlLoa.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Ajouter enseignant");
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

    public void table() {
        String sql = "SELECT  * FROM enseignants";
        try {

            ResultSet rs = conn.createStatement().executeQuery(sql);
            ObservableList<Enseignants> List_Enseignants = FXCollections.observableArrayList();
            while (rs.next()) {
                Enseignants e = new Enseignants();
                e.setId(rs.getInt("enseignant_id"));
                e.setNom(rs.getString("nom"));
                e.setPrenom(rs.getString("prenom"));
                e.setTelehone(rs.getString("telephone"));
                e.setEmail(rs.getString("email"));
                e.setAdresse(rs.getString("adresse"));
                List_Enseignants.add(e);
//                System.out.println(rs.getInt("etudiant_id"));
            }
            enseignantsTableView.setItems(List_Enseignants);
            enseignant_id.setCellValueFactory(f -> f.getValue().idProperty());
            nom.setCellValueFactory(f -> f.getValue().nomProperty());
            prenom.setCellValueFactory(f -> f.getValue().prenomProperty());
            telephone.setCellValueFactory(f -> f.getValue().telehoneProperty());
            email.setCellValueFactory(f -> f.getValue().emailProperty());
            adresse.setCellValueFactory(f -> f.getValue().adresseProperty());
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void edit_enseignant(ActionEvent event){
        Enseignants enseignant = enseignantsTableView.getSelectionModel().getSelectedItem();
        if (enseignant!= null) {
            try {
                FXMLLoader fxmlLoa = new FXMLLoader(getClass().getResource("EditEnseignants.fxml"));
                Parent root = (Parent)fxmlLoa.load();
                EditEnseignantsController controller = fxmlLoa.getController();
                controller.setEnseignant(enseignant);
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Modifier Enseignant");
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
    public void DeleteEnseignats(ActionEvent event){
            Enseignants e = enseignantsTableView.getSelectionModel().getSelectedItem();
            if (e != null) {
                try {
                    boolean existsInAbsences = checkIfExistsInTable("cours", e.getId());

                    if ( existsInAbsences) {
                        showAlert("La ligne est référencée par une clé étrangère dans la table(s):cours  Suppression impossible.","Suppression impossible");
                    } else {
                        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
                        confirmationAlert.setTitle("Confirmation");
                        confirmationAlert.setHeaderText(" Confirmation de suppression d'enseignant"+e.getNom()+" "+e.getPrenom());
                        confirmationAlert.setContentText("Êtes-vous sûr de vouloir supprimer cet enseignant ?");
                        Optional<ButtonType> result = confirmationAlert.showAndWait();
                        if (result.isPresent() && result.get() == ButtonType.OK) {
                            String deleteQuery = "DELETE FROM enseignants WHERE enseignant_id = ?";
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

    private boolean checkIfExistsInTable(String tableName, int studentId) throws SQLException {
            String query = "SELECT COUNT(*) FROM " + tableName + " WHERE enseignant_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, studentId);
            ResultSet resultSet = stmt.executeQuery();
            resultSet.next();
            int count = resultSet.getInt(1);
            return count > 0;
    }
    public void ExportExel(ActionEvent e) {
        try {

            exportTable(enseignantsTableView);

        } catch (IOException ex) {

            // Handle any exceptions that occur during the export process
            System.out.println("Couldn't export table data.");
            throw new RuntimeException(ex);

        }

    }
    private void exportTable(TableView<Enseignants> enseignantsTableView) throws IOException {

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));

        File file = fileChooser.showSaveDialog(enseignantsTableView.getScene().getWindow());

        if (file != null) {
            ExcelExporter.exportToExcel(enseignantsTableView, "FXUserData", file.getAbsolutePath());
        }
    }
}
