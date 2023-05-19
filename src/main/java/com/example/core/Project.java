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
        if (!this.isUnscheduled())
            return null;

        this.deplanifier(planning);
        for (Tache tache : getTaches()) {
            if (!tache.isUnscheduled())
                continue;

            try {
                startDateTime = tache.planifier(planning, startDateTime);
            } catch (UnscheduledException e) {
                this.deplanifier(planning);
                throw new UnscheduledException();
            }
        }

        return null;
    }

    /**
     * vérifier si un projet est planifiée ou non
     * @return true si le projet n'est pas planifiée, false si non
     */
    @Override
    public boolean isUnscheduled() {
        for (Tache tache : getTaches()) {
            if (tache.isUnscheduled())
                return true;
        }

        return false;
    }

    /**
     * déplanifier tous les taches d'un projet
     * @param planning le planning où les taches du projet sont planifiées
     */
    @Override
    public void deplanifier(Planning planning) {
        for (Tache tache : getTaches())
            tache.deplanifier(planning);
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
