package com.rbkmoney.threeds.server.domain.root.emvco;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.rbkmoney.threeds.server.constraint.CustomValidation;
import com.rbkmoney.threeds.server.domain.CardRange;
import com.rbkmoney.threeds.server.domain.message.MessageExtension;
import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.serialization.ListWrapper;
import com.rbkmoney.threeds.server.serialization.deserializer.CardRangeDataDeserializer;
import com.rbkmoney.threeds.server.serialization.deserializer.MessageExtensionDeserializer;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(onlyExplicitlyIncluded = true, callSuper = true)
@Builder
@JsonInclude(value = JsonInclude.Include.NON_ABSENT)
@CustomValidation
public class PRes extends Message {

    @ToString.Include
    @JsonDeserialize(using = CardRangeDataDeserializer.class)
    private ListWrapper<CardRange> cardRangeData;
    private String dsEndProtocolVersion;
    private String dsStartProtocolVersion;
    @ToString.Include
    private String threeDSServerTransID;
    @ToString.Include
    private String dsTransID;
    @JsonDeserialize(using = MessageExtensionDeserializer.class)
    private ListWrapper<MessageExtension> messageExtension;
    @ToString.Include
    private String serialNum;

    private boolean handleRepetitionNeeded = false;

}
