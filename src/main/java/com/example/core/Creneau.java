package com.example.core;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Objects;

public abstract class Creneau implements Comparable<Creneau>, Serializable {
    //region Attributes

    private LocalTime heureDebut;
    private LocalTime heureFin;

    //endregion

    //region Constructors

    public Creneau(@NotNull LocalTime heureDebut, @NotNull LocalTime heureFin) {
        this.heureDebut = heureDebut;
        this.heureFin = heureFin;

        if (heureDebut.isAfter(heureFin)) {
            this.heureDebut = heureFin;
            this.heureFin = heureDebut;
        }
    }

    //endregion

    //region Setter and Getters

    public LocalTime getHeureDebut (){
        return this.heureDebut ;
    }

    public LocalTime getHeureFin (){
        return this.heureFin ;
    }

    public Duration getDuration() {
        return Duration.between(heureDebut, heureFin);
    }

    //endregion

    //region Methods

    /**
     * compare la position de deux creneaux
     * @param creneau le creneau a comparer avec
     * @return true si this est avant creneau, false si non
     */
    public boolean isAvant(@NotNull Creneau creneau) {
        return !getHeureFin().isAfter(creneau.getHeureDebut());
    }

    /**
     * compare la position de deux creneaux
     * @param creneau le creneau a comparer avec
     * @return true si this est apres creneau, false si non
     */
    public boolean isApres(@NotNull Creneau creneau) {
        return !getHeureDebut().isBefore(creneau.getHeureFin());
    }

    /**
     * vérifier si le créneau est libre ou non
     * @return true si le créneau est libre, false si non
     */
    public boolean isLibre() {
        return this instanceof CreneauLibre;
    }

    /**
     * vérifier si le créneau est Occupe ou non
     * @return true si le créneau est Occupe, false si non
     */
    public boolean isOccupe() {
        return this instanceof CreneauOccupe;
    }

    @Override
    public int compareTo(@NotNull Creneau o) {
        return heureDebut.compareTo(o.heureDebut);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Creneau creneau = (Creneau) o;
        return Objects.equals(heureDebut, creneau.heureDebut) && Objects.equals(heureFin, creneau.heureFin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(heureDebut, heureFin);
    }

    //endregion
}
