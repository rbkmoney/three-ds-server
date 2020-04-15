package com.rbkmoney.threeds.server.service;

import com.rbkmoney.threeds.server.TestBase;
import com.rbkmoney.threeds.server.ThreeDsServerApplication;
import com.rbkmoney.threeds.server.config.MockConfig;
import com.rbkmoney.threeds.server.converter.ErroWrapperToErroConverter;
import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.domain.root.emvco.Erro;
import com.rbkmoney.threeds.server.domain.root.proprietary.PArs;
import com.rbkmoney.threeds.server.dto.ValidationResult;
import com.rbkmoney.threeds.server.utils.IdGenerator;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@SpringBootTest(classes = {ThreeDsServerApplication.class, MockConfig.class}, properties = "spring.main.allow-bean-definition-overriding=true")
public class SenderServiceIT extends TestBase {

    @Autowired
    private SenderService senderService;

    @MockBean
    private IdGenerator idGenerator;

    @Autowired
    private ErroWrapperToErroConverter erroWrapperToErroConverter;

    @Before
    public void setUp() throws Exception {
        Mockito.when(idGenerator.generateUUID()).thenReturn("ab4c9b80-adcd-4421-af27-9549dc6c2f4b");
    }

    @Test
    public void happyPathAReqScenario() throws IOException {
        givenAReqSuccessResponse();

        Message pArq = readMessageFromFile("happy-path-pArq.json");

        Message pArs = senderService.sendToDs(pArq);

        assertEquals(readMessageFromFile("happy-path-pArs.json"), pArs);
        assertTrue(pArs instanceof PArs);
    }

    @Test
    public void errorPathAReqScenario() throws IOException {
        givenAReqErrorResponse();

        Message pArq = readMessageFromFile("happy-path-pArq.json");

        Message error = senderService.sendToDs(pArq);

        assertEquals(erroWrapperToErroConverter.convert(ValidationResult.success(readMessageFromFile("error-path-Erro.json"))), error);
        assertTrue(error instanceof Erro);
    }
}
