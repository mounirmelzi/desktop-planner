package com.example.core;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Period;

public class Periode implements Serializable {
    LocalDate dateDebut;
    LocalDate dateFin;

    public Period getPeriod() {
        return Period.between(dateDebut, dateFin);
    }

    @Override
    public String toString() {
        return "Periode{" +
                "dateDebut=" + dateDebut +
                ", dateFin=" + dateFin +
                '}';
    }
}
