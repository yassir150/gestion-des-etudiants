module com.projet.gestionetudiant {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.sql;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;

    opens com.projet.gestionetudiant to javafx.fxml;
    exports com.projet.gestionetudiant;
}