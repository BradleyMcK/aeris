package com.aeris.demo.controller;

import com.aeris.demo.model.ConcentrationGrid;
import com.aeris.demo.service.CdfService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.InputStream;

@RestController
@RequestMapping("/")
public class CdfController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CdfController.class);

    @Autowired
    private CdfService cdfService;

    /**
        get-info, returns the NetCDF detailed information.
    */

    @GetMapping("/get-info")
    public String getInfo() {
        return cdfService.getInfo();
    }

    /**
        get-data, params to include time index and z index, returns json response that
        includes x, y, and concentration data.
    */

    @GetMapping("/get-data")
    public ConcentrationGrid getData(@RequestParam Integer tIndex, @RequestParam Integer zIndex) {
        return cdfService.getData(tIndex, zIndex);
    }

    /**
        get-image, params to include time index and z index, returns png visualization of
        concentration.
     */

    @GetMapping(value="/get-image", produces=MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<StreamingResponseBody> getImage(@RequestParam Integer tIndex, @RequestParam Integer zIndex) {

        StreamingResponseBody responseBody = os -> {
            try (InputStream is = cdfService.getImageStream(tIndex, zIndex)) {
                is.transferTo(os);
            } catch (RuntimeException e) {
                LOGGER.error(e.getMessage(), e);
                throw e;
            }
        };

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(responseBody);
    }
}
