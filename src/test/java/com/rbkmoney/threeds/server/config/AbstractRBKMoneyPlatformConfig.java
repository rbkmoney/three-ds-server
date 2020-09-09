package com.rbkmoney.threeds.server.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.rbkmoney.threeds.server.ThreeDsServerApplication;
import com.rbkmoney.threeds.server.config.utils.JsonMapper;
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
        classes = AbstractRBKMoneyPlatformConfig.TestConfig.class,
        properties = "spring.main.allow-bean-definition-overriding=true")
@ContextConfiguration(initializers = AbstractRBKMoneyPlatformConfig.Initializer.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@TestPropertySource("classpath:application.yml")
@AutoConfigureMockMvc
public abstract class AbstractRBKMoneyPlatformConfig {

    @RegisterExtension
    public static ServerExtension serverExtension = new ServerExtension();

    @TestConfiguration
    @Import(ThreeDsServerApplication.class)
    public static class TestConfig {

        @Bean
        public RestTemplate visaRestTemplate() {
            return testRestTemplate();
        }

        @Bean
        public RestTemplate mastercardRestTemplate() {
            return testRestTemplate();
        }

        @Bean
        public RestTemplate mirRestTemplate() {
            return testRestTemplate();
        }

        @Bean
        public JsonMapper jsonMapper(ObjectMapper objectMapper, ResourceLoader resourceLoader) {
            return new JsonMapper(objectMapper, resourceLoader);
        }

        private RestTemplate testRestTemplate() {
            return new RestTemplate();
        }
    }

    public static class Initializer extends ConfigFileApplicationContextInitializer {

        @Override
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            super.initialize(configurableApplicationContext);
            TestPropertyValues.of(
                    "storage.mode=IN_MEMORY",
                    "platform.mode=RBK_MONEY_PLATFORM",
                    "preparation-flow.on-startup.enabled=false",
                    "preparation-flow.on-schedule.enabled=false",
                    "environment.test.ds-url=http://localhost:" + serverExtension.getServer().port() + "/",
                    "environment.test.three-ds-requestor-url=https://rbk.money/",
                    "environment.test.three-ds-server-url=https://3ds.rbk.money/ds",
                    "environment.test.three-ds-server-ref-number=3DS_LOA_SER_PPFU_020100_00008",
                    "environment.test.three-ds-server-operator-id=10075020",
                    "environment.test.three-ds-server-network-timeout=10000",
                    "environment.visa.ds-url=http://localhost:" + serverExtension.getServer().port() + "/",
                    "environment.visa.three-ds-requestor-url=https://rbk.money/",
                    "environment.visa.three-ds-server-url=https://visa.3ds.rbk.money/ds",
                    "environment.visa.three-ds-server-ref-number=3DS_LOA_SER_DIPL_020200_00236",
                    "environment.visa.three-ds-server-operator-id=10075020",
                    "environment.visa.three-ds-server-network-timeout=10000",
                    "environment.mastercard.ds-url=http://localhost:" + serverExtension.getServer().port() + "/",
                    "environment.mastercard.three-ds-requestor-url=https://rbk.money/",
                    "environment.mastercard.three-ds-server-url=https://mastercard.3ds.rbk.money/ds",
                    "environment.mastercard.three-ds-server-ref-number=3DS_LOA_SER_DIPL_020200_00236",
                    "environment.mastercard.three-ds-server-operator-id=SVR-V210-RBK_MONEY_LTD.-23309",
                    "environment.mastercard.three-ds-server-network-timeout=10000",
                    "environment.mir.ds-url=http://localhost:" + serverExtension.getServer().port() + "/",
                    "environment.mir.three-ds-requestor-url=https://rbk.money/",
                    "environment.mir.three-ds-server-url=https://nspk.3ds.rbk.money/ds",
                    "environment.mir.three-ds-server-ref-number=3DS_LOA_SER_DIPL_020200_00236",
                    "environment.mir.three-ds-server-operator-id=2200040105",
                    "environment.mir.three-ds-server-network-timeout=10000",
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
