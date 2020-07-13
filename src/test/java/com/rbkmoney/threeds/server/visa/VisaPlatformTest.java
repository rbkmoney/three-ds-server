package com.rbkmoney.threeds.server.visa;

import com.fasterxml.jackson.databind.JsonNode;
import com.rbkmoney.threeds.server.config.AbstractVisaConfig;
import com.rbkmoney.threeds.server.config.utils.JsonMapper;
import com.rbkmoney.threeds.server.utils.IdGenerator;
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
import java.nio.charset.Charset;
import java.util.Base64;
import java.util.stream.Stream;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

public class VisaPlatformTest extends AbstractVisaConfig {

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

    @ParameterizedTest(name = "#{index} - Run Visa platform Frictionless Flow test case number {0}")
    @ArgumentsSource(VisaFrictionlessFlowArgumentsProvider.class)
    @SneakyThrows
    public void frictionlessFlowTests(String testCase, String threeDSServerTransID) {
        when(idGenerator.generateUUID()).thenReturn(threeDSServerTransID);

        visaFrictionlessFlowTest(testCase);
    }

    @ParameterizedTest(name = "#{index} - Run Visa platform Challenge Flow test case number {0}")
    @ArgumentsSource(VisaChallengeFlowArgumentsProvider.class)
    @SneakyThrows
    public void challengeFlowTests(String testCase, String threeDSServerTransID) {
        when(idGenerator.generateUUID()).thenReturn(threeDSServerTransID);

        visaFrictionlessFlowTest(testCase);
        visaChallengeFlowTest(testCase);
    }

    @ParameterizedTest(name = "#{index} - Run Visa platform Preparation Flow test case number {0}")
    @ArgumentsSource(VisaPreparationFlowArgumentsProvider.class)
    @SneakyThrows
    public void preparationFlowTests(String testCase, String threeDSServerTransID) {
        when(idGenerator.generateUUID()).thenReturn(threeDSServerTransID);

        visaPreparationFlowTest(testCase);
    }

    @ParameterizedTest(name = "#{index} - Run Visa platform Preparation Flow with second request test case number {1}")
    @ArgumentsSource(VisaPreparationFlowWithSecondRequestArgumentsProvider.class)
    @SneakyThrows
    public void preparationFlowWithSecondRequestTests(String testCaseFirst, String testCaseSecond, String threeDSServerTransID) {
        when(idGenerator.generateUUID()).thenReturn(threeDSServerTransID);

        visaPreparationFlowSecondRequestTest(testCaseFirst, testCaseSecond);
    }

    private static class VisaFrictionlessFlowArgumentsProvider implements ArgumentsProvider {

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
            return Stream.of(
                    Arguments.of("3DSS-210-101", "5201a899-749a-4300-841b-24a870565b51"),
                    Arguments.of("3DSS-210-102", "7ea39bdc-165a-4eee-9b82-212a303e98ff"),
                    Arguments.of("3DSS-210-103", "13f8213b-78fe-402c-8778-b82480624352"),
                    Arguments.of("3DSS-210-104", "8975ea7b-5878-4251-92ba-7a389ed087cb"),
                    Arguments.of("3DSS-210-105", "c89f9f1b-2e02-494a-a8dd-af1e522cf534"),
                    Arguments.of("3DSS-210-106", "8d550c35-5f17-4cf7-914c-0bb44bf7fd0d"),
                    Arguments.of("3DSS-210-301", "92a762e2-a906-4558-8fda-141ce0b0effe"),
                    Arguments.of("3DSS-210-302", "a458f666-0110-49a0-83c8-1778a3c766fd"),
                    Arguments.of("3DSS-210-401", "c8762558-63ed-4186-9a6e-a23ad871a3bf"),
                    Arguments.of("3DSS-220-101", "228b77c7-b316-4d2b-ad6e-13d0a6474ef4"),
                    Arguments.of("3DSS-220-102", "c871712f-de22-42e4-a56a-98358e5eb238"),
                    Arguments.of("3DSS-220-103", "d4cb22d1-d34c-4238-b23c-ebb386aadc68"),
                    Arguments.of("3DSS-220-104", "a910d180-e0ef-4b0c-a588-f7836fc8217e"),
                    Arguments.of("3DSS-220-105", "822d9c94-3cd6-4c87-8ab9-cffad9d2acf0"),
                    Arguments.of("3DSS-220-106", "a0f65c32-7863-4b87-a106-f4b0ec63b6b0"),
                    Arguments.of("3DSS-220-301", "9d9a991f-dd0a-49ec-83e0-4ce9b3d8344f"),
                    Arguments.of("3DSS-220-401", "cf6b0290-9eee-43c6-b907-3e0e30878ed9"),
                    Arguments.of("3DSS-220-402", "12b3b9fe-8065-4438-aa66-d5b01a730ea9"),
                    Arguments.of("3DSS-220-403", "b6e37059-cbc2-4c72-accb-e338b4375bc8"),
                    Arguments.of("3DSS-220-404", "4fd3d4a2-7f05-430b-9756-b7c922244aa3"),
                    Arguments.of("3DSS-220-502", "0d47de30-1c2b-4587-8859-9582c7f6a39a"),
                    Arguments.of("3DSS-220-503", "53065c21-99d0-47bd-8d89-d1eea3f2e10e"),
                    Arguments.of("3DSS-220-504", "9f6e3d9f-59d6-44fc-a62e-7343da3fce66"),
                    Arguments.of("3DSS-220-505", "685991f6-5e5a-4546-953d-0069ed0eddbd"),
                    Arguments.of("3DSS-220-601", "bb0b289e-22d6-44d2-a056-9201fe4affa0"),
                    Arguments.of("3DSS-220-602", "b6d9e7f1-1ac2-414f-bc65-1cc94bf2a3bd"),
                    Arguments.of("3DSS-220-701", "f47e5a77-a939-4b52-a9d6-4aa9cb5b0873")
            );
        }
    }

    private static class VisaChallengeFlowArgumentsProvider implements ArgumentsProvider {

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
            return Stream.of(
                    Arguments.of("3DSS-220-107", "6c57c18f-e55b-4333-a991-460b04ee0730"),
                    Arguments.of("3DSS-220-501", "721276cb-a700-4cb5-be72-c991f1986f8c"),
                    Arguments.of("3DSS-220-603", "14186090-9566-4b4f-ab1f-9f027a283d8e")
            );
        }
    }

    private static class VisaPreparationFlowArgumentsProvider implements ArgumentsProvider {

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
            return Stream.of(
                    Arguments.of("3DSS-210-001", "f7d9f026-b18b-4889-8593-3e665b2e4ca3"),
                    Arguments.of("3DSS-220-001", "fe115f4d-d010-4d09-8c7c-032545dc381c")
            );
        }
    }

    private static class VisaPreparationFlowWithSecondRequestArgumentsProvider implements ArgumentsProvider {

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
            return Stream.of(
                    Arguments.of("3DSS-210-001", "3DSS-210-002", "f7d9f026-b18b-4889-8593-3e665b2e4ca3"),
                    Arguments.of("3DSS-220-001", "3DSS-220-002", "fe115f4d-d010-4d09-8c7c-032545dc381c")
            );
        }
    }

    private void visaFrictionlessFlowTest(String testCase) throws Exception {
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

    private void visaChallengeFlowTest(String testCase) throws Exception {
        ChallengeFlow challengeFlow = new ChallengeFlow();

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

    private void visaPreparationFlowTest(String testCase) throws Exception {
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

    private void visaPreparationFlowSecondRequestTest(String testCaseFirst, String testCaseSecond) throws Exception {
        PreparationFlow preparationFlow = new PreparationFlow();

        stubFor(post(urlEqualTo("/"))
                .withRequestBody(equalToJson(preparationFlow.requestToDsServer(testCaseSecond), true, true))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .withHeader("x-ul-testcaserun-id", testCaseFirst)
                        .withBody(preparationFlow.responseFromDsServer(testCaseSecond))));

        MockHttpServletRequestBuilder prepRequest = MockMvcRequestBuilders
                .post("/sdk")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("x-ul-testcaserun-id", testCaseFirst)
                .content(preparationFlow.requestToThreeDsServer(testCaseSecond));

        mockMvc.perform(prepRequest)
                .andDo(print())
                .andExpect(content()
                        .json(preparationFlow.responseFromThreeDsServer(testCaseSecond)));
    }

    private String visaRequestorClient(String cReq, String acsUrl) {
        try {
            byte[] bytesCReq = jsonMapper.writeValueAsBytes(cReq);

            String encodeCReq = Base64.getEncoder().encodeToString(bytesCReq);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("creq", encodeCReq);

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

            ResponseEntity<String> response = testRestTemplate.postForEntity(acsUrl, request, String.class);

            String html = response.getBody();
            Document document = Jsoup.parse(html);
            Element element = document.select("input[name=cres]").first();
            String decodeCRes = element.attr("value");

            byte[] byteCRes = Base64.getDecoder().decode(decodeCRes);

            return new String(byteCRes, Charset.defaultCharset());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
            return readMessage("visa/" + testCase + "/parq.json");
        }

        private String readPArs(String testCase) throws IOException {
            return readMessage("visa/" + testCase + "/pars.json");
        }

        private String readAReq(String testCase) throws IOException {
            return readMessage("visa/" + testCase + "/areq.json");
        }

        private String readARes(String testCase) throws IOException {
            return readMessage("visa/" + testCase + "/ares.json");
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

        private String readRReq(String testCase) throws IOException {
            return readMessage("visa/" + testCase + "/rreq.json");
        }

        private String readRRes(String testCase) throws IOException {
            return readMessage("visa/" + testCase + "/rres.json");
        }

        private String readEncodeCReq(String testCase) throws IOException {
            String cReq = readMessage("visa/" + testCase + "/creq.json");
            byte[] bytes = jsonMapper.writeValueAsBytes(cReq);
            return Base64.getEncoder().encodeToString(bytes);
        }

        private String readEncodeCRes(String testCase) throws IOException {
            JsonNode jsonNode = jsonMapper.readValue(readMessage("visa/" + testCase + "/cres.json"), JsonNode.class);
            return jsonNode.toString();
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
            return readMessage("visa/" + testCase + "/pprq.json");
        }

        private String readPPrs(String testCase) throws IOException {
            return readMessage("visa/" + testCase + "/pprs.json");
        }

        private String readPReq(String testCase) throws IOException {
            return readMessage("visa/" + testCase + "/preq.json");
        }

        private String readPRes(String testCase) throws IOException {
            return readMessage("visa/" + testCase + "/pres.json");
        }

        private String readMessage(String fullPath) throws IOException {
            return jsonMapper.readStringFromFile(fullPath);
        }
    }
}
