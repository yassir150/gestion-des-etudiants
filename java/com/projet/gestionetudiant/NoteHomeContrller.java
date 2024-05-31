package com.projet.gestionetudiant;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class NoteHomeContrller {
    @FXML
    private AnchorPane main;

    @FXML
    void Ajouter_note(ActionEvent event) throws IOException {
        main.getChildren().clear();
        main.getChildren().add(FXMLLoader.load(getClass().getResource("AffectationNoteTable.fxml")));
    }

    @FXML
    void Modifier_note(ActionEvent event) throws IOException {
        main.getChildren().clear();
        main.getChildren().add(FXMLLoader.load(getClass().getResource("TableNotes.fxml")));
    }
}
