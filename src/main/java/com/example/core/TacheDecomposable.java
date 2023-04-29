package com.example.core;

import java.util.TreeSet;

public class TacheDecomposable extends Tache implements IDecomposable<Planning, TacheSimple> {
    private TreeSet<TacheSimple> children;

    @Override
    public TreeSet<TacheSimple> decomposer(Planning decomposer) {
        //TODO: implémenter la methode decomposer pour les taches décomposables
        return null;
    }

    @Override
    public String toString() {
        return "TacheDecomposable{" +
                "children=" + children +
                ", nom='" + nom + '\'' +
                ", duree=" + duree +
                ", priority=" + priority +
                ", deadline=" + deadline +
                ", category=" + category +
                ", state=" + state +
                '}';
    }
}
