package com.example.core;

import java.io.Serializable;
import java.util.Comparator;
import java.util.TreeSet;

public class Project implements Serializable {
    private String nom;
    private String description;
    private TreeSet<Tache> taches;

    public Project(String nom, String description) {
        this.nom = nom;
        this.description = description;
        this.taches = new TreeSet<>(Comparator.reverseOrder());
    }

    public Project(String nom, String description, TreeSet<Tache> taches) {
        this(nom, description);
        this.taches = taches;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Project project = (Project) o;
        return nom.equals(project.nom);
    }

    @Override
    public int hashCode() {
        return nom.hashCode();
    }

    @Override
    public String toString() {
        return "Project{" +
                "nom='" + nom + '\'' +
                ", description='" + description + '\'' +
                ", taches=" + taches +
                '}';
    }
}
