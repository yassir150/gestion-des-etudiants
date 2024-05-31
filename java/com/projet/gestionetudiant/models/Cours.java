package com.projet.gestionetudiant.models;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Cours {
    SimpleIntegerProperty cour_id;

    SimpleStringProperty nom;

    SimpleStringProperty description;
    SimpleStringProperty enseignant;


    public Cours() {
        this.cour_id = new SimpleIntegerProperty(this ,"cour_id");
        this.nom = new SimpleStringProperty(this ,"nom");
        this.description = new SimpleStringProperty(this ,"description");
        this.enseignant = new SimpleStringProperty(this ,"enseignant");
    }

    public Integer getCour_id() {
        return cour_id.get();
    }

    public SimpleIntegerProperty cour_idProperty() {
        return cour_id;
    }

    public void setCour_id(Integer cour_id) {
        this.cour_id.set(cour_id);
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

    public String getDescription() {
        return description.get();
    }

    public SimpleStringProperty descriptionProperty() {
        return description;
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public String getenseignant() {
        return enseignant.get();
    }

    public SimpleStringProperty enseignantProperty() {
        return enseignant;
    }

    public void setenseignant(String enseignant) {
        this.enseignant.set(enseignant);
    }




}
