package com.rbkmoney.threeds.server.miraccept;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rbkmoney.threeds.server.ThreeDsServerApplication;
import com.rbkmoney.threeds.server.domain.Valuable;
import com.rbkmoney.threeds.server.domain.root.emvco.CReq;
import com.rbkmoney.threeds.server.domain.root.emvco.CRes;
import com.rbkmoney.threeds.server.domain.root.proprietary.PArs;
import com.rbkmoney.threeds.server.serialization.EnumWrapper;
import com.rbkmoney.threeds.server.serialization.ListWrapper;
import com.rbkmoney.threeds.server.serialization.TemporalAccessorWrapper;
import com.rbkmoney.threeds.server.service.SenderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAccessor;
import java.util.*;
import java.util.function.Function;

@RunWith(SpringRunner.class)
@SpringBootTest(
        classes = {ThreeDsServerApplication.class, TestConfig.class},
        properties = {
                "client.ssl.trust-store=classpath:3ds_server_pki/mir2.p12",
                "client.ssl.trust-store-password=vYOAkEF7V4UHLfMn",
                "environment.ds-url=https://ds.vendorcert.mirconnect.ru:8443/ds/DServer",
                "environment.three-ds-server-ref-number=2200040105",
                "environment.three-ds-server-url=https://nspk.3ds.rbk.money/ds",
                "logging.level.com=error",
                "logging.level.org=error",
                "logging.level.com.rbkmoney.threeds.server.service.impl=debug",
                "logging.level.com.rbkmoney.threeds.server.controller=debug",
                "logging.level.com.rbkmoney.threeds.server.miraccept=debug",
        },
        webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT
)
@Ignore
@Slf4j
public abstract class MirAcceptIntegrationConfig {

    private static boolean WRITE_DATA_IN_FILE = false;

    @Autowired
    protected SenderService senderService;

    @Autowired
    protected RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ResourceLoader resourceLoader;

    private static final Random RANDOM = new Random();

    //nspk
    protected static final String ACQUIRER_BIN = "2200040105";
    protected static final String ACQUIRER_MERCHANT_ID = "RBK_mrc_1";
    protected static final String THREE_DS_REQUESTOR_ID = "2200040105";
    protected static final String THREE_DS_SERVER_OPERATOR_ID = "2200040105";
    protected static final String THREE_DS_SERVER_REF_NUMBER = "2200040105";

    protected static final String CARD_EXPIRY_DATE = "2012";
    protected static final String MERCHANT_COUNTRY_CODE = "643";
    protected static final String PURCHASE_CURRENCY = "643";
    protected static final String MESSAGE_VERSION = "2.1.0";

    protected CRes sendAs3dsClientTypeBRW(PArs pArs, Function<String, ResponseEntity<String>> function) {
        return sendAs3dsClientTypeBRW(pArs, function, "05");
    }

    protected CRes sendAs3dsClientTypeBRW(PArs pArs, Function<String, ResponseEntity<String>> function, String challengeWindowSize) {
        CReq cReq = CReq.builder()
                .acsTransID(pArs.getAcsTransID())
                .challengeWindowSize(challengeWindowSize)
                .messageVersion(pArs.getMessageVersion())
                .threeDSServerTransID(pArs.getThreeDSServerTransID())
                .build();

        try {
            log.info("\n" + objectMapper.writeValueAsString(cReq));

            byte[] bytesCReq = objectMapper.writeValueAsBytes(cReq);

            String encodeCReq = Base64.getEncoder().encodeToString(bytesCReq);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("creq", encodeCReq);

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(pArs.getAcsURL(), request, String.class);

            String html = response.getBody();
            log.info("\n" + objectMapper.writeValueAsString(html));
            Document document = Jsoup.parse(html);
            Element element = document.select("form[name=form]").first();
            String urlSubmit = element.attr("action");

            response = function.apply(urlSubmit);

            html = response.getBody();
            log.info("\n" + objectMapper.writeValueAsString(html));
            document = Jsoup.parse(html);
            element = document.select("input[name=cres]").first();
            String decodeCRes = element.attr("value");

            byte[] byteCRes = Base64.getDecoder().decode(decodeCRes);

            CRes cRes = objectMapper.readValue(byteCRes, CRes.class);
            log.info("\n" + objectMapper.writeValueAsString(cRes));
            return cRes;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected Function<String, ResponseEntity<String>> submitWithCorrectPassword() {
        return urlSubmit -> {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("password", "1qwezxc");
            map.add("submit", "Submit");

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

            return restTemplate.postForEntity(urlSubmit, request, String.class);
        };
    }

    protected Function<String, ResponseEntity<String>> submitWithIncorrectPassword() {
        return urlSubmit -> {
            try {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

                MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
                map.add("password", "1zxcqwe");
                map.add("submit", "Submit");

                HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

                ResponseEntity<String> response = restTemplate.postForEntity(urlSubmit, request, String.class);

                String html = response.getBody();
                log.info("\n" + objectMapper.writeValueAsString(html));
                Document document = Jsoup.parse(html);
                Element element = document.select("form[name=form]").first();
                urlSubmit = element.attr("action");

                response = restTemplate.postForEntity(urlSubmit, request, String.class);

                html = response.getBody();
                log.info("\n" + objectMapper.writeValueAsString(html));
                document = Jsoup.parse(html);
                element = document.select("form[name=form]").first();
                urlSubmit = element.attr("action");

                response = restTemplate.postForEntity(urlSubmit, request, String.class);

                html = response.getBody();
                log.info("\n" + objectMapper.writeValueAsString(html));
                document = Jsoup.parse(html);
                element = document.select("a.cancel").first();
                urlSubmit = element.attr("href");

                return restTemplate.getForEntity(urlSubmit, String.class);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }

    protected Function<String, ResponseEntity<String>> justCancel() {
        return urlSubmit -> restTemplate.getForEntity(urlSubmit + "?cancel=true", String.class);
    }

    protected void writeInFileAppend(PArs pArs, TestNumber testNumber) {
        writeInFile(pArs, testNumber, StandardOpenOption.APPEND);
    }

    protected void writeInFileTruncate(PArs pArs, TestNumber testNumber) {
        writeInFile(pArs, testNumber, StandardOpenOption.TRUNCATE_EXISTING);
    }

    private void writeInFile(PArs pArs, TestNumber testNumber, StandardOpenOption standardOpenOption) {
        if (WRITE_DATA_IN_FILE) {
            try {
                String text = String.format("%s (\"%s\": \"%s\")\n",
                        testNumber.getValue(),
                        "dsTransID", pArs.getDsTransID());

                Files.write(Paths.get(resourceLoader.getResource("classpath:__files/nspk_data").getURL().toURI()), text.getBytes(), standardOpenOption);
            } catch (IOException | URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @RequiredArgsConstructor
    public enum TestNumber {

        BRW_PA_1_1("1-1"),
        BRW_PA_1_2("1-2"),
        BRW_PA_1_3("1-3"),
        BRW_PA_1_4("1-4"),
        BRW_PA_1_5("1-5"),
        BRW_PA_1_6("1-6"),
        BRW_PA_1_7("1-7"),
        BRW_PA_1_8("1-8"),
        BRW_PA_1_9("1-9"),
        BRW_PA_1_10("1-10"),
        BRW_NPA_2_1("2-1"),
        BRW_NPA_2_3("2-3"),
        BRW_NPA_2_4("2-4"),
        BRW_NPA_2_5("2-5"),
        BRW_NPA_2_6("2-6"),
        BRW_NPA_2_7("2-7"),
        BRW_NPA_2_9("2-9"),
        BRW_NPA_2_10("2-10"),
        THREE_RI_NPA_5_2("5-2"),
        THREE_RI_NPA_5_3("5-3"),
        THREE_RI_NPA_5_4("5-4"),
        THREE_RI_NPA_5_5("5-5"),
        FSS_PA_6_1("6-1"),
        FSS_PA_6_2("6-2"),
        FSS_PA_6_3("6-3"),
        FSS_PA_6_4("6-4");

        private final String value;

        @JsonValue
        public String getValue() {
            return value;
        }

    }

    protected <T extends Valuable> EnumWrapper<T> getEnumWrapper(T value) {
        EnumWrapper<T> wrapper = new EnumWrapper<>();
        wrapper.setValue(value);
        return wrapper;
    }

    protected <T extends TemporalAccessor> TemporalAccessorWrapper<T> getTemporalAccessorWrapper(T value) {
        TemporalAccessorWrapper<T> wrapper = new TemporalAccessorWrapper<>();
        wrapper.setValue(value);
        return wrapper;
    }

    protected <T> ListWrapper<T> getListWrapper(List<T> value) {
        ListWrapper<T> wrapper = new ListWrapper<>();
        wrapper.setValue(value);
        return wrapper;
    }

    protected String randomId() {
        return UUID.randomUUID().toString();
    }

    protected boolean randomBoolean() {
        return new Random().nextBoolean();
    }

    protected String randomIP() {
        return "192.168.0.1";
    }

    protected String randomUrl() {
        return "https://www.nspk.ru/notify";
    }

    protected String randomMail() {
        return "support@rbk.money";
    }

    protected Map<String, String> getSdkEphemPubKey() {
        return Map.of("crv", "P-256", "kty", "EC", "x", "bbeJ_uHcjTimTa13wahBrYqOOTruiadSImwMh-I9gMs", "y", "3p7hjQDYzw32iEfVFK_ixJ8iECl9bQ8_qmy7m64R4s8");
    }

    protected String randomNumericTwoNumbers() {
        return "66";
    }

    protected TemporalAccessorWrapper<LocalDate> randomLocalDate() {
        return getTemporalAccessorWrapper(LocalDate.now());
    }

    protected TemporalAccessorWrapper<LocalDateTime> randomLocalDateTime() {
        return getTemporalAccessorWrapper(LocalDateTime.now());
    }

    protected String randomString() {
        return randomString(10);
    }

    protected String randomString(int length) {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        return RANDOM.ints(leftLimit, rightLimit + 1)
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    protected String randomNumeric(int length) {
        int leftLimit = 49; // letter 'a'
        int rightLimit = 57; // letter 'z'
        return RANDOM.ints(leftLimit, rightLimit + 1)
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}
