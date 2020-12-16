package com.rbkmoney.threeds.server.flow.rbkmoneyplatform.preparation.happycase;

import com.rbkmoney.damsel.threeds.server.storage.CardRangesStorageSrv;
import com.rbkmoney.damsel.threeds.server.storage.PreparationFlowInitializerSrv;
import com.rbkmoney.threeds.server.config.AbstractRBKMoneyPlatformSchedulerConfig;
import com.rbkmoney.threeds.server.config.utils.JsonMapper;
import com.rbkmoney.threeds.server.utils.IdGenerator;
import org.awaitility.Duration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.concurrent.TimeUnit;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.awaitility.Awaitility.await;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

public class RBKMoneyPreparationSchedulerFlowTest extends AbstractRBKMoneyPlatformSchedulerConfig {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JsonMapper jsonMapper;

    @MockBean
    private IdGenerator idGenerator;

    @MockBean
    private CardRangesStorageSrv.Iface cardRangesStorageClient;

    @MockBean
    private PreparationFlowInitializerSrv.Iface preparationFlowInitializerClient;

    @Test
    public void cronTest() throws Exception {
        // call PreparationFlowInitializer for three-ds-server-storage on application start up
        verify(preparationFlowInitializerClient, times(2)).initRBKMoneyPreparationFlow(any());

        // call PreparationFlowInitializer for three-ds-server-storage on cron schedule
        // cron = */5 * * * * ? (every 5 seconds)
        await()
                .pollDelay(Duration.ZERO)
                .pollInterval(new Duration(500, TimeUnit.MILLISECONDS))
                .atMost(new Duration(9, TimeUnit.SECONDS))
                .untilAsserted(() -> verify(preparationFlowInitializerClient, times(4)).initRBKMoneyPreparationFlow(any()));
    }

    @Test
    void preparationFlowTest() throws Exception {
        String testCase = "e8db9820-63b7-43ce-8831-36ce81a2e313";
        String path = "flow/rbkmoneyplatform/preparation/happycase/scheduler/";

        when(idGenerator.generateUUID()).thenReturn(testCase);
        when(cardRangesStorageClient.isStorageEmpty(anyString())).thenReturn(true);

        // DS return PRes on PReq request
        stubFor(post(urlEqualTo("/visa/DS2/authenticate"))
                .withRequestBody(equalToJson(jsonMapper.readStringFromFile(path + "preq.json"), true, true))
                .willReturn(
                        aResponse()
                                .withStatus(HttpStatus.OK.value())
                                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                                .withBody(jsonMapper.readStringFromFile(path + "pres.json"))));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post("/sdk")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonMapper.readStringFromFile(path + "rpq.json"));

        // three-ds-server-storage handles PreparationFlowInitializer and starts PreparationFlow with three-ds-server
        mockMvc.perform(request)
                .andExpect(content()
                        .json(jsonMapper.readStringFromFile(path + "rps.json")));


        verify(cardRangesStorageClient, times(1)).isStorageEmpty(any());
        verify(cardRangesStorageClient, times(1)).updateCardRanges(any());
    }
}