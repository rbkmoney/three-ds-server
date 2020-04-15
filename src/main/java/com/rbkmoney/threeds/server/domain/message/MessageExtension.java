package com.rbkmoney.threeds.server.domain.message;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

/**
 * Message Extensions are used to carry additional data that is not defined in the 3-D Secure Protocol
 * and Core Functions Specification.
 * The party defining the Message Extension shall define the format of the data.
 * <p>
 * JSON Data Type: Object
 */
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@JsonInclude(value = JsonInclude.Include.NON_ABSENT)
public class MessageExtension {

    private String name;
    private String id;
    private Boolean criticalityIndicator;
    private Map<String, String> data;

}
