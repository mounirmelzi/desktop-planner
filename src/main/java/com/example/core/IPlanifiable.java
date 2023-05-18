package com.example.core;

import com.example.core.exceptions.UnscheduledException;

import java.time.LocalDateTime;

public interface IPlanifiable {
    LocalDateTime planifier(Planning planning, LocalDateTime startDateTime) throws UnscheduledException;
    boolean isUnscheduled();
    void deplanifier();
}
