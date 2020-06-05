package com.rbkmoney.threeds.server.config.properties;

import com.rbkmoney.threeds.server.constants.StorageMode;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("storage")
public class StorageProperties {

    private StorageMode mode;

}
