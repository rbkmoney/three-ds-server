package com.rbkmoney.threeds.server.flow.rbkmoneyplatform.preparation.happycase;

import com.rbkmoney.damsel.three_ds_server_storage.CardRangesStorageSrv;
import com.rbkmoney.threeds.server.config.AbstractRBKMoneyPlatformConfig;
import com.rbkmoney.threeds.server.config.utils.JsonMapper;
import com.rbkmoney.threeds.server.flow.rbkmoneyplatform.preparation.PreparationFlow;
import com.rbkmoney.threeds.server.utils.IdGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

public class RBKMoneyPlatformPreparationFlowTest extends AbstractRBKMoneyPlatformConfig {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JsonMapper jsonMapper;

    @MockBean
    private IdGenerator idGenerator;

    @MockBean
    private CardRangesStorageSrv.Iface cardRangesStorageClient;

    @Test
    public void preparationFlowDefaultHandleTest() throws Exception {
        String testCase = "e8db9820-63b7-43ce-8831-36ce81a2e313";
        String path = "flow/rbkmoneyplatform/preparation/happycase/default-handle/";

        when(idGenerator.generateUUID()).thenReturn(testCase);
        when(cardRangesStorageClient.isStorageEmpty(anyString())).thenReturn(true);

        PreparationFlow preparationFlow = new PreparationFlow(jsonMapper, path);

        // stub
        preparationFlow.givenDsStub();

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post("/sdk")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(preparationFlow.requestToThreeDsServer());

        mockMvc.perform(request)
                .andDo(print())
                .andExpect(content()
                        .json(preparationFlow.responseFromThreeDsServer()));
    }
}
