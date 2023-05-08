package com.example.core;

import java.time.Duration;
import java.time.LocalDateTime;

public class TacheSimple extends Tache {
    private int periodicity;

    public TacheSimple(String nom, Duration duree, Priority priority, LocalDateTime deadline) {
        super(nom, duree, priority, deadline);
        periodicity = 0;
    }

    public TacheSimple(String nom, Duration duree, Priority priority, LocalDateTime deadline, Category category) {
        super(nom, duree, priority, deadline, category);
        periodicity = 0;
    }

    public TacheSimple(String nom, Duration duree, Priority priority, LocalDateTime deadline, State state) {
        super(nom, duree, priority, deadline, state);
        periodicity = 0;
    }

    public TacheSimple(String nom, Duration duree, Priority priority, LocalDateTime deadline, Category category, State state) {
        super(nom, duree, priority, deadline, category, state);
        periodicity = 0;
    }

    public int getPeriodicity() {
        return periodicity;
    }

    public void setPeriodicity(int periodicity) {
        this.periodicity = periodicity;
    }

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
