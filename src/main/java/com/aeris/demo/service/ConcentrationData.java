package com.aeris.demo.service;

import com.aeris.demo.model.ConcentrationGrid;
import com.aeris.demo.model.ConcentrationGrid.ConcentrationRow;
import ucar.ma2.Array;
import ucar.ma2.InvalidRangeException;
import ucar.nc2.NetcdfFile;
import ucar.nc2.Variable;
import ucar.nc2.dt.image.ImageArrayAdapter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Objects;

public class ConcentrationData {

    private final NetcdfFile cdf;
    private final int timeIndex;
    private final int zIndex;

    public ConcentrationData(NetcdfFile cdf, int timeIndex, int zIndex) {
        this.cdf = cdf;
        this.timeIndex = timeIndex;
        this.zIndex = zIndex;
    }

    public double getTime() {
        try {
            var variable = cdf.findVariable("time");
            return Objects.requireNonNull(variable).read().getDouble(timeIndex);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public double getZ() {
        try {
            var variable = cdf.findVariable("z");
            return Objects.requireNonNull(variable).read().getDouble(zIndex);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ConcentrationGrid getConcentrationGrid() {

        try {
            var yData = Objects.requireNonNull(cdf.findVariable("y")).read();
            var xData = Objects.requireNonNull(cdf.findVariable("x")).read();
            var cData = getConcentrationVariable().read();
            var rowArray = getRows(yData, xData, cData);
            return new ConcentrationGrid(getTime(), getZ(), rowArray);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public InputStream getImage() {

        try {
            var cVar = getConcentrationVariable();
            var smallImage = ImageArrayAdapter.makeGrayscaleImage(cVar.read(), null);

            // make it 10x
            var newWidth = smallImage.getWidth() * 10;
            var newHeight = smallImage.getHeight() * 10;
            var bigImage = smallImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);

            // Create a buffered image with transparency
            BufferedImage image = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
            Graphics2D bGr = image.createGraphics();
            bGr.drawImage(bigImage, 0, 0, null);
            bGr.dispose();

            // write it out to a temp file and return an InputStream
            File file = File.createTempFile("cdf", ".png");
            ImageIO.write(image, "PNG", file);
            file.deleteOnExit();

            return Files.newInputStream(file.toPath());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private ConcentrationRow[] getRows(Array yData, Array xData, Array cData) {

        var index = 0;
        var rowList = new ArrayList<ConcentrationRow>();
        for (var yIndex = 0; yIndex < yData.getSize(); yIndex++) {
            var yValue = yData.getDouble(yIndex);
            var currentRow = new ConcentrationRow(yValue, (int) xData.getSize());
            for (var xIndex = 0; xIndex < xData.getSize(); xIndex++) {
                var xValue = xData.getDouble(xIndex);
                var cValue = cData.getDouble(index++);
                currentRow.addConcentration(xIndex, xValue, cValue);
            }
            xData.resetLocalIterator();
            rowList.add(currentRow);
        }

        return rowList.toArray(new ConcentrationRow[0]);
    }

    private Variable getConcentrationVariable() {
        try {
            return Objects.requireNonNull(cdf.findVariable("concentration"))
                    .slice(0, timeIndex)
                    .slice(0, zIndex);
        } catch (InvalidRangeException e) {
            throw new RuntimeException(e);
        }
    }
}