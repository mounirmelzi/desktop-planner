package com.example.core;

import com.example.core.exceptions.UnscheduledException;

import java.time.Duration;
import java.time.LocalDateTime;

public class TacheSimple extends Tache {
    //region Attributes

    private int periodicity;

    //endregion

    //region Constructors

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

    //endregion

    //region Setter and Getters

    public int getPeriodicity() {
        return periodicity;
    }

    public void setPeriodicity(int periodicity) {
        this.periodicity = periodicity;
    }

    //endregion

    //region Methods

    /**
     * @param planning planning le planning dans le quel la tache simple sera planifiée
     * @throws UnscheduledException si la tache ne peut pas etre planifiée dans le planning
     */
    @Override
    public void planifier(Planning planning) throws UnscheduledException {
        planning.planifier(this);
        setUnscheduled(false);
    }

    @Override
    public String toString() {
        return "TacheSimple{periodicity=" + periodicity + ", super=" + super.toString() + "}";
    }

    //endregion
}
