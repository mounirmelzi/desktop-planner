package com.example.core;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Month;
import java.util.TreeSet;

public class Calendrier implements Serializable {
    //region Attributes

    private TreeSet<Day> days;

    //endregion

    //region Constructors

    public Calendrier() {
        this.days = new TreeSet<>();
    }

    public Calendrier(TreeSet<Day> days) {
        this.days = days;
    }

    //endregion

    //region Setter and Getters

    public TreeSet<Day> getDays() {
        return days;
    }

    //endregion

    //region Methods

    /**
     * extraire tous les jours d'un mois donné
     * @param year l'annee
     * @param month le mois
     * @return TreeSet<Day>
     */
    public TreeSet<Day> getDaysOfMonth(int year, Month month) {
        Day start = new Day(LocalDate.of(year, month, 1));
        Day end = new Day(LocalDate.of(year, month, month.maxLength()));

        return (TreeSet<Day>) getDays().subSet(start, true, end, true);
    }

    /**
     * rechercher une journée par sa date
     * @param date la date de la journée
     * @return Day
     */
    public Day getDayByDate(LocalDate date) {
        Day day = new Day(date);
        day = getDays().floor(day);
        return ((day != null) && (date.isEqual(day.getDate()))) ? day : null;
    }

    /**
     * ajouter une journée dans le calendrier
     * @param day la journée à ajouter
     * @return true si la journée est ajoutée, false si elle existe déjà
     */
    public boolean addDay(Day day) {
        return days.add(day);
    }

    //endregion
}
