package com.rbkmoney.threeds.server.visa;

import com.rbkmoney.threeds.server.domain.Valuable;
import com.rbkmoney.threeds.server.domain.root.emvco.CReq;
import com.rbkmoney.threeds.server.domain.root.emvco.CRes;
import com.rbkmoney.threeds.server.domain.root.proprietary.PArs;
import com.rbkmoney.threeds.server.serialization.EnumWrapper;
import com.rbkmoney.threeds.server.serialization.ListWrapper;
import com.rbkmoney.threeds.server.serialization.TemporalAccessorWrapper;
import com.rbkmoney.threeds.server.service.SenderService;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAccessor;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest(
        properties = {
                "environment.ds-url=https://visasecuretestsuite-vsts.3dsecure.net/ds2",
                "client.ssl.trust-store=classpath:3ds_server_pki/visa.p12",
                "client.ssl.trust-store-password=Hd03tFk6iO3XiZ9a",
                "environment.three-ds-server-ref-number=3DS_LOA_SER_DIPL_020200_00236",
                "three-ds-server-url=https://visa.3ds.rbk.money",
//                "logging.level.org.apache.http=debug",
        }
)
@Ignore
public abstract class VisaIntegrationConfig {

    @Autowired
    protected SenderService senderService;

    @Autowired
    protected RestTemplate restTemplate;

    private static final Random RANDOM = new Random();

    //visa
    protected static final String ACQUIRER_BIN = "400551";
    protected static final String THREE_DS_SERVER_OPERATOR_ID = "10075020";

    protected CRes sendAs3dsClientTypeBRW(PArs pArs) {
        CReq cReq = CReq.builder()
                .acsTransID(pArs.getAcsTransID())
                .challengeWindowSize("05")
                .messageType("CReq")
                .messageVersion("2.1.0")
                .threeDSServerTransID(pArs.getThreeDSServerTransID())
                .build();

        return restTemplate.postForEntity(pArs.getAcsURL(), cReq, CRes.class).getBody();
    }

    protected <T extends Valuable> EnumWrapper<T> getEnumWrapper(T value) {
        EnumWrapper<T> wrapper = new EnumWrapper<>();
        wrapper.setValue(value);
        return wrapper;
    }

    protected <T extends TemporalAccessor> TemporalAccessorWrapper<T> getTemporalAccessorWrapper(T value) {
        TemporalAccessorWrapper<T> wrapper = new TemporalAccessorWrapper<>();
        wrapper.setValue(value);
        return wrapper;
    }

    protected <T> ListWrapper<T> getListWrapper(List<T> value) {
        ListWrapper<T> wrapper = new ListWrapper<>();
        wrapper.setValue(value);
        return wrapper;
    }

    protected String randomId() {
        return UUID.randomUUID().toString();
    }

    protected boolean randomBoolean() {
        return new Random().nextBoolean();
    }

    protected String randomIP() {
        return "192.168.0.1";
    }

    protected String randomUrl() {
        return "https://www.visa.ru/notify";
    }

    protected String randomMail() {
        return "support@rbk.money";
    }

    protected Map<String, String> getSdkEphemPubKey() {
        return Map.of("crv", "P-256", "kty", "EC", "x", "bbeJ_uHcjTimTa13wahBrYqOOTruiadSImwMh-I9gMs", "y", "3p7hjQDYzw32iEfVFK_ixJ8iECl9bQ8_qmy7m64R4s8");
    }

    protected String randomNumericTwoNumbers() {
        return "66";
    }

    protected TemporalAccessorWrapper<LocalDate> randomLocalDate() {
        return getTemporalAccessorWrapper(LocalDate.now());
    }

    protected TemporalAccessorWrapper<LocalDateTime> randomLocalDateTime() {
        return getTemporalAccessorWrapper(LocalDateTime.now());
    }

    protected String randomString() {
        return randomString(10);
    }

    protected String randomString(int length) {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        return RANDOM.ints(leftLimit, rightLimit + 1)
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    protected String randomNumeric(int length) {
        int leftLimit = 48; // letter 'a'
        int rightLimit = 57; // letter 'z'
        return RANDOM.ints(leftLimit, rightLimit + 1)
                .limit(length)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}
