package com.rbkmoney.threeds.server.flow.rbkmoneyplatform.result;

import com.rbkmoney.threeds.server.config.AbstractRBKMoneyPlatformConfig;
import com.rbkmoney.threeds.server.config.utils.JsonMapper;
import com.rbkmoney.threeds.server.domain.acs.AcsDecConInd;
import com.rbkmoney.threeds.server.domain.device.DeviceChannel;
import com.rbkmoney.threeds.server.domain.result.ThreeDsResultRequest;
import com.rbkmoney.threeds.server.domain.versioning.ThreeDsVersionRequest;
import com.rbkmoney.threeds.server.domain.versioning.ThreeDsVersionResponse;
import com.rbkmoney.threeds.server.dto.ChallengeFlowTransactionInfo;
import com.rbkmoney.threeds.server.exception.ChallengeFlowTransactionInfoNotFoundException;
import com.rbkmoney.threeds.server.exception.ExternalStorageException;
import com.rbkmoney.threeds.server.service.rbkmoneyplatform.RBKMoneyCardRangesStorageService;
import com.rbkmoney.threeds.server.service.rbkmoneyplatform.RBKMoneyChallengeFlowTransactionInfoStorageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class RBKMoneyResultTest extends AbstractRBKMoneyPlatformConfig {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JsonMapper jsonMapper;

    @MockBean
    private RBKMoneyChallengeFlowTransactionInfoStorageService rbkMoneyChallengeFlowTransactionInfoStorageService;

    @Test
    public void shouldReturnThreeDsResultIfThreeDsServerTransIdIsExistInStorage() throws Exception {
        String expectedValue = "eci";
        when(rbkMoneyChallengeFlowTransactionInfoStorageService.getChallengeFlowTransactionInfo(anyString()))
                .thenReturn(ChallengeFlowTransactionInfo.builder()
                        .threeDsServerTransId("1")
                        .deviceChannel(DeviceChannel.BROWSER)
                        .decoupledAuthMaxTime(LocalDateTime.now())
                        .acsDecConInd(AcsDecConInd.DECOUPLED_AUTH_WILL_BE_USED)
                        .dsProviderId("1")
                        .messageVersion("2.1.0")
                        .acsUrl("1")
                        .eci(expectedValue)
                        .authenticationValue("value")
                        .build());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post("/result")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(getContent());

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.threeDsServerTransId").doesNotExist())
                .andExpect(jsonPath("$.authenticationValue").exists())
                .andExpect(jsonPath("$.eci").value(expectedValue));
    }
    @Test
    public void shouldReturnNotFoundIfAuthenticationValueIsNull() throws Exception {
        String expectedValue = "eci";
        when(rbkMoneyChallengeFlowTransactionInfoStorageService.getChallengeFlowTransactionInfo(anyString()))
                .thenReturn(ChallengeFlowTransactionInfo.builder()
                        .threeDsServerTransId("1")
                        .deviceChannel(DeviceChannel.BROWSER)
                        .decoupledAuthMaxTime(LocalDateTime.now())
                        .acsDecConInd(AcsDecConInd.DECOUPLED_AUTH_WILL_BE_USED)
                        .dsProviderId("1")
                        .messageVersion("2.1.0")
                        .acsUrl("1")
                        .eci(expectedValue)
                        .build());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post("/result")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(getContent());

        mockMvc.perform(request)
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturnNotFoundIfTransactionNotFound() throws Exception {
        when(rbkMoneyChallengeFlowTransactionInfoStorageService.getChallengeFlowTransactionInfo(anyString()))
                .thenThrow(ChallengeFlowTransactionInfoNotFoundException.class);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post("/result")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(getContent());

        mockMvc.perform(request)
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturnBadRequestIfThreeDsServerTransIdIsNull() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post("/result")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnInternalServerErrorIfErrorsExists() throws Exception {
        when(rbkMoneyChallengeFlowTransactionInfoStorageService.getChallengeFlowTransactionInfo(anyString()))
                .thenThrow(ExternalStorageException.class);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post("/result")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(getContent());

        mockMvc.perform(request)
                .andExpect(status().isInternalServerError());
    }

    private String getContent() {
        return jsonMapper.writeValueAsString(ThreeDsResultRequest.builder().threeDsServerTransId("1").build());
    }
}
