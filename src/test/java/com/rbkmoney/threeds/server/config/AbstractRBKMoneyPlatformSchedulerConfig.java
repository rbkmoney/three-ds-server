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
        classes = AbstractRBKMoneyPlatformSchedulerConfig.TestConfig.class,
        properties = "spring.main.allow-bean-definition-overriding=true")
@ContextConfiguration(initializers = AbstractRBKMoneyPlatformSchedulerConfig.Initializer.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@TestPropertySource("classpath:application.yml")
@AutoConfigureMockMvc
public abstract class AbstractRBKMoneyPlatformSchedulerConfig {

    @RegisterExtension
    public static ServerExtension serverExtension = new ServerExtension();

    @TestConfiguration
    @Import(ThreeDsServerApplication.class)
    public static class TestConfig {

        //        @Bean
//        public RestTemplate visaRestTemplate() {
//            return restTemplate();
//        }
//
//        @Bean
//        public RestTemplate mastercardRestTemplate() {
//            return restTemplate();
//        }
//
//        @Bean
//        public RestTemplate mirRestTemplate() {
//            return restTemplate();
//        }
//
        @Bean
        public JsonMapper jsonMapper(ObjectMapper objectMapper, ResourceLoader resourceLoader) {
            return new JsonMapper(objectMapper, resourceLoader);
        }

        private RestTemplate restTemplate() {
            return new RestTemplate();
        }
    }

    public static class Initializer extends ConfigFileApplicationContextInitializer {

        @Override
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            super.initialize(configurableApplicationContext);
            TestPropertyValues.of(
                    "client.ds.ssl.visa.trust-store=classpath:3ds_server_pki/mc.p12",
                    "client.ds.ssl.visa.trust-store-password=76UetirwSjugZh6t",
                    "client.ds.ssl.mastercard.trust-store=classpath:3ds_server_pki/mc.p12",
                    "client.ds.ssl.mastercard.trust-store-password=76UetirwSjugZh6t",
                    "client.three-ds-server-storage.card-ranges.url=http://localhost:" + serverExtension.getServer().port() + "/three-ds-server-storage/card-ranges",
                    "client.three-ds-server-storage.card-ranges.timeout=5000",
                    "client.three-ds-server-storage.challenge-flow-transaction-info.url=http://localhost:" + serverExtension.getServer().port() + "/three-ds-server-storage/challenge-flow-transaction-info",
                    "client.three-ds-server-storage.challenge-flow-transaction-info.timeout=5000",
                    "storage.challenge-flow-transaction-info.size=1000",
                    "platform.mode=RBK_MONEY_PLATFORM",
                    "rbkmoney-preparation-flow.scheduler.enabled=true",
                    "rbkmoney-preparation-flow.scheduler.schedule.executor-url=http://localhost:" + serverExtension.getServer().port() + "/three-ds-server-storage/preparation-flow",
                    "rbkmoney-preparation-flow.scheduler.schedule.cron=0 0 8-20 ? * *",
                    "rbkmoney-preparation-flow.scheduler.schedule.timeout=5000",
                    "rbkmoney-preparation-flow.scheduler.ds-provider.mastercard.enabled=true",
                    "rbkmoney-preparation-flow.scheduler.ds-provider.mastercard.message-version=2.1.0",
                    "rbkmoney-preparation-flow.scheduler.ds-provider.visa.enabled=true",
                    "rbkmoney-preparation-flow.scheduler.ds-provider.visa.message-version=2.1.0",
                    "environment.message.message-version=2.2.0",
                    "environment.message.valid-message-versions[0]=2.1.0",
                    "environment.message.valid-message-versions[1]=2.2.0",
                    "environment.visa.ds-url=http://localhost:" + serverExtension.getServer().port() + "/visa/DS2/authenticate",
                    "environment.visa.three-ds-requestor-url=visa",
                    "environment.visa.three-ds-requestor-prefix=visa",
                    "environment.visa.three-ds-server-url=http://visa.3ds.rbk.money/ds",
                    "environment.visa.three-ds-server-ref-number=visa",
                    "environment.visa.three-ds-server-operator-id=visa",
                    "environment.visa.three-ds-server-read-timeout=10000",
                    "environment.visa.three-ds-server-connect-timeout=5000",
                    "environment.mastercard.ds-url=http://localhost:" + serverExtension.getServer().port() + "/mastercard/DS2/authenticate",
                    "environment.mastercard.three-ds-requestor-url=mastercard",
                    "environment.mastercard.three-ds-requestor-prefix=mastercard",
                    "environment.mastercard.three-ds-server-url=http://mastercard.3ds.rbk.money/ds",
                    "environment.mastercard.three-ds-server-ref-number=mastercard",
                    "environment.mastercard.three-ds-server-operator-id=mastercard",
                    "environment.mastercard.three-ds-server-read-timeout=10000",
                    "environment.mastercard.three-ds-server-connect-timeout=5000")
                    .applyTo(configurableApplicationContext);
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
