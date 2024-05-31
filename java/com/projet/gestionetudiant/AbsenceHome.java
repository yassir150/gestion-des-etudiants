package com.projet.gestionetudiant;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class AbsenceHome {

    @FXML
    private AnchorPane main_core;
    @FXML
    void Ajouter_Absence(ActionEvent event) throws IOException {
        main_core.getChildren().clear();
        main_core.getChildren().add(FXMLLoader.load(getClass().getResource("AbsenceTable.fxml")));
    }

    @FXML
    void Modifier_Absence(ActionEvent event) throws IOException {
        main_core.getChildren().clear();
        main_core.getChildren().add(FXMLLoader.load(getClass().getResource("AffectationAbsenceTable.fxml")));
    }
}
