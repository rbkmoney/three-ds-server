package com.rbkmoney.threeds.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.google.common.base.Charsets;
import com.rbkmoney.threeds.server.domain.root.Message;
import io.micrometer.core.instrument.util.IOUtils;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

@RunWith(SpringRunner.class)
public abstract class TestBase {

    public static final int PORT = 8000;
    public static final String TEST_URL = "http://localhost:" + PORT + "/";

    private final ObjectMapper objectMapper = objectMapper();

    @Autowired
    private ResourceLoader resourceLoader;

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(wireMockConfig().port(PORT));

    private ObjectMapper objectMapper() {
        return new ObjectMapper()
                .findAndRegisterModules();
    }

    public Message readMessageFromFile(String fileName) throws IOException {
        return objectMapper.readValue(
                IOUtils.toString(resourceLoader.getResource("classpath:__files/" + fileName).getInputStream(), Charsets.UTF_8),
                Message.class);
    }

    public String readStringFromFile(String path, String fileName) throws IOException {
        return IOUtils.toString(
                resourceLoader.getResource("classpath:__files/" + path + fileName).getInputStream(),
                Charsets.UTF_8);
    }

    public void givenAReqSuccessResponse() {
        stubFor(post(urlEqualTo("/"))
                .willReturn(
                        aResponse()
                                .withStatus(HttpStatus.OK.value())
                                .withHeader("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE)
                                .withBodyFile("happy-path-ARes.json")));
    }

    public void givenAReqErrorResponse() {
        stubFor(post(urlEqualTo("/"))
                .willReturn(
                        aResponse()
                                .withStatus(HttpStatus.OK.value())
                                .withHeader("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE)
                                .withBodyFile("error-path-Erro.json")));
    }
}
