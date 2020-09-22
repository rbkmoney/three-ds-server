package com.rbkmoney.threeds.server.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.rbkmoney.threeds.server.ThreeDsServerApplication;
import com.rbkmoney.threeds.server.config.utils.JsonMapper;
import com.rbkmoney.threeds.server.mir.utils.ChallengeFlow;
import com.rbkmoney.threeds.server.mir.utils.FrictionlessFlow;
import com.rbkmoney.threeds.server.mir.utils.PreparationFlow;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.client.RestTemplate;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = AbstractMirPlatformConfig.TestConfig.class,
        properties = "spring.main.allow-bean-definition-overriding=true")
@ContextConfiguration(initializers = AbstractMirPlatformConfig.Initializer.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@TestPropertySource("classpath:application.yml")
@AutoConfigureMockMvc
public abstract class AbstractMirPlatformConfig {

    @RegisterExtension
    public static ServerExtension serverExtension = new ServerExtension();

    @TestConfiguration
    @Import(ThreeDsServerApplication.class)
    public static class TestConfig {

        @Bean
        public RestTemplate testRestTemplate() {
            return new RestTemplate();
        }

        @Bean
        public JsonMapper jsonMapper(ObjectMapper objectMapper, ResourceLoader resourceLoader) {
            return new JsonMapper(objectMapper, resourceLoader);
        }

        @Bean
        public FrictionlessFlow frictionlessFlow(JsonMapper jsonMapper) {
            return new FrictionlessFlow(jsonMapper);
        }

        @Bean
        public ChallengeFlow challengeFlow(JsonMapper jsonMapper) {
            return new ChallengeFlow(jsonMapper);
        }

        @Bean
        public PreparationFlow preparationFlow(JsonMapper jsonMapper) {
            return new PreparationFlow(jsonMapper);
        }
    }

    public static class Initializer extends ConfigFileApplicationContextInitializer {

        @Override
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            super.initialize(configurableApplicationContext);
            TestPropertyValues.of(
                    "storage.mode=IN_MEMORY",
                    "platform.mode=TEST_PLATFORM",
                    "environment.test.ds-url=http://localhost:" + serverExtension.getServer().port() + "/",
                    "environment.test.three-ds-requestor-url=https://rbk.money/",
                    "environment.test.three-ds-server-url=https://nspk.3ds.rbk.money/ds",
                    "environment.test.three-ds-server-ref-number=2200040105",
                    "environment.test.three-ds-server-operator-id=10075020",
                    "environment.test.three-ds-server-network-timeout=10000",
                    "environment.message.message-version=2.1.0",
                    "environment.message.valid-message-versions[0]=2.1.0",
                    "environment.message.valid-message-versions[1]=2.2.0"
            ).applyTo(configurableApplicationContext);
        }
    }

    public static class ServerExtension implements BeforeAllCallback, AfterAllCallback {

        private final WireMockServer server;

        public ServerExtension() {
            this.server = new WireMockServer(wireMockConfig().dynamicPort());
        }


        public WireMockServer getServer() {
            return server;
        }

        @Override
        public void beforeAll(ExtensionContext context) {
            server.start();
            WireMock.configureFor("localhost", server.port());
        }

        @Override
        public void afterAll(ExtensionContext context) {
            server.stop();
            server.resetAll();
        }
    }
}
