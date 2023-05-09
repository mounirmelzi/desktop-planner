package com.example.core;

import com.example.core.exceptions.InvalidDateTimeException;
import com.example.core.exceptions.UnscheduledException;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.TreeSet;

public class Planning implements Serializable {
    //region Attributes

    private LocalDate dateDebut;
    private LocalDate dateFin;
    private Calendrier calendrier;

    //endregion

    //region Constructors

    public Planning(@NotNull LocalDate dateDebut, @NotNull LocalDate dateFin, @NotNull Calendrier calendrier) throws InvalidDateTimeException {
        if (dateDebut.isBefore(LocalDate.now()) || dateDebut.isAfter(dateFin)) {
            throw new InvalidDateTimeException();
        }

        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.calendrier = calendrier;
    }

    //endregion

    //region Setter and Getters

    public Period getPeriod() {
        return Period.between(dateDebut, dateFin);
    }

    public long getNumberOfDays() {
        return ChronoUnit.DAYS.between(dateDebut, dateFin);
    }

    public TreeSet<Day> getDays() {
        return (TreeSet<Day>) calendrier.getDays().subSet(
                new Day(dateDebut), true,
                new Day(dateFin), true
        );
    }

    //endregion

    //region Methods

    /**
     * planifier une tache automatiquement dans un planning
     * @param tache la tache qui va etre planifier dans cette journée
     * @return (Day, TreeSet<Creneau> (CreneauOccupe, CreneauLibre?))
     * @throws UnscheduledException si la tache ne peut pas etre planifiée dans aucune journée
     */
    public List<Object> planifier(@NotNull Tache tache) throws UnscheduledException {
        for (Day day : getDays()) {
            if (day.getDate().isBefore(LocalDate.now()))
                continue;

            if (!tache.checkDeadline(day, null))
                throw new UnscheduledException();

            try {
                TreeSet<Creneau> creneaux = day.planifier(tache);
                return List.of(day, creneaux);
            } catch (UnscheduledException ignored) {}
        }

        throw new UnscheduledException();
    }

    //endregion
}
