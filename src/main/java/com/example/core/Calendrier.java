package com.example.core;

import java.io.Serializable;
import java.util.TreeSet;

public class Calendrier implements Serializable {
    private TreeSet<Day> days = new TreeSet<>();
    private Planning planning;
}
