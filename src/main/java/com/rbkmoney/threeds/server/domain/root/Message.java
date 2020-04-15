package com.rbkmoney.threeds.server.domain.root;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.rbkmoney.threeds.server.domain.root.emvco.*;
import com.rbkmoney.threeds.server.domain.root.proprietary.*;
import lombok.Data;
import lombok.ToString;
import org.springframework.lang.Nullable;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "messageType",
        visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = ErroWrapper.class, name = "Erro"),
        @JsonSubTypes.Type(value = AReq.class, name = "AReq"),
        @JsonSubTypes.Type(value = ARes.class, name = "ARes"),
        @JsonSubTypes.Type(value = PReq.class, name = "PReq"),
        @JsonSubTypes.Type(value = PRes.class, name = "PRes"),
        @JsonSubTypes.Type(value = RReq.class, name = "RReq"),
        @JsonSubTypes.Type(value = RRes.class, name = "RRes"),

        @JsonSubTypes.Type(value = PArq.class, name = "pArq"),
        @JsonSubTypes.Type(value = PArs.class, name = "pArs"),
        @JsonSubTypes.Type(value = PGcq.class, name = "pGcq"),
        @JsonSubTypes.Type(value = PGcs.class, name = "pGcs"),
        @JsonSubTypes.Type(value = PPrq.class, name = "pPrq"),
        @JsonSubTypes.Type(value = PPrs.class, name = "pPrs"),
})
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@ToString(onlyExplicitlyIncluded = true)
public abstract class Message implements TransactionalMessage, RepeatableHandleMessage {

    @ToString.Include
    private String messageVersion;

    // 6.5.2 Test Case Identification (3ds.selftestplatform.com)
    @JsonIgnore
    private String xULTestCaseRunId;

    @JsonIgnore
    @Nullable
    private Message requestMessage;

    @JsonIgnore
    public boolean isRelevantMessageVersion() {
        if (messageVersion != null) {
            return messageVersion.equals("2.2.0");
        }

        return false;
    }
}
