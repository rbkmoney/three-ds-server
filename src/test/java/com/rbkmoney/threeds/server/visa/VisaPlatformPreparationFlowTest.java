package com.rbkmoney.threeds.server.visa;

import com.rbkmoney.threeds.server.config.AbstractVisaConfig;
import com.rbkmoney.threeds.server.utils.IdGenerator;
import com.rbkmoney.threeds.server.visa.utils.PreparationFlow;
import lombok.SneakyThrows;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.stream.Stream;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

public class VisaPlatformPreparationFlowTest extends AbstractVisaConfig {

    @MockBean
    private IdGenerator idGenerator;

    @Autowired
    private MockMvc mockMvc;

    @Value("${environment.test.ds-url}")
    private String wireMockServerUrl;

    @Autowired
    private PreparationFlow preparationFlow;

    @ParameterizedTest(name = "#{index} - Run Visa platform Preparation Flow test case number {0}")
    @ArgumentsSource(VisaPreparationFlowArgumentsProvider.class)
    @SneakyThrows
    public void preparationFlowTests(String testCase, String threeDSServerTransID) {
        when(idGenerator.generateUUID()).thenReturn(threeDSServerTransID);

        visaPreparationFlowTest(testCase);
    }

    @ParameterizedTest(name = "#{index} - Run Visa platform Preparation Flow with second request test case number {0}")
    @ArgumentsSource(VisaPreparationFlowWithSecondRequestArgumentsProvider.class)
    @SneakyThrows
    public void preparationFlowWithSecondRequestTests(String testCase, String testCaseHeader, String threeDSServerTransID) {
        when(idGenerator.generateUUID()).thenReturn(threeDSServerTransID);

        visaPreparationFlowWithChangedHeaderTest(testCase, testCaseHeader);
    }

    private void visaPreparationFlowTest(String testCase) throws Exception {
        preparationFlow.givenDsStub(testCase);

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

    private void visaPreparationFlowWithChangedHeaderTest(String testCase, String testCaseHeader) throws Exception {
        preparationFlow.givenDsStubWithChangedHeader(testCase, testCaseHeader);

        MockHttpServletRequestBuilder prepRequest = MockMvcRequestBuilders
                .post("/sdk")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("x-ul-testcaserun-id", testCaseHeader)
                .content(preparationFlow.requestToThreeDsServer(testCase));

        mockMvc.perform(prepRequest)
                .andDo(print())
                .andExpect(content()
                        .json(preparationFlow.responseFromThreeDsServer(testCase)));
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
                    Arguments.of("3DSS-210-002", "3DSS-210-001", "f7d9f026-b18b-4889-8593-3e665b2e4ca3"),
                    Arguments.of("3DSS-220-002", "3DSS-220-001", "fe115f4d-d010-4d09-8c7c-032545dc381c")
            );
        }
    }
}
