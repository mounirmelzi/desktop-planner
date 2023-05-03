package com.example.core;

import java.io.Serializable;
import java.util.HashSet;
import java.util.TreeSet;

public class Planning implements Serializable {
    private final TreeSet<Day> days = new TreeSet<>();
    private final TreeSet<Tache> taches = new TreeSet<>(); // les taches no programmées : unscheduled (l'ensemble des taches)
    private final HashSet<Project> projects = new HashSet<>();
    private Periode periode;
}
