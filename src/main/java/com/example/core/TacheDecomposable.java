package com.example.core;

import com.example.core.exceptions.DecompositionImpossibleException;
import com.example.core.exceptions.UnscheduledException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.TreeSet;

public class TacheDecomposable extends Tache implements IDecomposable<Planning, TacheSimple> {
    //region Attributes

    private TreeSet<TacheSimple> children;

    //endregion

    //region Constructors

    public TacheDecomposable(String nom, Duration duree, Priority priority, LocalDateTime deadline) {
        super(nom, duree, priority, deadline);
        children = new TreeSet<>();
    }

    public TacheDecomposable(String nom, Duration duree, Priority priority, LocalDateTime deadline, Category category) {
        super(nom, duree, priority, deadline, category);
        children = new TreeSet<>();
    }

    public TacheDecomposable(String nom, Duration duree, Priority priority, LocalDateTime deadline, State state) {
        super(nom, duree, priority, deadline, state);
        children = new TreeSet<>();
    }

    public TacheDecomposable(String nom, Duration duree, Priority priority, LocalDateTime deadline, Category category, State state) {
        super(nom, duree, priority, deadline, category, state);
        children = new TreeSet<>();
    }

    //endregion

    //region Setter and Getters

    public TreeSet<TacheSimple> getChildren() {
        return children;
    }

    public void setChildren(TreeSet<TacheSimple> children) {
        this.children = children;
    }

    //endregion

    //region Methods

    public boolean hasChildren() {
        return !children.isEmpty();
    }

    /**
     * @param planning planning le planning dans le quel la tache décomposable sera planifiée
     * @throws UnscheduledException si la tache ne peut pas etre planifiée dans le planning
     */
    @Override
    public void planifier(Planning planning) throws UnscheduledException {
        if (hasChildren()) {
            // TODO: implémenter la planification d'une tache décomposable
        } else {
            planning.planifier(this);
        }
    }

    @Override
    public TreeSet<TacheSimple> decomposer(Planning decomposer) throws DecompositionImpossibleException {
        //TODO: implémenter la methode decomposer pour les taches décomposables
        return null;
    }

    @Override
    public String toString() {
        return "TacheDecomposable{" +
                "children=" + children +
                ", nom='" + getNom() + '\'' +
                ", duree=" + getDuree() +
                ", priority=" + getPriority() +
                ", deadline=" + getDeadline() +
                ", category=" + getCategory() +
                ", state=" + getState() +
                '}';
    }

    //endregion
}
