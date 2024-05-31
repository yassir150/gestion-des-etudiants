package com.projet.gestionetudiant.models;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Etudiant {

    SimpleIntegerProperty id;

    SimpleStringProperty nom;

    SimpleStringProperty prenom;

    SimpleStringProperty telehone;

    SimpleStringProperty email;
    SimpleStringProperty adresse;
    public Etudiant() {
        this.id = new SimpleIntegerProperty(this ,"id");
        this.nom = new SimpleStringProperty(this ,"nom");
        this.prenom = new SimpleStringProperty(this ,"prenom");
        this.telehone = new SimpleStringProperty(this,"telehone");
        this.email = new SimpleStringProperty(this,"email");
        this.adresse = new SimpleStringProperty(this,"adresse");
    }

    public Integer getId() {
        return id.get();
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public void setId(Integer id) {
        this.id.set(id);
    }

    public String getNom() {
        return nom.get();
    }

    public SimpleStringProperty nomProperty() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom.set(nom);
    }

    public String getPrenom() {
        return prenom.get();
    }

    public SimpleStringProperty prenomProperty() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom.set(prenom);
    }

    public String getTelehone() {
        return telehone.get();
    }

    public SimpleStringProperty telehoneProperty() {
        return telehone;
    }

    public void setTelehone(String telehone) {
        this.telehone.set(telehone);
    }

    public String getEmail() {
        return email.get();
    }

    public SimpleStringProperty emailProperty() {
        return email;
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public String getAdresse() {
        return adresse.get();
    }

    public SimpleStringProperty adresseProperty() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse.set(adresse);
    }


}