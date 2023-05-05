package com.example.core;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Objects;

public abstract class Creneau implements Comparable<Creneau>, Serializable {
    private LocalTime heureDebut;
    private LocalTime heureFin;

    public Creneau(LocalTime heureDebut, LocalTime heureFin) {
        this.heureDebut = heureDebut;
        this.heureFin = heureFin;
    }

    public void setHeureDebut (LocalTime heureDebut){
        this.heureDebut = heureDebut ;
    }
    public void setHeureFin (LocalTime heureFin){
        this.heureFin = heureFin ;
    }
    public LocalTime getHeureDebut (){
        return this.heureDebut ;
    }
    public LocalTime getHeureFin (){
        return this.heureFin ;
    }
    public Duration getDuration() {
        return Duration.between(heureDebut, heureFin);
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

    @Override
    public String toString() {
        return "Creneau{" +
                "heureDebut=" + heureDebut +
                ", heureFin=" + heureFin +
                '}';
    }

    /**
     * compare la position de deux creneaux, si this est avant creneau elle retourne 1
     * @param creneau the object to be compared.
     * @return
     */
    public int avant(Creneau creneau) {
        if ((creneau.getHeureDebut()).compareTo(this.getHeureFin())>=0) {
            return 1 ;
        }
        else {return -1; }
    }


}
