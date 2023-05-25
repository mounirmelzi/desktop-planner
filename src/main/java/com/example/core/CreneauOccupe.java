package com.example.core;

import java.time.LocalTime;

public class CreneauOccupe extends Creneau {
    //region Attributes

    private final Tache tache;
    private boolean blocked;

    //endregion

    //region Constructors

    public CreneauOccupe(LocalTime heureDebut, LocalTime heureFin, Tache tache) {
        super(heureDebut, heureFin);
        this.tache = tache;
        this.blocked = false;
    }

    //endregion

    //region Setter and Getters

    public Tache getTache() {
        return tache;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    //endregion

    //region Methods

    //endregion
}
