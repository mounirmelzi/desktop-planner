package com.example.core;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class Day implements Comparable<Day>, Serializable {
    private LocalDate date;
    private TreeSet<Creneau> creneaux;

    public Day(LocalDate date) {
        this.date = date;
        this.creneaux = new TreeSet<>();
    }

    public Day(LocalDate date, TreeSet<Creneau> creneaux) {
        this(date);
        this.creneaux = creneaux;
    }

    public TreeSet<CreneauLibre> getCreneauxLibres() {
        return creneaux.stream()
                .filter(creneau -> creneau instanceof CreneauLibre)
                .map(creneau -> (CreneauLibre) creneau)
                .collect(Collectors.toCollection(TreeSet::new));
    }

    public TreeSet<CreneauOccupe> getCreneauxOccupes() {
        return creneaux.stream()
                .filter(creneau -> creneau instanceof CreneauOccupe)
                .map(creneau -> (CreneauOccupe) creneau)
                .collect(Collectors.toCollection(TreeSet::new));
    }

    public TreeSet<Creneau> getCreneaux() {
        return creneaux;
    }

    /**
     * ajouter un creneau libre dans les creneau de la journée
     *
     * @param creneauLibre le creneau libre à ajouter
     * @return Boolean: True si le creneau est ajouté avec succès, False si non
     */
    public Boolean ajouterCreneauLibre(CreneauLibre creneauLibre) {
        Boolean inserable = Boolean.TRUE ;
        Iterator<Creneau> it = creneaux.iterator();
        while ((it.hasNext())) {
            Creneau c = it.next() ;
            if (!((c.avant(creneauLibre)== 1)||(creneauLibre.avant(c)== 1))) {
                inserable = Boolean.FALSE ;
                break ;
            }
        }
        if (inserable == Boolean.TRUE) { creneaux.add(creneauLibre);}
        else {System.out.println("nonInserable"); }
        return inserable;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Day day = (Day) o;
        return date.equals(day.date);
    }

    @Override
    public int hashCode() {
        return date.hashCode();
    }

    @Override
    public int compareTo(@NotNull Day other) {
        return date.compareTo(other.date);
    }
}
