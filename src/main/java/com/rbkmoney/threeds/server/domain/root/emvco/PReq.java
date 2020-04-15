package com.rbkmoney.threeds.server.domain.root.emvco;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.rbkmoney.threeds.server.domain.message.MessageExtension;
import com.rbkmoney.threeds.server.domain.root.Message;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(onlyExplicitlyIncluded = true, callSuper = true)
@Builder
@JsonInclude(value = JsonInclude.Include.NON_ABSENT)
public class PReq extends Message {

    @ToString.Include
    private String threeDSServerRefNumber;
    @ToString.Include
    private String threeDSServerOperatorID;
    @ToString.Include
    private String threeDSServerTransID;
    private List<MessageExtension> messageExtension;
    @ToString.Include
    private String serialNum;

    @ToString.Include
    private String threeDSRequestorURL;

}
