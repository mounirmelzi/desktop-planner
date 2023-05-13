package com.example.core;

import com.example.core.exceptions.UnscheduledException;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;

public class Project implements IPlanifiable, Serializable {
    //region Attributes

    private String nom;
    private String description;
    private LinkedHashSet<Tache> taches;

    //endregion

    //region Constructors

    public Project(String nom, String description) {
        this.nom = nom;
        this.description = description;
        this.taches = new LinkedHashSet<>();
    }

    public Project(String nom, String description, LinkedHashSet<Tache> taches) {
        this(nom, description);
        this.taches = taches;
    }

    //endregion

    //region Setter and Getters

    public LinkedHashSet<Tache> getTaches() {
        return taches;
    }

    //endregion

    //region Methods

    /**
     * planifier un projet automatiquement dans un planning
     * @param planning le planning dans lequel le projet sera planifiée
     * @param startDateTime la journée et le temps du début de planification
     * @return LocalDateTime
     * @throws UnscheduledException si le projet ne peut pas etre planifié dans le planning
     */
    @Override
    public LocalDateTime planifier(Planning planning, LocalDateTime startDateTime) throws UnscheduledException {
        LocalDateTime firstDateTime = null;

        for (Tache tache : getTaches()) {
            try {
                startDateTime = tache.planifier(planning, startDateTime);
                if (firstDateTime == null) firstDateTime = startDateTime;
            } catch (UnscheduledException ignored) {}
        }

        return firstDateTime;
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
