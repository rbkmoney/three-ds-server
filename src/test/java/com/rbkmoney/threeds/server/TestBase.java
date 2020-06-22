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
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

@RunWith(SpringRunner.class)
public abstract class TestBase {

    public static final int PORT = 8000;
    public static final String TEST_URL = "http://localhost:" + PORT + "/";

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    private ResourceLoader resourceLoader;

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(wireMockConfig().port(PORT));

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
}
