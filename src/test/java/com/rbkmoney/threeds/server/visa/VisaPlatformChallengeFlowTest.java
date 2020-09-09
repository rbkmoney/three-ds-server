package com.rbkmoney.threeds.server.visa;

import com.rbkmoney.threeds.server.config.AbstractVisaPlatformConfig;
import com.rbkmoney.threeds.server.config.utils.JsonMapper;
import com.rbkmoney.threeds.server.utils.IdGenerator;
import com.rbkmoney.threeds.server.visa.utils.ChallengeFlow;
import com.rbkmoney.threeds.server.visa.utils.FrictionlessFlow;
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
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.Base64;
import java.util.stream.Stream;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

public class VisaPlatformChallengeFlowTest extends AbstractVisaPlatformConfig {

    @MockBean
    private IdGenerator idGenerator;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JsonMapper jsonMapper;

    @Autowired
    private RestTemplate testRestTemplate;

    @Autowired
    private FrictionlessFlow frictionlessFlow;

    @Autowired
    private ChallengeFlow challengeFlow;

    @ParameterizedTest(name = "#{index} - Run Visa platform Challenge Flow test case number {0}")
    @ArgumentsSource(VisaChallengeFlowArgumentsProvider.class)
    @SneakyThrows
    public void challengeFlowTests(String testCase, String threeDSServerTransID) {
        when(idGenerator.generateUUID()).thenReturn(threeDSServerTransID);

        visaFrictionlessFlowTest(testCase);
        visaChallengeFlowTest(testCase);
    }

    private void visaFrictionlessFlowTest(String testCase) throws Exception {
        frictionlessFlow.givenDsStub(testCase);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post("/sdk")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("x-ul-testcaserun-id", testCase)
                .content(frictionlessFlow.requestToThreeDsServer(testCase));

        mockMvc.perform(request)
                .andDo(print())
                .andExpect(content()
                        .json(frictionlessFlow.responseFromThreeDsServer(testCase)));
    }

    private void visaChallengeFlowTest(String testCase) throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post("/ds")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("x-ul-testcaserun-id", testCase)
                .content(challengeFlow.requestFromDs(testCase));

        mockMvc.perform(request)
                .andDo(print())
                .andExpect(content()
                        .json(challengeFlow.responseToDs(testCase)));
    }

    // В оригинальном интеграционном тесте для обработки 3дс сервером rreq/rres сообщений, предварительно,
    // необходимо, самостоятельно, запустить обмен creq/cres сообщениями с ACS, в котором 3дс сервер не участвует.
    // В данном случае метод не используется, тк данные от acs не сохранились
    private String processChallengeFlowBetweenAcsAnd3dsRequestor(String cReq, String acsUrl) {
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
}
