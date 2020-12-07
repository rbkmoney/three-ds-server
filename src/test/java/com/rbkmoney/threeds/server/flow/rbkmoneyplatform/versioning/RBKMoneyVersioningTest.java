package com.rbkmoney.threeds.server.flow.rbkmoneyplatform.versioning;

import com.rbkmoney.threeds.server.config.AbstractRBKMoneyPlatformConfig;
import com.rbkmoney.threeds.server.domain.versioning.ThreeDsVersion;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class RBKMoneyVersioningTest extends AbstractRBKMoneyPlatformConfig {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RBKMoneyCardRangesStorageService rbkMoneyCardRangesStorageService;

    @Test
    public void shouldReturnThreeDsVersionIfAcctNumberIsSupportedThreeDsVersion() throws Exception {
        String expectedValue = "2.1.0";
        when(rbkMoneyCardRangesStorageService.getThreeDsVersion(anyLong())).thenReturn(
                Optional.of(
                        ThreeDsVersion.builder()
                                .threeDsServerTransId("1")
                                .dsStartProtocolVersion(expectedValue)
                                .build()));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get("/versioning")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .param("account_number", "1");

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.threeDsServerTransId").exists())
                .andExpect(jsonPath("$.dsStartProtocolVersion").value(expectedValue));
    }

    @Test
    public void shouldReturnNotFoundIfAcctNumberIsUnsupportedThreeDsVersion() throws Exception {
        when(rbkMoneyCardRangesStorageService.getThreeDsVersion(anyLong())).thenReturn(Optional.empty());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get("/versioning")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .param("account_number", "1");

        mockMvc.perform(request)
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturnBadRequestIfAcctNumberIsInvalid() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get("/versioning")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .param("account_number", "asd");

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnBadRequestIfAcctNumberIsNull() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get("/versioning")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnInternalServerErrorIfErrorsExists() throws Exception {
        when(rbkMoneyCardRangesStorageService.getThreeDsVersion(anyLong())).thenThrow(ExternalStorageException.class);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get("/versioning")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .param("account_number", "1");

        mockMvc.perform(request)
                .andExpect(status().isInternalServerError());
    }
}
