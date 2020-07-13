package com.rbkmoney.threeds.server.mir.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.rbkmoney.threeds.server.config.utils.JsonMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Base64;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@RequiredArgsConstructor
public class ChallengeFlow {

    private final JsonMapper jsonMapper;

    public void givenAcsStubForFirstAuthenticationRequest(String testCase) {
        stubFor(post(urlEqualTo("/form/authentication"))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .withBody(responseFromAcs(testCase, AcsHtmlResponse.html_form_authentication))));
    }

    public void givenAcsStubForSecondAuthenticationRequest(String testCase) {
        stubFor(post(urlEqualTo("/form/authentication/second"))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .withBody(responseFromAcs(testCase, AcsHtmlResponse.html_form_authentication_second))));
    }

    public void givenAcsStubForThirdAuthenticationRequest(String testCase) {
        stubFor(post(urlEqualTo("/form/authentication/third"))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .withBody(responseFromAcs(testCase, AcsHtmlResponse.html_form_authentication_third))));
    }

    public void givenAcsStubAfterAuthenticationWithCardBlockedResult(String testCase) {
        stubFor(post(urlEqualTo("/form/card/blocked"))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .withBody(responseFromAcs(testCase, AcsHtmlResponse.html_form_card_blocked))));
    }

    public void givenAcsStubAfterAuthenticationWithCResSuccessResult(String testCase) {
        stubFor(post(urlEqualTo("/form/cres/success"))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .withBody(responseFromAcs(testCase, AcsHtmlResponse.html_form_cres_success))));
    }

    public void givenAcsStubAfterAuthenticationWithCResErrorResult(String testCase) {
        stubFor(get(urlEqualTo("/form/cres/error"))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .withBody(responseFromAcs(testCase, AcsHtmlResponse.html_form_cres_error))));
    }

    public void givenAcsStubAfterAuthenticationWithCResCancelResult(String testCase) {
        stubFor(get(urlEqualTo("/cres/cancel"))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE)
                        .withBody(responseFromAcs(testCase, AcsHtmlResponse.html_form_cres_after_cancel))));
    }

    public String requestFromDs(String testCase) {
        return readRReq(testCase);
    }

    public String responseToDs(String testCase) {
        return readRRes(testCase);
    }

    public String readEncodeCReq(String testCase) {
        String cReq = readMessage("mir/" + testCase + "/creq.json");
        byte[] bytes = jsonMapper.writeValueAsBytes(cReq);
        return Base64.getEncoder().encodeToString(bytes);
    }

    public String readEncodeCRes(String testCase) {
        JsonNode jsonNode = jsonMapper.readValue(readMessage("mir/" + testCase + "/cres.json"), JsonNode.class);
        return jsonNode.toString();
    }

    private String readRReq(String testCase) {
        return readMessage("mir/" + testCase + "/rreq.json");
    }

    private String readRRes(String testCase) {
        return readMessage("mir/" + testCase + "/rres.json");
    }

    private String responseFromAcs(String testCase, AcsHtmlResponse acsFormType) {
        switch (acsFormType) {
            case html_form_authentication:
                return readHtmlFormAuthentication(testCase);
            case html_form_authentication_second:
                return readHtmlFormAuthenticationSecond(testCase);
            case html_form_authentication_third:
                return readHtmlFormAuthenticationThird(testCase);
            case html_form_card_blocked:
                return readHtmlFormCardBlocked(testCase);
            case html_form_cres_success:
                return readHtmlFormSubmitCorrect(testCase);
            case html_form_cres_error:
                return readHtmlFormSubmitIncorrect(testCase);
            case html_form_cres_after_cancel:
                return readHtmlFormSubmitCancel(testCase);
        }

        throw new IllegalArgumentException(testCase + "." + acsFormType);
    }

    private String readHtmlFormAuthentication(String testCase) {
        return readHtml("mir/" + testCase + "/html_form_authentication.json");
    }

    private String readHtmlFormAuthenticationSecond(String testCase) {
        return readHtml("mir/" + testCase + "/html_form_authentication_second.json");
    }

    private String readHtmlFormAuthenticationThird(String testCase) {
        return readHtml("mir/" + testCase + "/html_form_authentication_third.json");
    }

    private String readHtmlFormCardBlocked(String testCase) {
        return readHtml("mir/" + testCase + "/html_form_card_blocked.json");
    }

    private String readHtmlFormSubmitIncorrect(String testCase) {
        return readHtml("mir/" + testCase + "/html_form_cres_error.json");
    }

    private String readHtmlFormSubmitCorrect(String testCase) {
        return readHtml("mir/" + testCase + "/html_form_cres_success.json");
    }

    private String readHtmlFormSubmitCancel(String testCase) {
        return readHtml("mir/" + testCase + "/html_form_cres_after_cancel.json");
    }

    private String readHtml(String fullPath) {
        return jsonMapper.readFromFile(fullPath, String.class);
    }

    private String readMessage(String fullPath) {
        return jsonMapper.readStringFromFile(fullPath);
    }

    public enum AcsHtmlResponse {

        html_form_authentication,
        html_form_authentication_second,
        html_form_authentication_third,
        html_form_card_blocked,
        html_form_cres_success,
        html_form_cres_error,
        html_form_cres_after_cancel

    }
}
