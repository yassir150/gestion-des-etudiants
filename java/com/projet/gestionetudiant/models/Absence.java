package com.projet.gestionetudiant.models;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Absence {
    SimpleIntegerProperty absence_id;

    SimpleStringProperty date;

    SimpleIntegerProperty etudiant_id;

    SimpleStringProperty nom_etudiant;

    SimpleStringProperty prenom_etudiant;

    SimpleStringProperty statut;

    public Absence() {
        this.absence_id = new SimpleIntegerProperty(this ,"absence_id");
        this.date = new SimpleStringProperty(this ,"date");
        this.nom_etudiant = new SimpleStringProperty(this ,"nom_etudiant");
        this.prenom_etudiant = new SimpleStringProperty(this ,"prenom_etudiant");
        this.etudiant_id = new SimpleIntegerProperty(this ,"etudiant_id");
        this.statut = new SimpleStringProperty(this ,"statut");
    }


    public Integer getAbsence_id() {
        return absence_id.get();
    }

    public SimpleIntegerProperty absence_idProperty() {
        return absence_id;
    }

    public void setAbsence_id(Integer absence_id) {
        this.absence_id.set(absence_id);
    }

    public String getDate() {
        return date.get();
    }

    public SimpleStringProperty dateProperty() {
        return date;
    }

    public void setDate(String date) {
        this.date.set(date);
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

    public String getStatut() {
        return statut.get();
    }

    public SimpleStringProperty statutProperty() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut.set(statut);
    }



}
