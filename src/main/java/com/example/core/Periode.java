package com.example.core;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;

public class Periode implements Serializable {
    LocalDate dateDebut;
    LocalDate dateFin;

    public Period getPeriod() {
        return Period.between(dateDebut, dateFin);
    }
    public long getNumberOfDays() {
        return ChronoUnit.DAYS.between(dateDebut, dateFin);
    }

    @Override
    public String toString() {
        return "Periode{" +
                "dateDebut=" + dateDebut +
                ", dateFin=" + dateFin +
                '}';
    }
}
