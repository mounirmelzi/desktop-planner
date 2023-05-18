package com.example.core;

import com.example.core.exceptions.InvalidDateTimeException;
import com.example.core.exceptions.UnscheduledException;
import com.example.core.utils.Pair;
import com.example.core.utils.Utils;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.TreeSet;

public class Planning implements Serializable {
    //region Attributes

    private LocalDate dateDebut;
    private LocalDate dateFin;
    private Calendrier calendrier;
    private HashMap<Badge, Integer> badges;

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

    public TreeSet<Day> getDays(@NotNull LocalDate dateDebut) {
        if (dateDebut.isBefore(this.dateDebut))
            dateDebut = this.dateDebut;

        return (TreeSet<Day>) calendrier.getDays().subSet(
                new Day(dateDebut), true,
                new Day(dateFin), true
        );
    }

    public TreeSet<Day> getDays(@NotNull LocalDate dateDebut, @NotNull LocalDate dateFin) {
        if (dateDebut.isBefore(this.dateDebut))
            dateDebut = this.dateDebut;

        if (dateFin.isAfter(this.dateFin))
            dateFin = this.dateFin;

        return (TreeSet<Day>) calendrier.getDays().subSet(
                new Day(dateDebut), true,
                new Day(dateFin), true
        );
    }

    public Day getDayByDate(LocalDate date) {
        if (date == null || date.isBefore(getDateDebut()) || date.isAfter(getDateFin()))
            return null;

        return calendrier.getDayByDate(date);
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    //endregion

    //region Methods

    /**
     * planifier une tache automatiquement dans un planning
     * @param tache la tache qui va etre planifier dans ce planning
     * @param startDateTime la journée et le temps du début de planification
     * @return LocalDateTime
     * @throws UnscheduledException si la tache ne peut pas etre planifiée dans aucune journée
     */
    public LocalDateTime planifier(@NotNull Tache tache, LocalDateTime startDateTime) throws UnscheduledException {
        if (startDateTime == null)
            startDateTime = LocalDateTime.now();

        if (dateFin.isBefore(startDateTime.toLocalDate()))
            throw new UnscheduledException();

        for (Day day : getDays(startDateTime.toLocalDate(), tache.getDeadline().toLocalDate())) {
            try {
                TreeSet<Creneau> creneaux = day.planifier(tache, startDateTime);
                return Utils.dateTimePairToLocalDateTime(new Pair<>(day, creneaux));
            } catch (UnscheduledException ignored) {}
        }

        throw new UnscheduledException();
    }

    /**
     * planifier une tache manuellement dans un planning
     * @param tache la tache qui va etre planifier dans ce planning
     * @param date la date de la journée où on va planifier la tache
     * @param time le temps du début de créneau libre dans lequel on va planifier la tache
     * @return LocalDateTime
     * @throws UnscheduledException si la tache ne peut pas etre planifiée
     */
    public LocalDateTime planifierManuellement(Tache tache, LocalDate date, LocalTime time) throws UnscheduledException {
        Day day = new Day(date);
        day = this.getDays().floor(day);

        if (day == null || !day.getDate().isEqual(date))
            throw new UnscheduledException();

        if (!tache.checkDeadline(day, null))
            throw new UnscheduledException();

        TreeSet<Creneau> creneaux = day.planifierManuellement(tache, time);
        return Utils.dateTimePairToLocalDateTime(new Pair<>(day, creneaux));
    }

    public boolean deleteTache(LocalDateTime planificationDateTime) {
        for (Day day : getDays()) {
            if (day.deleteTache(planificationDateTime))
                return true;
        }

        return false;
    }

    //endregion
}
