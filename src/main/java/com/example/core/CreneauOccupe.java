package com.example.core;

import java.time.LocalTime;

public class CreneauOccupe extends Creneau {
    private Tache tache;
    private Boolean blocked;

    public CreneauOccupe(LocalTime heureDebut, LocalTime heureFin, Tache tache) {
        super(heureDebut, heureFin);
        this.tache = tache;
        this.blocked = false;
    }

    public CreneauOccupe(LocalTime heureDebut, LocalTime heureFin, Tache tache, Boolean blocked) {
        this(heureDebut, heureFin, tache);
        this.blocked = blocked;
    }

    @Override
    public String toString() {
        return "CreneauOccupe{" +
                "tache=" + tache +
                ", blocked=" + blocked +
                ", heureDebut=" + heureDebut +
                ", heureFin=" + heureFin +
                '}';
    }
}
