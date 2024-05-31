package com.projet.gestionetudiant.models;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Notes {
    SimpleIntegerProperty note_id;
    SimpleIntegerProperty etudiant_id;
    SimpleStringProperty nom_etudiant;
    SimpleStringProperty prenom_etudiant;
    SimpleStringProperty cours;
    SimpleIntegerProperty note;
    public Notes() {
        this.note_id = new SimpleIntegerProperty(this ,"note_id");
        this.etudiant_id = new SimpleIntegerProperty(this ,"etudiant_id");
        this.nom_etudiant = new SimpleStringProperty(this ,"nom_etudiant");
        this.prenom_etudiant = new SimpleStringProperty(this ,"prenom_etudiant");
        this.cours = new SimpleStringProperty(this ,"cours");
        this.note = new SimpleIntegerProperty(this ,"note");
    }

    public int getNote_id() {
        return note_id.get();
    }

    public SimpleIntegerProperty note_idProperty() {
        return note_id;
    }

    public void setNote_id(int note_id) {
        this.note_id.set(note_id);
    }

    public Integer getEtudiant_id() {
        return etudiant_id.get();
    }

    public SimpleIntegerProperty etudiant_idProperty() {
        return etudiant_id;
    }

    public void setEtudiant_id(Integer etudiant_id) {
        this.etudiant_id.set(etudiant_id);
    }

    public String getNom_etudiant() {
        return nom_etudiant.get();
    }

    public SimpleStringProperty nom_etudiantProperty() {
        return nom_etudiant;
    }

    public void setNom_etudiant(String nom_etudiant) {
        this.nom_etudiant.set(nom_etudiant);
    }

    public String getPrenom_etudiant() {
        return prenom_etudiant.get();
    }

    public SimpleStringProperty prenom_etudiantProperty() {
        return prenom_etudiant;
    }

    public void setPrenom_etudiant(String prenom_etudiant) {
        this.prenom_etudiant.set(prenom_etudiant);
    }

    public String getCours() {
        return cours.get();
    }

    public SimpleStringProperty coursProperty() {
        return cours;
    }

    public void setCours(String cours) {
        this.cours.set(cours);
    }

    public Integer getNote() {
        return note.get();
    }

    public SimpleIntegerProperty noteProperty() {
        return note;
    }

    public void setNote(Integer note) {
        this.note.set(note);
    }
    

}
