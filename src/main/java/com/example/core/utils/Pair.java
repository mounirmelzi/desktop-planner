package com.example.core.utils;

public class Pair<T1, T2> {
    private T1 v1;
    private T2 v2;

    public Pair(T1 v1, T2 v2) {
        this.v1 = v1;
        this.v2 = v2;
    }

    public T1 getFirst() {
        return v1;
    }

    public void setFirst(T1 v1) {
        this.v1 = v1;
    }

    public T2 getSecond() {
        return v2;
    }

    public void setSecond(T2 v2) {
        this.v2 = v2;
    }
}
