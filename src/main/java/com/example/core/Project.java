package com.example.core;

import com.example.core.exceptions.UnscheduledException;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Project implements IPlanifiable, Serializable {
    //region Attributes

    private String nom;
    private String description;
    private ArrayList<Tache> taches;

    //endregion

    //region Constructors

    public Project(String nom, String description) {
        this.nom = nom;
        this.description = description;
        this.taches = new ArrayList<>();
    }

    public Project(String nom, String description, ArrayList<Tache> taches) {
        this(nom, description);
        this.taches = taches;
    }

    //endregion

    //region Setter and Getters

    //endregion

    //region Methods

    @Override
    public LocalDateTime planifier(Planning planning, LocalDateTime startDateTime) throws UnscheduledException {
        //TODO: implémenter la methode planifier pour les projets
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Project project = (Project) o;
        return nom.equals(project.nom);
    }

    @Override
    public int hashCode() {
        return nom.hashCode();
    }

    @Override
    public String toString() {
        return "Project{" +
                "nom='" + nom + '\'' +
                ", description='" + description + '\'' +
                ", taches=" + taches +
                '}';
    }

    //endregion
}
