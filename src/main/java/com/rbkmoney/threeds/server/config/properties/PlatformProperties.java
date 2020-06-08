package com.rbkmoney.threeds.server.config.properties;

import com.rbkmoney.threeds.server.constants.PlatformMode;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("platform")
public class PlatformProperties {

    private PlatformMode mode;

}
