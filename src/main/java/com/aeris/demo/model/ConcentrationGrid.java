package com.aeris.demo.model;

import ucar.nc2.NetcdfFile;

public class ConcentrationGrid {

    private final double time;
    private final double z;
    private final ConcentrationRow[] rows;

    public ConcentrationGrid(NetcdfFile cdf, int timeIndex, int zIndex) {
        ConcentrationIterator iter = new ConcentrationIterator(cdf, timeIndex, zIndex);
        this.time = iter.getTime();
        this.z = iter.getZ();
        this.rows = iter.getConcentrationRows();
    }

    public double getTime() {
        return time;
    }

    public double getZ() {
        return z;
    }

    public ConcentrationRow[] getRows() {
        return rows;
    }
}
