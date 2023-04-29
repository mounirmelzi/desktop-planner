package com.example.core;

import java.io.Serializable;
import java.util.ArrayList;

public class Calendrier implements Serializable {
    private final ArrayList<Planning> plannings;

    public Calendrier() {
        this.plannings = new ArrayList<>();
    }

    public Calendrier(ArrayList<Planning> plannings) {
        this.plannings = plannings;
    }
}
