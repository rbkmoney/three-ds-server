package com.rbkmoney.threeds.server.mir;

import com.rbkmoney.threeds.server.config.AbstractMirConfig;
import com.rbkmoney.threeds.server.config.utils.JsonMapper;
import com.rbkmoney.threeds.server.mir.utils.ChallengeFlow;
import com.rbkmoney.threeds.server.mir.utils.FrictionlessFlow;
import com.rbkmoney.threeds.server.mir.utils.challenge.AcsAction;
import com.rbkmoney.threeds.server.mir.utils.challenge.AcsResult;
import com.rbkmoney.threeds.server.mir.utils.challenge.CRes;
import com.rbkmoney.threeds.server.utils.IdGenerator;
import lombok.SneakyThrows;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.stream.Stream;

import static com.rbkmoney.threeds.server.mir.utils.challenge.HtmlExtractor.*;
import static com.rbkmoney.threeds.server.mir.utils.challenge.HttpBuilder.*;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

public class MirPlatformChallengeFlowTest extends AbstractMirConfig {

    @MockBean
    private IdGenerator idGenerator;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JsonMapper jsonMapper;

    @Autowired
    private FrictionlessFlow frictionlessFlow;

    @Autowired
    private ChallengeFlow challengeFlow;

    @Value("${environment.test.ds-url}")
    private String wireMockServerUrl;

    @Autowired
    private RestTemplate testRestTemplate;

    @ParameterizedTest(name = "#{index} - Run Mir platform Challenge Flow test case number {0}, type {2}")
    @ArgumentsSource(MirChallengeFlowArgumentsProvider.class)
    @SneakyThrows
    public void challengeFlowTests(String testCase, String threeDSServerTransID, AcsResult challengeFlowAcsResult) {
        when(idGenerator.generateUUID()).thenReturn(threeDSServerTransID);

        mirFrictionlessFlowTest(testCase);
        mirChallengeFlowTest(testCase, challengeFlowAcsResult);
    }

    private void mirFrictionlessFlowTest(String testCase) throws Exception {
        frictionlessFlow.givenDsStub(testCase);

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

    private void mirChallengeFlowTest(String testCase, AcsResult challengeFlowAcsResult) throws Exception {
        // в оригинальном интеграционном тесте для обработки 3дс сервером rreq/rres сообщений предварительно
        // необходимо самостоятельно запустить обмен creq/cres сообщениями с ACS, в котором 3дс сервер не участвует
        // данные, полученные в этом обмене сообщениями и логика взаимодействия сохранена в этом методе
        // исключение  - вместо acsUrl используется wireMockServerUrl
        processChallengeFlowBetweenAcsAnd3dsRequestor(testCase, challengeFlowAcsResult);

        // rreq/rres flow w/o stub
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

    private void processChallengeFlowBetweenAcsAnd3dsRequestor(String testCase, AcsResult acsResult) throws Exception {
        ResponseEntity<String> response = firstAuthenticationRequestInAcs(testCase);

        String urlSubmit = extractUrlFromHtmlResponseForNextRequestWithHtmlResult(response);

        // в случае интеграционного теста этот urlSubmit использовался бы для выполнения дальнеший запросов к ACS
        assertTrue(urlSubmit.contains("acs.vendorcert.mirconnect.ru"));

        // выполняются опеределенные заранее действия, которые приведут к
        // cres_success, cres_error или cres_after_cancel результату в CRes сообщении
        AcsAction acsAction = resolve(acsResult);
        response = acsAction.apply(testCase);

        String encodeCRes = extractEncodedCResFromHtmlResponse(response);

        CRes cRes = decodeCRes(encodeCRes);

        // проверяется, чтоб после псевдо-обмена сообщениями с ACS, итоговый CRes совпадает с сохраненным
        assertEquals(challengeFlow.readEncodeCRes(testCase), jsonMapper.writeValueAsString(cRes));
    }

    private ResponseEntity<String> firstAuthenticationRequestInAcs(String testCase) {
        challengeFlow.givenAcsStubForFirstAuthenticationRequest(testCase);

        HttpEntity<MultiValueMap<String, String>> request = buildHttpRequestWithInitEncodedCReq(challengeFlow.readEncodeCReq(testCase));

        return testRestTemplate.postForEntity(wireMockServerUrl + "form/authentication", request, String.class);
    }

    private AcsAction resolve(AcsResult acsResult) {
        switch (acsResult) {
            case cres_success:
                return acsActionWithCResSuccessResult();
            case cres_error:
                return acsActionWithCResErrorResult();
            case cres_after_cancel:
                return acsActionWithCResCancelResult();
        }

        throw new IllegalArgumentException(acsResult.toString());
    }

    private AcsAction acsActionWithCResSuccessResult() {
        return (testCase) -> {
            challengeFlow.givenAcsStubAfterAuthenticationWithCResSuccessResult(testCase);

            HttpEntity<MultiValueMap<String, String>> request = buildHttpRequestWithCorrectPassword();

            return testRestTemplate.postForEntity(wireMockServerUrl + "form/cres/success", request, String.class);
        };
    }

    private AcsAction acsActionWithCResErrorResult() {
        return (testCase) -> {
            challengeFlow.givenAcsStubForSecondAuthenticationRequest(testCase);
            challengeFlow.givenAcsStubForThirdAuthenticationRequest(testCase);
            challengeFlow.givenAcsStubAfterAuthenticationWithCardBlockedResult(testCase);
            challengeFlow.givenAcsStubAfterAuthenticationWithCResErrorResult(testCase);

            HttpEntity<MultiValueMap<String, String>> request = buildHttpRequestWithIncorrectPassword();

            ResponseEntity<String> response = testRestTemplate.postForEntity(wireMockServerUrl + "form/authentication/second", request, String.class);

            String urlSubmit = extractUrlFromHtmlResponseForNextRequestWithHtmlResult(response);

            assertTrue(urlSubmit.contains("acs.vendorcert.mirconnect.ru"));

            response = testRestTemplate.postForEntity(wireMockServerUrl + "form/authentication/third", request, String.class);

            urlSubmit = extractUrlFromHtmlResponseForNextRequestWithHtmlResult(response);

            assertTrue(urlSubmit.contains("acs.vendorcert.mirconnect.ru"));

            response = testRestTemplate.postForEntity(wireMockServerUrl + "form/card/blocked", request, String.class);

            urlSubmit = extractUrlFromHtmlResponseForCResErrorResult(response);

            assertTrue(urlSubmit.contains("acs.vendorcert.mirconnect.ru"));

            return testRestTemplate.getForEntity(wireMockServerUrl + "form/cres/error", String.class);
        };
    }

    private AcsAction acsActionWithCResCancelResult() {
        return (testCase) -> {
            challengeFlow.givenAcsStubAfterAuthenticationWithCResCancelResult(testCase);

            return testRestTemplate.getForEntity(wireMockServerUrl + "cres/cancel", String.class);
        };
    }

    private CRes decodeCRes(String encodeCRes) {
        byte[] byteCRes = Base64.getDecoder().decode(encodeCRes);

        return jsonMapper.readValue(byteCRes, CRes.class);
    }

    private static class MirChallengeFlowArgumentsProvider implements ArgumentsProvider {

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
            return Stream.of(
                    Arguments.of("1-6", "167a4912-510f-4cee-b1c6-dad5284ca329", AcsResult.cres_success),
                    Arguments.of("1-7", "d6d53ca5-459e-4ddc-a006-e56e17456399", AcsResult.cres_error),
                    Arguments.of("1-10", "b24ce22f-662b-48d2-8cdc-ed0f5962e890", AcsResult.cres_after_cancel),
                    Arguments.of("2-6", "07a3e943-6f06-4089-90f5-62c19e5d222f", AcsResult.cres_success),
                    Arguments.of("2-7", "bd292166-461f-4f4b-937b-275603b7ce46", AcsResult.cres_error),
                    Arguments.of("2-10", "ccaccb30-72e0-4700-b11b-f2f6673f2145", AcsResult.cres_after_cancel),
                    Arguments.of("6-1", "1dbf4543-1ad2-4f64-bbab-fa2ca5f28270", AcsResult.cres_success),
                    Arguments.of("6-2", "9ebb6698-3fbd-4acf-aa6c-47ab74dc868c", AcsResult.cres_success),
                    Arguments.of("6-3", "d29606f6-3321-4de4-ab04-abd2c9751b27", AcsResult.cres_after_cancel),
                    Arguments.of("6-4", "e58bf997-f11b-4ba3-be5c-134462ed824b", AcsResult.cres_success)
            );
        }
    }
}
