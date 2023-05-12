package com.example.core;

import com.example.core.exceptions.UnscheduledException;
import com.example.core.utils.Pair;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.TreeSet;

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
     * planifier une tache automatiquement dans un planning
     * @param planning le planning dans lequel la tache sera planifiée
     * @param startDateTime la journée et le temps du début de planification
     * @return (Day, TreeSet<Creneau> (CreneauOccupe, CreneauLibre?))
     * @throws UnscheduledException si la tache ne peut pas etre planifiée dans le planning
     */
    @Override
    public Pair<Day, TreeSet<Creneau>> planifier(@NotNull Planning planning, LocalDateTime startDateTime) throws UnscheduledException {
        Pair<Day, TreeSet<Creneau>> infos = planning.planifier(this, startDateTime);
        setUnscheduled(false);
        return infos;
    }

    @Override
    public String toString() {
        return "TacheSimple{periodicity=" + periodicity + ", super=" + super.toString() + "}";
    }

    //endregion
}
