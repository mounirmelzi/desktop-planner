package com.example.core;

import com.example.core.exceptions.UnscheduledException;

public interface IPlanifiable {
    void planifier(Planning planning) throws UnscheduledException;
}
