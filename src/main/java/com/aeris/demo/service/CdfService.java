package com.aeris.demo.service;

import com.aeris.demo.model.ConcentrationGrid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.core.io.ClassPathResource;

import ucar.nc2.NetcdfFile;
import ucar.nc2.NetcdfFiles;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

@Service
public class CdfService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CdfService.class);
    private static final String CDF_FILENAME = "concentration.timeseries.nc";

    private final URL cdfUrl;

    public CdfService() {
        this(CDF_FILENAME);
    }

    public CdfService(String cdfFilename) {

        try {
            cdfUrl = (new ClassPathResource(cdfFilename)).getURL();
        } catch (IOException e) {
            String errMsg = String.format("Could not locate file %s", cdfFilename);
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
            ConcentrationData cd = new ConcentrationData(cdf, timeIndex, zIndex);
            return cd.getConcentrationGrid();
        } catch (Exception e) {
            LOGGER.error("Error getting concentration data from CDF file", e);
            throw new RuntimeException(e);
        }
    }

    public InputStream getImageStream(Integer timeIndex, Integer zIndex) {

        try (NetcdfFile cdf = NetcdfFiles.open(cdfUrl.getPath())) {
            ConcentrationData cd = new ConcentrationData(cdf, timeIndex, zIndex);
            return cd.getImage();
        } catch (Exception e) {
            LOGGER.error("Error creating image from concentration data", e);
            throw new RuntimeException(e);
        }
    }
}

