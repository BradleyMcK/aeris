package com.aeris.demo.controller;

import com.aeris.demo.service.CdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class CdfController {

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
    public String getData(@RequestParam String timeIndex, @RequestParam String zIndex) {
        return "get-data";
    }

    /**
        get-image, params to include time index and z index, returns png visualization of
        concentration.
     */

    @GetMapping("/get-image")
    public String getImage(@RequestParam String timeIndex, @RequestParam String zIndex) {
        return "get-image";
    }
}
