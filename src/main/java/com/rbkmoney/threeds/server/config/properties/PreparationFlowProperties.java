package com.rbkmoney.threeds.server.config.properties;

import com.rbkmoney.threeds.server.constants.PreparationFlowMode;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("preparation-flow")
public class PreparationFlowProperties {

    private PreparationFlowMode mode;

}
