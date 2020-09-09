package com.rbkmoney.threeds.server.mir;

import com.rbkmoney.threeds.server.config.AbstractMirPlatformConfig;
import com.rbkmoney.threeds.server.mir.utils.FrictionlessFlow;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.stream.Stream;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

public class MirPlatformFrictionlessFlowTest extends AbstractMirPlatformConfig {

    @MockBean
    private IdGenerator idGenerator;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FrictionlessFlow frictionlessFlow;

    @Value("${environment.test.ds-url}")
    private String wireMockServerUrl;

    @ParameterizedTest(name = "#{index} - Run Mir platform Frictionless Flow test case number {0}")
    @ArgumentsSource(MirFrictionlessFlowArgumentsProvider.class)
    @SneakyThrows
    public void frictionlessFlowTests(String testCase, String threeDSServerTransID) {
        when(idGenerator.generateUUID()).thenReturn(threeDSServerTransID);

        mirFrictionlessFlowTest(testCase);
    }

    private void mirFrictionlessFlowTest(String testCase) throws Exception {
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
}
