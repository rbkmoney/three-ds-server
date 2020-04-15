package com.rbkmoney.threeds.server.handle.constraint.ares.messageversion;

import com.rbkmoney.threeds.server.config.properties.EnvironmentProperties;
import com.rbkmoney.threeds.server.domain.root.emvco.ARes;
import com.rbkmoney.threeds.server.dto.ConstraintValidationResult;
import com.rbkmoney.threeds.server.handle.constraint.ares.AResConstraintValidationHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.rbkmoney.threeds.server.dto.ConstraintType.NOT_BLANK;
import static com.rbkmoney.threeds.server.dto.ConstraintType.PATTERN;
import static org.apache.commons.lang3.StringUtils.isBlank;

@Component
@RequiredArgsConstructor
public class AResMessageVersionContentConstraintValidationHandlerImpl implements AResConstraintValidationHandler {

    private final EnvironmentProperties environmentProperties;

    @Override
    public boolean canHandle(ARes o) {
        return true;
    }

    @Override
    public boolean isValidMessageVersion(ARes message) {
        return true;
    }

    @Override
    public ConstraintValidationResult handle(ARes o) {
        String messageVersion = o.getMessageVersion();
        if (isBlank(messageVersion)) {
            return ConstraintValidationResult.failure(NOT_BLANK, "messageVersion");
        }

        if (!environmentProperties.getValidMessageVersions().contains(messageVersion)) {
            return ConstraintValidationResult.failure(PATTERN, "messageVersion");
        }

        String messageVersionAReq = o.getRequestMessage().getMessageVersion();

        if (environmentProperties.getValidMessageVersions().contains(messageVersion) && !messageVersionAReq.equals(messageVersion)) {
            return ConstraintValidationResult.failure(PATTERN, "messageVersion");
        }

        return ConstraintValidationResult.success();
    }
}
