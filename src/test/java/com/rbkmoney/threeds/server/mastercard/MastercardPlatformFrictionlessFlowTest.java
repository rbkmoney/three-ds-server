package com.rbkmoney.threeds.server.mastercard;

import com.rbkmoney.threeds.server.config.AbstractMastercardConfig;
import com.rbkmoney.threeds.server.mastercard.utils.FrictionlessFlow;
import com.rbkmoney.threeds.server.utils.IdGenerator;
import lombok.SneakyThrows;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.stream.Stream;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

public class MastercardPlatformFrictionlessFlowTest extends AbstractMastercardConfig {

    @MockBean
    private IdGenerator idGenerator;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FrictionlessFlow frictionlessFlow;

    @ParameterizedTest(name = "#{index} - Run Mastercard platform Frictionless Flow test case number {0}")
    @ArgumentsSource(MastercardFrictionlessFlowArgumentsProvider.class)
    @SneakyThrows
    public void frictionlessFlowTests(String testCase, String threeDSServerTransID) {
        when(idGenerator.generateUUID()).thenReturn(threeDSServerTransID);

        mastercardFrictionlessFlowTest(testCase);
    }

    private void mastercardFrictionlessFlowTest(String testCase) throws Exception {
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


    private static class MastercardFrictionlessFlowArgumentsProvider implements ArgumentsProvider {

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
            return Stream.of(
                    Arguments.of("TC_SERVER_00001_001", "a90b2aed-5eee-49ab-b131-2c173656e141"),
                    Arguments.of("TC_SERVER_00001_002", "6a70c589-b08e-4f94-92ea-87d1be8d8840"),
                    Arguments.of("TC_SERVER_00002_001", "852ded38-7fc6-4d85-a399-582fef801952"),
                    Arguments.of("TC_SERVER_00002_002", "ac24eaa1-9705-4156-92f8-3e6e9b6be98c"),
                    Arguments.of("TC_SERVER_00003_001", "d9fe605f-b13d-443d-9659-b721a95d4b53"),
                    Arguments.of("TC_SERVER_00004_001", "a5086dea-c2e1-44d6-9d85-290d6b9a0e2a"),
                    Arguments.of("TC_SERVER_00004_002", "6be8fd3c-f607-4e72-817d-968035d557ee"),
                    Arguments.of("TC_SERVER_00005_001", "465ed775-f1ed-43c3-9be5-25d975ea5b6e"),
                    Arguments.of("TC_SERVER_00005_002", "1dc4e7b5-f1f1-4d19-938a-8b1fb6df1692"),
                    Arguments.of("TC_SERVER_00006_001", "028faf24-dfc9-4deb-a247-c4245771144d"),
                    Arguments.of("TC_SERVER_00007_001", "db9f1294-8d77-48cc-8be8-c74ef5ab66d9"),
                    Arguments.of("TC_SERVER_00007_002", "badf1131-3ad0-43e3-afbc-7ca7ef346598"),
                    Arguments.of("TC_SERVER_00008_001", "7e73cbf7-bb26-4713-be1b-f00c4c69401b"),
                    Arguments.of("TC_SERVER_00008_002", "fa2603a7-5049-4374-b8f7-b824b7904c99"),
                    Arguments.of("TC_SERVER_00009_001", "5efc3552-aa16-40b8-910a-1a67df598a4b"),
                    Arguments.of("TC_SERVER_00009_002", "c55195b6-b4a1-4a0b-a0f5-0c7ac8c07084"),
                    Arguments.of("TC_SERVER_00010_001", "658d5630-56ca-43d8-81e1-a43e9b14ab08"),
                    Arguments.of("TC_SERVER_00010_002", "d3565012-9aba-4607-86c2-6248272ab887"),
                    Arguments.of("TC_SERVER_00011_001", "6f8ebd84-4dc0-4a2c-a783-3c7e180736a9")
            );
        }
    }
}
