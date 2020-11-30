package com.rbkmoney.threeds.server.config.builder;

import com.rbkmoney.threeds.server.config.properties.EnvironmentProperties;
import com.rbkmoney.threeds.server.config.properties.KeystoreProperties;
import com.rbkmoney.threeds.server.exception.SSLContextBuilderException;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.time.Duration;

public class RestTemplateBuilder {

    private static final String PKCS_12 = "pkcs12";

    public static RestTemplate restTemplate(
            EnvironmentProperties environmentProperties,
            KeystoreProperties keystoreProperties,
            ResourceLoader resourceLoader) {
        return new org.springframework.boot.web.client.RestTemplateBuilder()
                .requestFactory(() -> new HttpComponentsClientHttpRequestFactory(httpClient(keystoreProperties, resourceLoader)))
                .setConnectTimeout(Duration.ofMillis(environmentProperties.getThreeDsServerNetworkTimeout()))
                .setReadTimeout(Duration.ofMillis(environmentProperties.getThreeDsServerNetworkTimeout()))
                .build();
    }

    private static CloseableHttpClient httpClient(KeystoreProperties keystoreProperties, ResourceLoader resourceLoader) {
        SSLContext sslContext = sslContext(keystoreProperties, resourceLoader);

        return HttpClients.custom()
                .setSSLSocketFactory(new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE))
                .build();
    }

    private static SSLContext sslContext(KeystoreProperties keystoreProperties, ResourceLoader resourceLoader) {
        try {
            return SSLContextBuilder.create()
                    .loadTrustMaterial(
                            trustStore(keystoreProperties, resourceLoader),
                            keystoreProperties.getTrustStorePassword().toCharArray(),
                            (n, v) -> true)
                    .loadKeyMaterial(
                            keyStore(keystoreProperties, resourceLoader),
                            keystoreProperties.getTrustStorePassword().toCharArray())
                    .build();
        } catch (Exception ex) {
            throw new SSLContextBuilderException(ex);
        }
    }

    private static File trustStore(KeystoreProperties keystoreProperties, ResourceLoader resourceLoader) throws IOException {
        return resourceLoader
                .getResource(keystoreProperties.getTrustStore())
                .getFile();
    }

    private static KeyStore keyStore(
            KeystoreProperties keystoreProperties,
            ResourceLoader resourceLoader) throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException {
        KeyStore keyStore = KeyStore.getInstance(PKCS_12);
        URI uri = resourceLoader.getResource(keystoreProperties.getTrustStore()).getURI();

        try (InputStream pKeyFileStream = Files.newInputStream(Path.of(uri))) {
            keyStore.load(pKeyFileStream, keystoreProperties.getTrustStorePassword().toCharArray());
        }

        return keyStore;
    }
}
