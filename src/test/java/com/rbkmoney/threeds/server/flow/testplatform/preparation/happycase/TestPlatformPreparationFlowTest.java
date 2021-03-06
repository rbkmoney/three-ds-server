package com.rbkmoney.threeds.server.flow.testplatform.preparation.happycase;

import com.rbkmoney.threeds.server.config.AbstractTestPlatformConfig;
import com.rbkmoney.threeds.server.config.utils.JsonMapper;
import com.rbkmoney.threeds.server.flow.testplatform.preparation.PreparationFlow;
import com.rbkmoney.threeds.server.service.testplatform.TestPlatformCardRangesStorageService;
import com.rbkmoney.threeds.server.service.testplatform.TestPlatformSerialNumStorageService;
import com.rbkmoney.threeds.server.utils.IdGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

public class TestPlatformPreparationFlowTest extends AbstractTestPlatformConfig {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestPlatformSerialNumStorageService testPlatformSerialNumStorageService;

    @Autowired
    private TestPlatformCardRangesStorageService testPlatformCardRangesStorageService;

    @Autowired
    private JsonMapper jsonMapper;

    @MockBean
    private IdGenerator idGenerator;

    @Test
    public void preparationFlowWithRepeatRequestTest() throws Exception {
        String testCase = "bc9f0b90-1041-47f0-94df-d692170ea0d7";

        when(idGenerator.generateUUID()).thenReturn(testCase);

        // cache is clear
        assertNull(testPlatformSerialNumStorageService.getSerialNum(testCase));
        assertTrue(testPlatformCardRangesStorageService.isInCardRange(testCase, "7654320500000001"));

        String path = "flow/testplatform/preparation/happycase/with-repeat-request/";
        PreparationFlow preparationFlow = new PreparationFlow(jsonMapper, path);

        // first stub
        preparationFlow.givenFirstDsStub(testCase);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post("/sdk")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header("x-ul-testcaserun-id", testCase)
                .content(preparationFlow.requestToThreeDsServer());

        mockMvc.perform(request)
                .andExpect(content()
                        .json(preparationFlow.responseFromThreeDsServer()));

        // now cache is filled, but pan is not in card ranges
        assertEquals("20190411083623719000", testPlatformSerialNumStorageService.getSerialNum(testCase));
        assertFalse(testPlatformCardRangesStorageService.isInCardRange(testCase, "7654320500000001"));

        // second stub
        preparationFlow.givenSecondDsStub(testCase);

        //update content with serialNum value
        request.content(preparationFlow.secondRequestToThreeDsServer());

        mockMvc.perform(request)
                .andExpect(content().json(preparationFlow.responseFromThreeDsServer()));

        // after update, pan is in card ranges
        assertEquals("20190411083625236000", testPlatformSerialNumStorageService.getSerialNum(testCase));
        assertTrue(testPlatformCardRangesStorageService.isInCardRange(testCase, "7654320500000001"));
    }
}
