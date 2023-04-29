package com.example.core;

import java.util.TreeSet;

public interface IDecomposable<Decomposer, Child> {
    TreeSet<Child> decomposer(Decomposer decomposer);
}
