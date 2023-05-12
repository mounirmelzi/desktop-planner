package com.example.core;

import com.example.core.exceptions.DecompositionImpossibleException;
import com.example.core.exceptions.UnscheduledException;
import com.example.core.utils.Pair;

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

    @Override
    public Pair<Day, TreeSet<Creneau>> planifier(Planning planning, LocalDateTime startDateTime) throws UnscheduledException {
        //TODO: implémenter la methode planifier pour les taches décomposables
        return null;
    }

    @Override
    public TreeSet<TacheSimple> decomposer(Planning decomposer) throws DecompositionImpossibleException {
        //TODO: implémenter la methode decomposer pour les taches décomposables
        return null;
    }

    @Override
    public String toString() {
        return "TacheDecomposable{children=" + children + ", super=" + super.toString() + "}";
    }

    //endregion
}
