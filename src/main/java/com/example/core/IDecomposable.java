package com.example.core;

import com.example.core.exceptions.DecompositionImpossibleException;

import java.util.TreeSet;

public interface IDecomposable<Decomposer, Child> {
    TreeSet<Child> decomposer(Decomposer decomposer) throws DecompositionImpossibleException;
}
