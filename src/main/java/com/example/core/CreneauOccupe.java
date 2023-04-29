package com.example.core;

public class CreneauOccupe extends Creneau {
    private Tache tache;
    private Boolean blocked;

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
