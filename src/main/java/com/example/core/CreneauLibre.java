package com.example.core;

import java.time.LocalTime;
import java.util.TreeSet;

public class CreneauLibre extends Creneau implements IDecomposable<Tache, Creneau> {
    public CreneauLibre(LocalTime heureDebut, LocalTime heureFin) {
        super(heureDebut, heureFin);
    }

    @Override
    public TreeSet<Creneau> decomposer(Tache decomposer) {
        //TODO: implémenter la methode decomposer pour les créneau libre
        return null;
    }
}
