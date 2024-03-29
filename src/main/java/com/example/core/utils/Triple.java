package com.example.core.utils;

public class Triple<T1, T2, T3> {
    private T1 v1;
    private T2 v2;
    private T3 v3;

    public Triple(T1 v1, T2 v2, T3 v3) {
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
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

    public T3 getThird() {
        return v3;
    }

    public void setThird(T3 v3) {
        this.v3 = v3;
    }
}
