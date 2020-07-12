package com.rbkmoney.threeds.server.mir;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import com.rbkmoney.threeds.server.config.AbstractMirConfig;
import com.rbkmoney.threeds.server.config.utils.JsonMapper;
import com.rbkmoney.threeds.server.utils.IdGenerator;
import lombok.Data;
import lombok.SneakyThrows;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.*;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Base64;
import java.util.stream.Stream;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

public class MirPlatformTest extends AbstractMirConfig {

    @MockBean
    private IdGenerator idGenerator;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JsonMapper jsonMapper;

    @Value("${environment.test.ds-url}")
    private String wireMockServerUrl;

    @Autowired
    private RestTemplate testRestTemplate;

    @ParameterizedTest(name = "#{index} - Run Mir platform Frictionless Flow test case number {0}")
    @ArgumentsSource(MirFrictionlessFlowArgumentsProvider.class)
    @SneakyThrows
    public void frictionlessFlowTests(String testCase, String threeDSServerTransID) {
        when(idGenerator.generateUUID()).thenReturn(threeDSServerTransID);

        mirFrictionlessFlowTest(testCase);
    }

    @ParameterizedTest(name = "#{index} - Run Mir platform Challenge Flow test case number {0}, type {2}")
    @ArgumentsSource(MirChallengeFlowArgumentsProvider.class)
    @SneakyThrows
    public void challengeFlowTests(String testCase, String threeDSServerTransID, ChallengeFormActionType challengeFormActionType) {
        when(idGenerator.generateUUID()).thenReturn(threeDSServerTransID);

        mirFrictionlessFlowTest(testCase);
        mirChallengeFlowTest(testCase, resolve(challengeFormActionType));
    }

    @ParameterizedTest(name = "#{index} - Run Mir platform Preparation Flow test case number {0}")
    @ArgumentsSource(MirPreparationFlowArgumentsProvider.class)
    @SneakyThrows
    public void preparationFlowTests(String testCase, String threeDSServerTransID) {
        when(idGenerator.generateUUID()).thenReturn(threeDSServerTransID);

        mirPreparationFlowTest(testCase);
    }

    private void mirFrictionlessFlowTest(String testCase) throws Exception {
        FrictionlessFlow frictionlessFlow = new FrictionlessFlow();

        stubFor(post(urlEqualTo("/"))
                .withRequestBody(equalToJson(frictionlessFlow.requestToDsServer(testCase), true, true))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .withHeader("x-ul-testcaserun-id", testCase)
                        .withBody(frictionlessFlow.responseFromDsServer(testCase))));

        MockHttpServletRequestBuilder prepRequest = MockMvcRequestBuilders
                .post("/sdk")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("x-ul-testcaserun-id", testCase)
                .content(frictionlessFlow.requestToThreeDsServer(testCase));

        mockMvc.perform(prepRequest)
                .andDo(print())
                .andExpect(content()
                        .json(frictionlessFlow.responseFromThreeDsServer(testCase)));
    }

    private void mirPreparationFlowTest(String testCase) throws Exception {
        PreparationFlow preparationFlow = new PreparationFlow();

        stubFor(post(urlEqualTo("/"))
                .withRequestBody(equalToJson(preparationFlow.requestToDsServer(testCase), true, true))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .withHeader("x-ul-testcaserun-id", testCase)
                        .withBody(preparationFlow.responseFromDsServer(testCase))));

        MockHttpServletRequestBuilder prepRequest = MockMvcRequestBuilders
                .post("/sdk")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("x-ul-testcaserun-id", testCase)
                .content(preparationFlow.requestToThreeDsServer(testCase));

        mockMvc.perform(prepRequest)
                .andDo(print())
                .andExpect(content()
                        .json(preparationFlow.responseFromThreeDsServer(testCase)));
    }

    private void mirChallengeFlowTest(String testCase, ChallengeFormAction challengeFormAction) throws Exception {
        ChallengeFlow challengeFlow = new ChallengeFlow();

        stubFor(post(urlEqualTo("/form/authentication"))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .withBody(challengeFlow.responseFromAcs(testCase, AcsFormType.html_form_authentication))));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("creq", challengeFlow.readEncodeCReq(testCase));

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        ResponseEntity<String> response = testRestTemplate.postForEntity(wireMockServerUrl + "form/authentication", request, String.class);

        String html = response.getBody();
        Document document = Jsoup.parse(html);
        Element element = document.select("form[name=form]").first();
        String urlSubmit = element.attr("action");

        assertTrue(urlSubmit.contains("acs.vendorcert.mirconnect.ru"));

        response = challengeFormAction.apply(testCase, challengeFlow);

        html = response.getBody();
        document = Jsoup.parse(html);
        element = document.select("input[name=cres]").first();
        String encodeCRes = element.attr("value");

        byte[] byteCRes = Base64.getDecoder().decode(encodeCRes);

        CRes cRes = jsonMapper.readValue(byteCRes, CRes.class);

        assertEquals(challengeFlow.readEncodeCRes(testCase), jsonMapper.writeValueAsString(cRes));

        MockHttpServletRequestBuilder prepRequest = MockMvcRequestBuilders
                .post("/ds")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("x-ul-testcaserun-id", testCase)
                .content(challengeFlow.requestFromDs(testCase));

        mockMvc.perform(prepRequest)
                .andDo(print())
                .andExpect(content()
                        .json(challengeFlow.responseToDs(testCase)));
    }

    private ChallengeFormAction submitWithCorrectPassword() {
        return (testCase, challengeFlow) -> {
            stubFor(post(urlEqualTo("/form/submit/correct"))
                    .willReturn(aResponse()
                            .withStatus(HttpStatus.OK.value())
                            .withHeader("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE)
                            .withBody(challengeFlow.responseFromAcs(testCase, AcsFormType.html_form_submit_correct))));

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("password", "1qwezxc");
            map.add("submit", "Submit");

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

            return testRestTemplate.postForEntity(wireMockServerUrl + "form/submit/correct", request, String.class);
        };
    }

    private ChallengeFormAction submitWithIncorrectPassword() {
        return (testCase, challengeFlow) -> {
            try {
                stubFor(post(urlEqualTo("/form/authentication/second"))
                        .willReturn(aResponse()
                                .withStatus(HttpStatus.OK.value())
                                .withHeader("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE)
                                .withBody(challengeFlow.responseFromAcs(testCase, AcsFormType.html_form_authentication_second))));
                stubFor(post(urlEqualTo("/form/authentication/third"))
                        .willReturn(aResponse()
                                .withStatus(HttpStatus.OK.value())
                                .withHeader("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE)
                                .withBody(challengeFlow.responseFromAcs(testCase, AcsFormType.html_form_authentication_third))));
                stubFor(post(urlEqualTo("/form/card/blocked"))
                        .willReturn(aResponse()
                                .withStatus(HttpStatus.OK.value())
                                .withHeader("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE)
                                .withBody(challengeFlow.responseFromAcs(testCase, AcsFormType.html_form_card_blocked))));
                stubFor(get(urlEqualTo("/form/submit/incorrect"))
                        .willReturn(aResponse()
                                .withStatus(HttpStatus.OK.value())
                                .withHeader("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE)
                                .withBody(challengeFlow.responseFromAcs(testCase, AcsFormType.html_form_submit_incorrect))));

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

                MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
                map.add("password", "1zxcqwe");
                map.add("submit", "Submit");

                HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

                ResponseEntity<String> response = testRestTemplate.postForEntity(wireMockServerUrl + "/form/authentication/second", request, String.class);

                String html = response.getBody();
                Document document = Jsoup.parse(html);
                Element element = document.select("form[name=form]").first();
                String urlSubmit = element.attr("action");

                assertTrue(urlSubmit.contains("acs.vendorcert.mirconnect.ru"));

                response = testRestTemplate.postForEntity(wireMockServerUrl + "/form/authentication/third", request, String.class);

                html = response.getBody();
                document = Jsoup.parse(html);
                element = document.select("form[name=form]").first();
                urlSubmit = element.attr("action");

                assertTrue(urlSubmit.contains("acs.vendorcert.mirconnect.ru"));

                response = testRestTemplate.postForEntity(wireMockServerUrl + "/form/card/blocked", request, String.class);

                html = response.getBody();
                document = Jsoup.parse(html);
                element = document.select("a.cancel").first();
                urlSubmit = element.attr("href");

                assertTrue(urlSubmit.contains("acs.vendorcert.mirconnect.ru"));

                return testRestTemplate.getForEntity(wireMockServerUrl + "/form/submit/incorrect", String.class);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }

    private ChallengeFormAction justCancel() {
        return (testCase, challengeFlow) -> {
            stubFor(get(urlEqualTo("/cancel"))
                    .willReturn(aResponse()
                            .withStatus(HttpStatus.OK.value())
                            .withHeader("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE)
                            .withBody(challengeFlow.responseFromAcs(testCase, AcsFormType.html_form_cancel))));

            return testRestTemplate.getForEntity(wireMockServerUrl + "/cancel", String.class);
        };
    }

    private static class MirFrictionlessFlowArgumentsProvider implements ArgumentsProvider {

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
            return Stream.of(
                    Arguments.of("1-1", "e369b015-7d65-4398-86f2-0115d912d296"),
                    Arguments.of("1-2", "17354aa2-4bf9-4281-9612-7443d0c33102"),
                    Arguments.of("1-3", "f964b817-d25c-4cbe-8929-e6f09bb9cf64"),
                    Arguments.of("1-4", "9bc607de-7257-447e-b321-2f6c9e593bc2"),
                    Arguments.of("1-5", "6cdb2497-4d1f-474f-b854-ec1cc1ae0198"),
                    Arguments.of("1-9", "765d2c96-4102-4c5a-99b2-598f530c3bb1"),
                    Arguments.of("2-1", "cf9f551a-a0be-41d9-b49f-4e406ac65b45"),
                    Arguments.of("2-3", "7a63aa44-a380-4149-aab4-f739404d00b4"),
                    Arguments.of("2-4", "d89a144f-7d0e-4bb5-8dc7-315668aa0d13"),
                    Arguments.of("2-5", "846ed727-b0cb-4b13-98c1-32183ec8a5ce"),
                    Arguments.of("2-9", "30c30f38-edbd-42b0-a5af-46372bbb02c7"),
                    Arguments.of("5-2", "647f9aec-5b80-4ff4-97bd-1614ecc3e71f"),
                    Arguments.of("5-3", "9910b79f-9cd9-44f9-9ea6-d1e3717a3d63"),
                    Arguments.of("5-4", "66be9376-fb63-4adb-8bfa-fbfec8e3f585"),
                    Arguments.of("5-5", "e8d82922-fb65-47c1-9c7a-dc301dfcb604")
            );
        }
    }

    private static class MirChallengeFlowArgumentsProvider implements ArgumentsProvider {

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
            return Stream.of(
                    Arguments.of("1-6", "167a4912-510f-4cee-b1c6-dad5284ca329", ChallengeFormActionType.correct),
                    Arguments.of("1-7", "d6d53ca5-459e-4ddc-a006-e56e17456399", ChallengeFormActionType.incorrect),
                    Arguments.of("1-10", "b24ce22f-662b-48d2-8cdc-ed0f5962e890", ChallengeFormActionType.cancel),
                    Arguments.of("2-6", "07a3e943-6f06-4089-90f5-62c19e5d222f", ChallengeFormActionType.correct),
                    Arguments.of("2-7", "bd292166-461f-4f4b-937b-275603b7ce46", ChallengeFormActionType.incorrect),
                    Arguments.of("2-10", "ccaccb30-72e0-4700-b11b-f2f6673f2145", ChallengeFormActionType.cancel),
                    Arguments.of("6-1", "1dbf4543-1ad2-4f64-bbab-fa2ca5f28270", ChallengeFormActionType.correct),
                    Arguments.of("6-2", "9ebb6698-3fbd-4acf-aa6c-47ab74dc868c", ChallengeFormActionType.correct),
                    Arguments.of("6-3", "d29606f6-3321-4de4-ab04-abd2c9751b27", ChallengeFormActionType.cancel),
                    Arguments.of("6-4", "e58bf997-f11b-4ba3-be5c-134462ed824b", ChallengeFormActionType.correct)
            );
        }
    }

    private static class MirPreparationFlowArgumentsProvider implements ArgumentsProvider {

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
            return Stream.of(
                    Arguments.of("1-8", "9bdfe2dd-d57b-453f-93a7-995701330872")
            );
        }
    }

    private ChallengeFormAction resolve(ChallengeFormActionType challengeFormActionType) {
        switch (challengeFormActionType) {
            case correct:
                return submitWithCorrectPassword();
            case incorrect:
                return submitWithIncorrectPassword();
            case cancel:
                return justCancel();
        }

        throw new IllegalArgumentException(challengeFormActionType.toString());
    }

    private class FrictionlessFlow {

        private String requestToThreeDsServer(String testCase) throws IOException {
            return readPArq(testCase);
        }

        private String responseFromThreeDsServer(String testCase) throws IOException {
            return readPArs(testCase);
        }

        private String requestToDsServer(String testCase) throws IOException {
            return readAReq(testCase);
        }

        private String responseFromDsServer(String testCase) throws IOException {
            return readARes(testCase);
        }

        private String readPArq(String testCase) throws IOException {
            return readMessage("mir/" + testCase + "/parq.json");
        }

        private String readPArs(String testCase) throws IOException {
            return readMessage("mir/" + testCase + "/pars.json");
        }

        private String readAReq(String testCase) throws IOException {
            return readMessage("mir/" + testCase + "/areq.json");
        }

        private String readARes(String testCase) throws IOException {
            return readMessage("mir/" + testCase + "/ares.json");
        }

        private String readMessage(String fullPath) throws IOException {
            return jsonMapper.readStringFromFile(fullPath);
        }
    }

    private class ChallengeFlow {

        private String requestFromDs(String testCase) throws IOException {
            return readRReq(testCase);
        }

        private String responseToDs(String testCase) throws IOException {
            return readRRes(testCase);
        }

        private String responseFromAcs(String testCase, AcsFormType acsFormType) throws IOException {
            switch (acsFormType) {
                case html_form_authentication:
                    return readHtmlFormAuthentication(testCase);
                case html_form_authentication_second:
                    return readHtmlFormAuthenticationSecond(testCase);
                case html_form_authentication_third:
                    return readHtmlFormAuthenticationThird(testCase);
                case html_form_card_blocked:
                    return readHtmlFormCardBlocked(testCase);
                case html_form_submit_correct:
                    return readHtmlFormSubmitCorrect(testCase);
                case html_form_submit_incorrect:
                    return readHtmlFormSubmitIncorrect(testCase);
                case html_form_cancel:
                    return readHtmlFormSubmitCancel(testCase);
            }

            throw new IllegalArgumentException(testCase + "." + acsFormType);
        }

        private String readRReq(String testCase) throws IOException {
            return readMessage("mir/" + testCase + "/rreq.json");
        }

        private String readRRes(String testCase) throws IOException {
            return readMessage("mir/" + testCase + "/rres.json");
        }

        private String readEncodeCReq(String testCase) throws IOException {
            String cReq = readMessage("mir/" + testCase + "/creq.json");
            byte[] bytes = jsonMapper.writeValueAsBytes(cReq);
            return Base64.getEncoder().encodeToString(bytes);
        }

        private String readEncodeCRes(String testCase) throws IOException {
            JsonNode jsonNode = jsonMapper.readValue(readMessage("mir/" + testCase + "/cres.json"), JsonNode.class);
            return jsonNode.toString();
        }

        private String readHtmlFormAuthentication(String testCase) throws IOException {
            return readHtml("mir/" + testCase + "/html_form_authentication.json");
        }

        private String readHtmlFormAuthenticationSecond(String testCase) throws IOException {
            return readHtml("mir/" + testCase + "/html_form_authentication_second.json");
        }

        private String readHtmlFormAuthenticationThird(String testCase) throws IOException {
            return readHtml("mir/" + testCase + "/html_form_authentication_third.json");
        }

        private String readHtmlFormCardBlocked(String testCase) throws IOException {
            return readHtml("mir/" + testCase + "/html_form_card_blocked.json");
        }

        private String readHtmlFormSubmitIncorrect(String testCase) throws IOException {
            return readHtml("mir/" + testCase + "/html_form_submit_incorrect.json");
        }

        private String readHtmlFormSubmitCorrect(String testCase) throws IOException {
            return readHtml("mir/" + testCase + "/html_form_submit_correct.json");
        }

        private String readHtmlFormSubmitCancel(String testCase) throws IOException {
            return readHtml("mir/" + testCase + "/html_form_cancel.json");
        }

        private String readHtml(String fullPath) throws IOException {
            return jsonMapper.readFromFile(fullPath, String.class);
        }

        private String readMessage(String fullPath) throws IOException {
            return jsonMapper.readStringFromFile(fullPath);
        }
    }

    private class PreparationFlow {

        private String requestToThreeDsServer(String testCase) throws IOException {
            return readPPrq(testCase);
        }

        private String responseFromThreeDsServer(String testCase) throws IOException {
            return readPPrs(testCase);
        }

        private String requestToDsServer(String testCase) throws IOException {
            return readPReq(testCase);
        }

        private String responseFromDsServer(String testCase) throws IOException {
            return readPRes(testCase);
        }

        private String readPPrq(String testCase) throws IOException {
            return readMessage("mir/" + testCase + "/pprq.json");
        }

        private String readPPrs(String testCase) throws IOException {
            return readMessage("mir/" + testCase + "/pprs.json");
        }

        private String readPReq(String testCase) throws IOException {
            return readMessage("mir/" + testCase + "/preq.json");
        }

        private String readPRes(String testCase) throws IOException {
            return readMessage("mir/" + testCase + "/pres.json");
        }

        private String readMessage(String fullPath) throws IOException {
            return jsonMapper.readStringFromFile(fullPath);
        }
    }

    private interface ChallengeFormAction {

        ResponseEntity<String> apply(String testCase, ChallengeFlow challengeFlow) throws Exception;

    }

    private enum ChallengeFormActionType {

        correct,
        incorrect,
        cancel

    }

    private enum AcsFormType {

        html_form_authentication,
        html_form_authentication_second,
        html_form_authentication_third,
        html_form_card_blocked,
        html_form_submit_incorrect,
        html_form_submit_correct,
        html_form_cancel

    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(value = JsonInclude.Include.NON_ABSENT)
    public static class CRes {

        private String acsTransID;
        private String acsUiType;
        private String acsHTML;
        private String acsCounterAtoS;
        private String challengeCompletionInd;
        private String messageType;
        private String messageVersion;
        private String sdkTransID;
        private String threeDSServerTransID;
        private String transStatus;

    }
}
