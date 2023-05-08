package com.example.core;

import java.time.LocalTime;

public class CreneauOccupe extends Creneau {
    //region Attributes

    private Tache tache;
    private boolean blocked;

    //endregion

    //region Constructors

    public CreneauOccupe(LocalTime heureDebut, LocalTime heureFin, Tache tache) {
        super(heureDebut, heureFin);
        this.tache = tache;
        this.blocked = false;
    }

    public CreneauOccupe(LocalTime heureDebut, LocalTime heureFin, Tache tache, boolean blocked) {
        this(heureDebut, heureFin, tache);
        this.blocked = blocked;
    }

    //endregion

    //region Setter and Getters

    //endregion

    //region Methods

    @Override
    public String toString() {
        return "CreneauOccupe{" +
                "tache=" + tache +
                ", blocked=" + blocked +
                ", heureDebut=" + getHeureDebut() +
                ", heureFin=" + getHeureFin() +
                '}';
    }

    //endregion
}
