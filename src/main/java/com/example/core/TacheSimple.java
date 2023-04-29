package com.example.core;

public class TacheSimple extends Tache {
    private int periodicity;

    @Override
    public String toString() {
        return "TacheSimple{" +
                "periodicity=" + periodicity +
                ", nom='" + nom + '\'' +
                ", duree=" + duree +
                ", priority=" + priority +
                ", deadline=" + deadline +
                ", category=" + category +
                ", state=" + state +
                '}';
    }
}
