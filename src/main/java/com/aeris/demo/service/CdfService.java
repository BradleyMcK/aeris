package com.aeris.demo.service;

import com.aeris.demo.model.ConcentrationGrid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ucar.nc2.NetcdfFile;
import ucar.nc2.NetcdfFiles;

import java.io.IOException;
import java.net.URL;

@Service
public class CdfService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CdfService.class);
    private static final String CDF_FILENAME = "static/concentration.timeseries.nc";

    private final URL cdfUrl;

    public CdfService() {
        this(CDF_FILENAME);
    }

    public CdfService(String cdfFilename) {
        cdfUrl = ClassLoader.getSystemResource(cdfFilename);
        if (cdfUrl == null) {
            String errMsg = String.format("Could not locate file %s", CDF_FILENAME);
            throw new IllegalArgumentException(errMsg);
        }
    }

    public String getInfo() {

        try (NetcdfFile cdf = NetcdfFiles.open(cdfUrl.getPath())) {
            return cdf.getDetailInfo();
        } catch (IOException e) {
            LOGGER.error("Error opening CDF file", e);
            throw new RuntimeException(e);
        }
    }

//    . /get-data, params to include time index and z index, returns json response that
//    includes x, y, and concentration data.

    public ConcentrationGrid getData(Integer timeIndex, Integer zIndex) {

        try (NetcdfFile cdf = NetcdfFiles.open(cdfUrl.getPath())) {
            /*
            ConcentrationIterator ci = new ConcentrationIterator(cdf, timeIndex, zIndex);
            var tVal = ci.getTime();
            var zVal = ci.getZ();
            while (ci.hasNext()) {
                var concentration = ci.next();
                var cVal = concentration.getC();
                var yVal = concentration.getY();
                var xVal = concentration.getX();
                System.out.println(String.format("{ time=%f z=%f y=%f x=%f c=%e }", tVal, zVal, yVal, xVal, cVal));
            }
            */

            return new ConcentrationGrid(cdf, timeIndex, zIndex);
        } catch (Exception e) {
            LOGGER.error("Error opening CDF file", e);
            throw new RuntimeException(e);
        }
    }

}


