package com.rbkmoney.threeds.server.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.rbkmoney.threeds.server.ThreeDsServerApplication;
import com.rbkmoney.threeds.server.config.utils.JsonMapper;
import com.rbkmoney.threeds.server.mastercard.MastercardPlatformTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.client.RestTemplate;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

//@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = MastercardPlatformTest.TestConfig.class,
        properties = "spring.main.allow-bean-definition-overriding=true")
@ContextConfiguration(initializers = AbstractMastercardConfig.Initializer.class)
@TestPropertySource("classpath:application.yml")
@AutoConfigureMockMvc
public abstract class AbstractMastercardConfig {

    //    @ClassRule
    public static WireMockRule wireMock = new WireMockRule(wireMockConfig().dynamicPort());

    @BeforeAll
    public static void setup() {
        wireMock = new WireMockRule(wireMockConfig().dynamicPort());
        wireMock.start();
        WireMock.configureFor("localhost", wireMock.port());
    }

    @AfterAll
    public static void tearDown() {
        wireMock.stop();
    }

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
    }

    public static class Initializer extends ConfigFileApplicationContextInitializer {

        @Override
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            super.initialize(configurableApplicationContext);
            TestPropertyValues.of(
                    "spring.main.allow-bean-definition-overriding=true",
                    "storage.mode=IN_MEMORY",
                    "platform.mode=TEST_PLATFORM",
                    "preparation-flow.on-startup.enabled=false",
                    "preparation-flow.on-schedule.enabled=false",
                    "environment.test.ds-url=http://localhost:" + wireMock.port() + "/",
                    "environment.test.three-ds-requestor-url=https://rbk.money/",
                    "environment.test.three-ds-server-url=https://3ds.rbk.money/ds",
                    "environment.test.three-ds-server-ref-number=3DS_LOA_SER_PPFU_020100_00008",
                    "environment.test.three-ds-server-operator-id=10075020",
                    "environment.test.three-ds-server-network-timeout=10000",
                    "environment.message.message-version=2.1.0",
                    "environment.message.valid-message-versions[0]=2.1.0",
                    "environment.message.valid-message-versions[1]=2.2.0"
            ).applyTo(configurableApplicationContext);
        }
    }
}
