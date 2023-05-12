package com.example.core;

import com.example.core.exceptions.UnscheduledException;
import com.example.core.utils.Pair;

import java.time.LocalDateTime;
import java.util.TreeSet;

public interface IPlanifiable {
    Pair<Day, TreeSet<Creneau>> planifier(Planning planning, LocalDateTime startDateTime) throws UnscheduledException;
}
