package com.rbkmoney.threeds.server.domain.acs;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.rbkmoney.threeds.server.serialization.EnumWrapper;
import com.rbkmoney.threeds.server.serialization.deserializer.AcsInterfaceDeserializer;
import com.rbkmoney.threeds.server.serialization.deserializer.AcsUiTemplateDeserializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The ACS Rendering Type contains information about the rendering type
 * that the ACS is sending for the cardholder authentication.
 */
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@JsonInclude(value = JsonInclude.Include.NON_ABSENT)
public class AcsRenderingTypeWrapper {

    @JsonDeserialize(using = AcsInterfaceDeserializer.class)
    private EnumWrapper<AcsInterface> acsInterface;
    @JsonDeserialize(using = AcsUiTemplateDeserializer.class)
    private EnumWrapper<AcsUiTemplate> acsUiTemplate;

}
