package com.rbkmoney.threeds.server.mir.utils.challenge;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class HttpBuilder {

    public static HttpEntity<MultiValueMap<String, String>> buildHttpRequestWithInitEncodedCReq(String body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("creq", body);

        return new HttpEntity<>(map, headers);
    }

    public static HttpEntity<MultiValueMap<String, String>> buildHttpRequestWithCorrectPassword() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("password", "1qwezxc");
        map.add("submit", "Submit");

        return new HttpEntity<>(map, headers);
    }

    public static HttpEntity<MultiValueMap<String, String>> buildHttpRequestWithIncorrectPassword() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("password", "1zxcqwe");
        map.add("submit", "Submit");

        return new HttpEntity<>(map, headers);
    }
}
