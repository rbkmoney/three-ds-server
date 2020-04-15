package com.rbkmoney.threeds.server.domain.unwrapped;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Aggregate class for Address information.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@JsonInclude(value = JsonInclude.Include.NON_ABSENT)
public class Address {

    @JsonProperty("AddrCity")
    private String addrCity;
    @JsonProperty("AddrCountry")
    private String addrCountry;
    @JsonProperty("AddrLine1")
    private String addrLine1;
    @JsonProperty("AddrLine2")
    private String addrLine2;
    @JsonProperty("AddrLine3")
    private String addrLine3;
    @JsonProperty("AddrPostCode")
    private String addrPostCode;
    @JsonProperty("AddrState")
    private String addrState;

}
