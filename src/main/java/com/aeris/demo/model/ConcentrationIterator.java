package com.aeris.demo.model;

import com.aeris.demo.model.Concentration;
import com.aeris.demo.model.ConcentrationGrid;
import com.aeris.demo.model.ConcentrationRow;
import ucar.ma2.Array;
import ucar.ma2.IndexIterator;
import ucar.ma2.InvalidRangeException;
import ucar.nc2.NetcdfFile;

import java.io.IOException;
import java.util.Iterator;

public class ConcentrationIterator implements Iterator<Concentration> {

    private final double timeValue;
    private final double zValue;

    private final Array yData;
    private final Array xData;
    private final IndexIterator cIterator;

    private double yValue = Double.POSITIVE_INFINITY;
    private int yIndex = -1;

    public ConcentrationIterator(NetcdfFile cdf, int timeIndex, int zIndex) {
        try {
            this.timeValue = cdf.findVariable("time").read().getDouble(timeIndex);
            this.zValue = cdf.findVariable("z").read().getDouble(zIndex);
            this.yData = cdf.findVariable("y").read();
            this.xData = cdf.findVariable("x").read();

            this.cIterator = cdf.findVariable("concentration")
                    .slice(0, timeIndex)
                    .slice(0, zIndex)
                    .read().getIndexIterator();

        } catch (IOException | InvalidRangeException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean hasNext() {
        return cIterator.hasNext();
    }

    @Override
    public Concentration next() {

        var cValue = cIterator.getDoubleNext();
        var counter = cIterator.getCurrentCounter();
        if (counter[0] != yIndex) {
            yIndex = counter[0];
            yValue = yData.getDouble(yIndex);
            xData.resetLocalIterator();
        }
        var xValue = xData.getDouble(counter[1]);
        return new Concentration(timeValue, zValue, yValue, xValue, cValue);
    }

    public double getTime() {
        return this.timeValue;
    }

    public double getZ() {
        return this.zValue;
    }

    public ConcentrationRow[] getConcentrationRows() {

        var index = 0;
        var lastIndex = -1;
        ConcentrationRow curRow = null;
        var lastValue = Double.POSITIVE_INFINITY;
        var myData = new ConcentrationRow[(int) yData.getSize()];

        while (cIterator.hasNext()) {
            var cValue = cIterator.getDoubleNext();
            var counter = cIterator.getCurrentCounter();
            if (counter[0] != lastIndex) {
                lastIndex = counter[0];
                lastValue = yData.getDouble(lastIndex);
                curRow = new ConcentrationRow(lastValue, (int) xData.getSize());
                myData[lastIndex] = curRow;
                xData.resetLocalIterator();
                index = 0;
            }
            var xValue = xData.getDouble(counter[1]);
            var c = new Concentration(timeValue, zValue, yValue, xValue, cValue);
            curRow.addConcentration(index++, xValue, cValue);
        }
        return myData;
    }
}
