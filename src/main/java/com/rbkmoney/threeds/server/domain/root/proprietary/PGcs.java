package com.rbkmoney.threeds.server.domain.root.proprietary;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.rbkmoney.threeds.server.domain.root.Message;
import lombok.*;

/**
 * proprietary Get Challenge Response
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@JsonInclude(value = JsonInclude.Include.NON_ABSENT)
public class PGcs extends Message {

    private String p_messageVersion;
    private String htmlCreq;

}
