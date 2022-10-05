package com.example.nasa_demo_1.service;

import java.net.URI;
import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.JsonNode;

@Service
public class NasaService {

    @Value("${nasa.api.url}")
    private String nasaUrl;

    @Value("${nasa.api.key}")
    private String nasaKey;

    private final RestTemplate restTemplate;

    public NasaService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Cacheable("largest")
    public byte[] getLargestPic(Integer sol, String camera) {
        var uri = UriComponentsBuilder.fromHttpUrl(nasaUrl)
          .queryParam("api_key", nasaKey)
          .queryParam("sol", sol)
          .queryParamIfPresent("camera", Optional.ofNullable(camera))
          .build().toUri();
        return
          Optional.ofNullable(restTemplate.getForObject(uri, JsonNode.class))
            .orElseThrow()
            .get("photos").findValues("img_src")
            .stream()
            .parallel()
            .map(url -> getPictureByteArray(url.asText()))
            .max(Comparator.comparing(e -> e.length))
            .orElseThrow(() -> new NoSuchElementException("No pictures found"));
    }

    private byte[] getPictureByteArray(String url) {
        var location = restTemplate.headForHeaders(URI.create(url)).getLocation();
        return restTemplate.getForObject(location, byte[].class);

    }
}
