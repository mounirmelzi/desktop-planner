package com.example.core;

import com.example.core.exceptions.CreneauLibreDurationException;
import com.example.core.exceptions.InvalidDateTimeException;
import com.example.core.exceptions.UnscheduledException;
import com.example.core.utils.Pair;
import com.example.core.utils.Utils;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.time.*;
import java.util.HashMap;
import java.util.TreeSet;

public class Planning implements Serializable {
    //region Attributes

    private LocalDate dateDebut;
    private LocalDate dateFin;
    private final Calendrier calendrier;
    private final HashMap<Badge, Integer> badges;

    private int tachesCompletedCounter;
    private int nbrTachesMinParJour;
    private LocalDate currentDate;

    //endregion

    //region Constructors

    public Planning(@NotNull LocalDate dateDebut, @NotNull LocalDate dateFin, @NotNull Calendrier calendrier) throws InvalidDateTimeException {
        if (dateDebut.isBefore(LocalDate.now()) || dateDebut.isAfter(dateFin)) {
            throw new InvalidDateTimeException();
        }

        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.calendrier = calendrier;

        this.currentDate = LocalDate.now();
        this.tachesCompletedCounter = 0;
        this.badges = new HashMap<>();
        for (Badge badge : Badge.values()) {
            badges.put(badge, 0);
        }
    }

    //endregion

    //region Setter and Getters

    public TreeSet<Day> getDays() {
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

    void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public Calendrier getCalendrier() {return calendrier ;}

    public HashMap<Badge, Integer> getBadges() {return badges;}

    void setNbrTachesMinParJour(int nbrTachesMinParJour) {
        this.nbrTachesMinParJour = nbrTachesMinParJour;
    }

    public int getNbTachesCompletees() {
        int cpt = 0;

        for (Day day: getDays()) {
            cpt += day.getCompletedTachesNumber() ;
        }

        return cpt ;
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

    void libererCreneauxOccupes() {
        for (Day day : getDays()) {
            day.libererCreneauxOccupes(this);
        }
    }

    /**
     * ajouter des créneaux libres dans toutes les journées d'un planning
     * @param creneauxLibres les creneaux libres à ajouter dans le planning
     */
    public void ajouterCreneauLibre(HashMap<DayOfWeek, Pair<LocalTime, LocalTime>> creneauxLibres) {
        for (LocalDate date = getDateDebut(); !date.isAfter(getDateFin()); date = date.plusDays(1)) {
            Day day = this.getDayByDate(date);
            if (day == null) {
                day = new Day(date);
                calendrier.addDay(day);
            }

            try {
                day.ajouterCreneauLibre(new CreneauLibre(
                        creneauxLibres.get(date.getDayOfWeek()).getFirst(),
                        creneauxLibres.get(date.getDayOfWeek()).getSecond()
                ));
            } catch (CreneauLibreDurationException | IllegalArgumentException ignored) {}
        }
    }

    public Day dayPlusRentable() {
        if (getDays().size() == 0)
            return null;

        Day dayPlusRentable = getDays().first();
        for (Day day : getDays()) {
            if (day.getCompletedTachesNumber() > dayPlusRentable.getCompletedTachesNumber())
                dayPlusRentable = day;
        }

        return dayPlusRentable;
    }

    public double rendementMoyen() {
        int totalCompletedTachesNumber = 0;
        int totalTachesNumber = 0;

        for (Day day : getDays()) {
            totalCompletedTachesNumber += day.getCompletedTachesNumber();
            totalTachesNumber += day.getTotalTachesNumber();
        }

        return (totalTachesNumber == 0) ? 1.0 : ((double) totalCompletedTachesNumber / totalTachesNumber);
    }

    public Category getMostCompletedCategory() {
        HashMap<Category, Integer> categoriesCounter = new HashMap<>();

        for (Day day : getDays()) {
            HashMap<Category, Integer> dayCategoriesCounter = day.countCompletedCategory();

            for (Category category : dayCategoriesCounter.keySet()) {
                if (categoriesCounter.containsKey(category)) {
                    categoriesCounter.put(category, categoriesCounter.get(category) + dayCategoriesCounter.get(category));
                } else {
                    categoriesCounter.put(category, dayCategoriesCounter.get(category));
                }
            }
        }

        Category maxCategory = null;
        int cpt = 0;

        for (Category category : categoriesCounter.keySet()) {
            if (maxCategory == null || categoriesCounter.get(category) > cpt) {
                cpt = categoriesCounter.get(category);
                maxCategory = category;
            }
        }

        return maxCategory;
    }

    void updateTacheCompletedCounter(int nbrTachesCompleted) {
        if (!currentDate.isEqual(LocalDate.now())) {
            currentDate = LocalDate.now();
            this.tachesCompletedCounter = 0;
        }

        this.tachesCompletedCounter += nbrTachesCompleted;
        if (this.tachesCompletedCounter < 0)
            this.tachesCompletedCounter = 0;

        // update badges
        if (tachesCompletedCounter >= nbrTachesMinParJour) {
            tachesCompletedCounter = 0;
            badges.put(Badge.GOOD, badges.get(Badge.GOOD) + 1);
            badges.put(Badge.VERY_GOOD, badges.get(Badge.GOOD) / 3);
            badges.put(Badge.EXCELLENT, badges.get(Badge.VERY_GOOD) / 3);
        }
    }

    //endregion
}
