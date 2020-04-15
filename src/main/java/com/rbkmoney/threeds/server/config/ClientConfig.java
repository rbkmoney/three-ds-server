package com.rbkmoney.threeds.server.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rbkmoney.threeds.server.client.DsClient;
import com.rbkmoney.threeds.server.client.impl.DsClientImpl;
import com.rbkmoney.threeds.server.config.properties.EnvironmentProperties;
import com.rbkmoney.threeds.server.config.properties.KeystoreProperties;
import com.rbkmoney.threeds.server.converter.MessageToErrorResConverter;
import com.rbkmoney.threeds.server.exeption.SSLContextBuilderException;
import com.rbkmoney.threeds.server.flow.ErrorCodeResolver;
import com.rbkmoney.threeds.server.flow.ErrorMessageResolver;
import lombok.RequiredArgsConstructor;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.annotation.RequestScope;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.time.Duration;

@Configuration
@RequiredArgsConstructor
public class ClientConfig {

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        return objectMapper;
    }

    @Bean
    public DsClient dsClient(RestTemplate restTemplate,
                             EnvironmentProperties environmentProperties,
                             MessageToErrorResConverter messageToErrorConverter,
                             ErrorCodeResolver errorCodeResolver,
                             ErrorMessageResolver errorMessageResolver) {
        return new DsClientImpl(restTemplate, environmentProperties, messageToErrorConverter, errorCodeResolver, errorMessageResolver);
    }

    @Bean
    @RequestScope
    public RestTemplate restTemplate(KeystoreProperties keystoreProperties, ResourceLoader resourceLoader, EnvironmentProperties environmentProperties) {
        return new RestTemplateBuilder()
                .requestFactory(() -> new HttpComponentsClientHttpRequestFactory(httpClient(keystoreProperties, resourceLoader)))
                .setConnectTimeout(Duration.ofMillis(environmentProperties.getThreeDsServerNetworkTimeout()))
                .setReadTimeout(Duration.ofMillis(environmentProperties.getThreeDsServerNetworkTimeout()))
                .build();
    }

    private CloseableHttpClient httpClient(KeystoreProperties keystoreProperties, ResourceLoader resourceLoader) {
        SSLContext sslContext = sslContext(keystoreProperties, resourceLoader);

        return HttpClients.custom()
                .setSSLSocketFactory(new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE))
                .build();
    }

    private SSLContext sslContext(KeystoreProperties keystoreProperties, ResourceLoader resourceLoader) {
        try {
            return SSLContextBuilder.create()
                    .loadTrustMaterial(trustStore(keystoreProperties, resourceLoader), keystoreProperties.getTrustStorePassword().toCharArray(), (n, v) -> true)
                    .loadKeyMaterial(keyStore(keystoreProperties, resourceLoader), keystoreProperties.getTrustStorePassword().toCharArray())
                    .build();
        } catch (Exception ex) {
            throw new SSLContextBuilderException(ex);
        }
    }

    private File trustStore(KeystoreProperties keystoreProperties, ResourceLoader resourceLoader) throws IOException {
        return resourceLoader.getResource(keystoreProperties.getTrustStore()).getFile();
    }

    private KeyStore keyStore(KeystoreProperties keystoreProperties, ResourceLoader resourceLoader) throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException {
        KeyStore keyStore = KeyStore.getInstance("pkcs12");
        try (InputStream pKeyFileStream = Files.newInputStream(Path.of(resourceLoader.getResource(keystoreProperties.getTrustStore()).getURI()))) {
            keyStore.load(pKeyFileStream, keystoreProperties.getTrustStorePassword().toCharArray());
        }
        return keyStore;
    }
}
