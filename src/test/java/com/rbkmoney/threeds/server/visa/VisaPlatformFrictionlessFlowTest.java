package com.rbkmoney.threeds.server.visa;

import com.rbkmoney.threeds.server.config.AbstractVisaPlatformConfig;
import com.rbkmoney.threeds.server.utils.IdGenerator;
import com.rbkmoney.threeds.server.visa.utils.FrictionlessFlow;
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

public class VisaPlatformFrictionlessFlowTest extends AbstractVisaPlatformConfig {

    @MockBean
    private IdGenerator idGenerator;

    @Autowired
    private MockMvc mockMvc;

    @Value("${environment.test.ds-url}")
    private String wireMockServerUrl;

    @Autowired
    private FrictionlessFlow frictionlessFlow;

    @ParameterizedTest(name = "#{index} - Run Visa platform Frictionless Flow test case number {0}")
    @ArgumentsSource(VisaFrictionlessFlowArgumentsProvider.class)
    @SneakyThrows
    public void frictionlessFlowTests(String testCase, String threeDSServerTransID) {
        when(idGenerator.generateUUID()).thenReturn(threeDSServerTransID);

        visaFrictionlessFlowTest(testCase);
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
}
