package com.rbkmoney.threeds.server.flow.rbkmoneyplatform.versioning;

import com.rbkmoney.threeds.server.config.AbstractRBKMoneyPlatformConfig;
import com.rbkmoney.threeds.server.config.utils.JsonMapper;
import com.rbkmoney.threeds.server.domain.versioning.ThreeDsVersionRequest;
import com.rbkmoney.threeds.server.domain.versioning.ThreeDsVersionResponse;
import com.rbkmoney.threeds.server.exception.ExternalStorageException;
import com.rbkmoney.threeds.server.service.rbkmoneyplatform.RBKMoneyCardRangesStorageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RBKMoneyVersioningTest extends AbstractRBKMoneyPlatformConfig {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JsonMapper jsonMapper;

    @MockBean
    private RBKMoneyCardRangesStorageService rbkMoneyCardRangesStorageService;

    @Test
    public void shouldReturnThreeDsVersionIfAcctNumberIsSupportedThreeDsVersion() throws Exception {
        String expectedValue = "2.1.0";
        when(rbkMoneyCardRangesStorageService.getThreeDsVersionResponse(anyLong())).thenReturn(
                Optional.of(
                        ThreeDsVersionResponse.builder()
                                .threeDsServerTransId("1")
                                .dsStartProtocolVersion(expectedValue)
                                .build()));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post("/versioning")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(getContent());

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.threeDsServerTransId").exists())
                .andExpect(jsonPath("$.dsStartProtocolVersion").value(expectedValue));
    }

    @Test
    public void shouldReturnNotFoundIfAcctNumberIsUnsupportedThreeDsVersion() throws Exception {
        when(rbkMoneyCardRangesStorageService.getThreeDsVersionResponse(anyLong())).thenReturn(Optional.empty());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post("/versioning")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(getContent());

        mockMvc.perform(request)
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturnBadRequestIfAcctNumberIsInvalid() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post("/versioning")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(ThreeDsVersionRequest.builder().accountNumber("asd").build()));

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnBadRequestIfAcctNumberIsNull() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post("/versioning")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnInternalServerErrorIfErrorsExists() throws Exception {
        when(rbkMoneyCardRangesStorageService.getThreeDsVersionResponse(anyLong()))
                .thenThrow(ExternalStorageException.class);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post("/versioning")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(getContent());

        mockMvc.perform(request)
                .andExpect(status().isInternalServerError());
    }

    private String getContent() {
        return jsonMapper.writeValueAsString(ThreeDsVersionRequest.builder().accountNumber("1").build());
    }
}
