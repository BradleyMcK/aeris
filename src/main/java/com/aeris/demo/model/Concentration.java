package com.aeris.demo.model;

public class Concentration {

    private final double time;
    private final double z;
    private final double y;
    private final double x;
    private final double c;

    public Concentration(double time, double z, double y, double x, double c) {
        this.time = time;
        this.z = z;
        this.y = y;
        this.x = x;
        this.c = c;
    }

    public double getTime() {
        return time;
    }

    public double getZ() {
        return z;
    }

    public double getY() {
        return y;
    }

    public double getX() {
        return x;
    }

    public double getC() {
        return c;
    }
}
