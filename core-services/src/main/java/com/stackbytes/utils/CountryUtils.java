package com.stackbytes.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.Inet4Address;
import java.net.UnknownHostException;

@Component
public class CountryUtils {

    private static final String API_URL = "http://ip-api.com/json/";
    private final RestTemplate restTemplate;



    public CountryUtils(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getCountryCode(HttpServletRequest request) throws UnknownHostException {
        String ipAddress = request.getRemoteAddr();

        if ("127.0.0.1".equals(ipAddress)) {
            return "RO";
        }

        String response = restTemplate.getForObject(String.format("%s%s", API_URL, ipAddress), String.class);

        if (response != null && response.contains("\"countryCode\":\"")) {
            return response.split("\"countryCode\":\"")[1].split("\"")[0];
        }

        return null;
    }
}
