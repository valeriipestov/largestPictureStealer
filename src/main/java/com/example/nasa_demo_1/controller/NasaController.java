package com.example.nasa_demo_1.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.nasa_demo_1.service.NasaService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/nasa")
@AllArgsConstructor
public class NasaController {

    private NasaService nasaService;

    @GetMapping(value = "/mars/pictures/largest", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getLargestPic(@RequestParam Integer sol, @RequestParam String camera) {
        return nasaService.getLargestPic(sol, camera);
    }


}
