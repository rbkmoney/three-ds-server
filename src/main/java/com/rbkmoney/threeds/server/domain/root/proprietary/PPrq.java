package com.rbkmoney.threeds.server.domain.root.proprietary;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.rbkmoney.threeds.server.constraint.CustomValidation;
import com.rbkmoney.threeds.server.domain.message.MessageExtension;
import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.serialization.ListWrapper;
import com.rbkmoney.threeds.server.serialization.deserializer.MessageExtensionDeserializer;
import lombok.*;

/**
 * proprietary Preparation Request
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(onlyExplicitlyIncluded = true, callSuper = true)
@Builder
@JsonInclude(value = JsonInclude.Include.NON_ABSENT)
@CustomValidation
public class PPrq extends Message {

    private String p_messageVersion;
    @ToString.Include
    private String threeDSRequestorID;
    @ToString.Include
    private String threeDSServerTransID;
    private String threeDSRequestorURL;
    @JsonDeserialize(using = MessageExtensionDeserializer.class)
    private ListWrapper<MessageExtension> messageExtension;

    @ToString.Include
    private String threeDSServerOperatorID;

}
