package com.example.core;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;

public class Periode implements Serializable {
    //region Attributes

    LocalDate dateDebut;
    LocalDate dateFin;

    //endregion

    //region Constructors

    //endregion

    //region Setter and Getters

    public Period getPeriod() {
        return Period.between(dateDebut, dateFin);
    }

    public long getNumberOfDays() {
        return ChronoUnit.DAYS.between(dateDebut, dateFin);
    }

    //endregion

    //region Methods

    @Override
    public String toString() {
        return "Periode{" +
                "dateDebut=" + dateDebut +
                ", dateFin=" + dateFin +
                '}';
    }

    //endregion
}
