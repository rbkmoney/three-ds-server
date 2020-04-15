package com.rbkmoney.threeds.server.domain.acs;

import com.fasterxml.jackson.annotation.JsonInclude;
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
public class AcsRenderingType {

    private AcsInterface acsInterface;
    private AcsUiTemplate acsUiTemplate;

}
