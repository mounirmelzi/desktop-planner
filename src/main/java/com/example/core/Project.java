package com.example.core;

import com.example.core.exceptions.UnscheduledException;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
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
    
    public String getNom() {
        return nom;
    }

    public String getDescription() {
        return description;
    }

    public LinkedHashSet<Tache> getTaches() {
        return taches;
    }

    @Override
    public State getState() {
        if (!hasTaches())
            return State.IN_PROGRESS;

        HashMap<State, Integer> stateCounter = new HashMap<>();
        for (State state : State.values()) {
            stateCounter.put(state, 0);
        }

        for (Tache tache : getTaches()) {
            State subState = tache.getState();
            stateCounter.put(subState, stateCounter.get(subState) + 1);
        }

        if (stateCounter.get(State.CANCELLED) > 0)
            return State.CANCELLED;

        if (stateCounter.get(State.NOT_REALIZED) > 0)
            return State.NOT_REALIZED;

        if (stateCounter.get(State.DELAYED) > 0)
            return State.DELAYED;

        if (stateCounter.get(State.COMPLETED) == getTaches().size())
            return State.COMPLETED;

        return State.IN_PROGRESS;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setDescription(String description) {
        this.description = description;
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
     * vérifier si un projet a des taches ou non
     * @return true si le projet contient des taches, false si non
     */
    public boolean hasTaches() {
        return !getTaches().isEmpty();
    }

    public boolean hasTache(Tache tache) {
        for (Tache t: getTaches()) {
            if (t.compareTo(tache)==0) {return true;}
        }
        return false;
    }

    /**
     * ajouter une tache dans l'ensemble des taches du projet
     * @param tache la tache à ajouter
     * @return true si la tache est ajoutée, false si elle existe déjà
     */
    public boolean addTache(Tache tache) {
        return this.taches.add(tache);
    }

    /**
     * supprimer une tache de l'ensemble des taches du projet
     * @param tache la tache à supprimer
     * @return true si la tache est supprimer, false si non
     */
    public boolean deleteTache(@NotNull Tache tache) {
        if (tache.isUnscheduled()) {
            this.taches.remove(tache);
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

    /**
     * calculer le rendement d'un projet
     * @return double le rendement d'un projet
     */
    public double getRendement() {
        int completedTachesCounter = 0;
        for (Tache tache : getTaches()) {
            if (tache.getState() == State.COMPLETED)
                completedTachesCounter++;
        }

        if (getTaches().size() == 0)
            return 1.0;

        return ((double)completedTachesCounter / getTaches().size());
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
