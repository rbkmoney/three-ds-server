package com.rbkmoney.threeds.server.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.rbkmoney.threeds.server.constraint.CustomValidation;
import com.rbkmoney.threeds.server.domain.acs.AcsInfoInd;
import com.rbkmoney.threeds.server.serialization.EnumWrapper;
import com.rbkmoney.threeds.server.serialization.ListWrapper;
import com.rbkmoney.threeds.server.serialization.deserializer.AcsInfoIndDeserializer;
import com.rbkmoney.threeds.server.serialization.deserializer.ActionIndDeserializer;
import lombok.*;

/**
 * The Card Range Data data element contains information returned in the PRes message to the 3DS Server from the DS that indicates the most
 * recent EMV 3-D Secure version supported by the ACS that hosts that card range. It also may optionally contain the ACS URL for the 3DS
 * Method if supported by the ACS and the DS Start and End Protocol Versions which support the card range.
 */
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@JsonInclude(value = JsonInclude.Include.NON_ABSENT)
@CustomValidation
public class CardRange {

    private String threeDSMethodURL;
    private String acsEndProtocolVersion;
    @JsonDeserialize(using = AcsInfoIndDeserializer.class)
    private ListWrapper<EnumWrapper<AcsInfoInd>> acsInfoInd;
    private String acsStartProtocolVersion;
    @JsonDeserialize(using = ActionIndDeserializer.class)
    private EnumWrapper<ActionInd> actionInd;
    private String dsEndProtocolVersion;
    private String dsStartProtocolVersion;
    @ToString.Include
    @EqualsAndHashCode.Include
    private String endRange;
    @ToString.Include
    @EqualsAndHashCode.Include
    private String startRange;

}
