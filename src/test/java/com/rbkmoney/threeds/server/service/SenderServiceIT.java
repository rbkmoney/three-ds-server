package com.rbkmoney.threeds.server.service;

import com.rbkmoney.threeds.server.TestBase;
import com.rbkmoney.threeds.server.ThreeDsServerApplication;
import com.rbkmoney.threeds.server.client.DsClient;
import com.rbkmoney.threeds.server.config.DirectoryServerProviderHolder;
import com.rbkmoney.threeds.server.config.MockConfig;
import com.rbkmoney.threeds.server.converter.ErroWrapperToErroConverter;
import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.domain.root.emvco.Erro;
import com.rbkmoney.threeds.server.domain.root.proprietary.PArs;
import com.rbkmoney.threeds.server.dto.ValidationResult;
import com.rbkmoney.threeds.server.utils.IdGenerator;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@SpringBootTest(
        classes = {ThreeDsServerApplication.class, MockConfig.class},
        properties = "spring.main.allow-bean-definition-overriding=true")
public class SenderServiceIT extends TestBase {

    @Autowired
    private SenderService senderService;

    @Autowired
    private DsClient testDsClient;

    @Autowired
    private ErroWrapperToErroConverter erroWrapperToErroConverter;

    @MockBean
    private IdGenerator idGenerator;

    @MockBean
    private DirectoryServerProviderHolder providerHolder;

    @Before
    public void setUp() {
        when(idGenerator.generateUUID())
                .thenReturn("ab4c9b80-adcd-4421-af27-9549dc6c2f4b");
        when(providerHolder.getDsClient())
                .thenReturn(testDsClient);
    }

    @Test
    public void happyPathAReqScenario() throws IOException {
        givenAReqSuccessResponse();

        Message pArq = readMessageFromFile("happy-path-pArq.json");
        Message pArs = senderService.sendToDs(pArq);

        Message expected = readMessageFromFile("happy-path-pArs.json");
        assertEquals(expected, pArs);
        assertTrue(pArs instanceof PArs);
    }

    @Test
    public void errorPathAReqScenario() throws IOException {
        givenAReqErrorResponse();

        Message pArq = readMessageFromFile("happy-path-pArq.json");
        Message error = senderService.sendToDs(pArq);

        Message expected = erroWrapperToErroConverter.convert(ValidationResult.success(readMessageFromFile("error-path-Erro.json")));
        assertEquals(expected, error);
        assertTrue(error instanceof Erro);
    }
}
