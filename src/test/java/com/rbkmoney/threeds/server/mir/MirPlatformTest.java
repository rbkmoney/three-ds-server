package com.rbkmoney.threeds.server.mir;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import com.rbkmoney.threeds.server.config.AbstractMirConfig;
import com.rbkmoney.threeds.server.config.utils.JsonMapper;
import com.rbkmoney.threeds.server.utils.IdGenerator;
import lombok.Data;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.Test;
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

    @Test
    public void test11() throws Exception {
        when(idGenerator.generateUUID()).thenReturn("e369b015-7d65-4398-86f2-0115d912d296");

        mirFrictionlessFlowTest("1-1");
    }

    @Test
    public void test12() throws Exception {
        when(idGenerator.generateUUID()).thenReturn("17354aa2-4bf9-4281-9612-7443d0c33102");

        mirFrictionlessFlowTest("1-2");
    }

    @Test
    public void test16() throws Exception {
        when(idGenerator.generateUUID()).thenReturn("167a4912-510f-4cee-b1c6-dad5284ca329");

        mirFrictionlessFlowTest("1-6");
        mirChallengeFlowTest("1-6");
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

    private void mirChallengeFlowTest(String testCase) throws Exception {
        ChallengeFlow challengeFlow = new ChallengeFlow();

        stubFor(post(urlEqualTo("/form/authentication"))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .withBody(challengeFlow.responseFromAcsFormAuthentication(testCase))));

        stubFor(post(urlEqualTo("/form/submit/correct"))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .withBody(challengeFlow.responseFromAcsFormSubmitCorrect(testCase))));

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

        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        map = new LinkedMultiValueMap<>();
        map.add("password", "1qwezxc");
        map.add("submit", "Submit");

        request = new HttpEntity<>(map, headers);

        response = testRestTemplate.postForEntity(wireMockServerUrl + "form/submit/correct", request, String.class);

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

        private String responseFromAcsFormAuthentication(String testCase) throws IOException {
            return readHtmlFormAuthentication(testCase);
        }

        private String responseFromAcsFormSubmitCorrect(String testCase) throws IOException {
            return readHtmlFormSubmitCorrect(testCase);
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
            return readHtml("mir/" + testCase + "/html1.json");
        }

        private String readHtmlFormSubmitCorrect(String testCase) throws IOException {
            return readHtml("mir/" + testCase + "/html2.json");
        }

        private String readHtml(String fullPath) throws IOException {
            return jsonMapper.readFromFile(fullPath, String.class);
        }

        private String readMessage(String fullPath) throws IOException {
            return jsonMapper.readStringFromFile(fullPath);
        }
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
