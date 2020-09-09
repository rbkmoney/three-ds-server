package com.rbkmoney.threeds.server.mir;

import com.rbkmoney.threeds.server.config.AbstractMirPlatformConfig;
import com.rbkmoney.threeds.server.mir.utils.PreparationFlow;
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

import java.util.stream.Stream;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

public class MirPlatformPreparationFlowTest extends AbstractMirPlatformConfig {

    @MockBean
    private IdGenerator idGenerator;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PreparationFlow preparationFlow;

    @Value("${environment.test.ds-url}")
    private String wireMockServerUrl;

    @ParameterizedTest(name = "#{index} - Run Mir platform Preparation Flow test case number {0}")
    @ArgumentsSource(MirPreparationFlowArgumentsProvider.class)
    @SneakyThrows
    public void preparationFlowTests(String testCase, String threeDSServerTransID) {
        when(idGenerator.generateUUID()).thenReturn(threeDSServerTransID);

        mirPreparationFlowTest(testCase);
    }

    private void mirPreparationFlowTest(String testCase) throws Exception {
        preparationFlow.givenDsStub(testCase);

        MockHttpServletRequestBuilder request = post("/sdk")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("x-ul-testcaserun-id", testCase)
                .content(preparationFlow.requestToThreeDsServer(testCase));

        mockMvc.perform(request)
                .andDo(print())
                .andExpect(content()
                        .json(preparationFlow.responseFromThreeDsServer(testCase)));
    }


    private static class MirPreparationFlowArgumentsProvider implements ArgumentsProvider {

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
            return Stream.of(
                    Arguments.of("1-8", "9bdfe2dd-d57b-453f-93a7-995701330872")
            );
        }
    }
}
