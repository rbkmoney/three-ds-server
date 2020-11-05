package com.rbkmoney.threeds.server.handle.constraint.testplatform.pprq.messageversion;

import com.rbkmoney.threeds.server.config.properties.EnvironmentMessageProperties;
import com.rbkmoney.threeds.server.domain.root.proprietary.PPrq;
import com.rbkmoney.threeds.server.dto.ConstraintValidationResult;
import com.rbkmoney.threeds.server.handle.constraint.testplatform.pprq.PPrqConstraintValidationHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.rbkmoney.threeds.server.dto.ConstraintType.NOT_BLANK;
import static com.rbkmoney.threeds.server.dto.ConstraintType.PATTERN;
import static org.apache.commons.lang3.StringUtils.isBlank;

@Component
@RequiredArgsConstructor
public class PPrqMessageVersionContentConstraintValidationHandlerImpl implements PPrqConstraintValidationHandler {

    private final EnvironmentMessageProperties messageProperties;

    @Override
    public boolean canHandle(PPrq o) {
        return true;
    }

    @Override
    public boolean isValidMessageVersion(PPrq message) {
        return true;
    }

    @Override
    public ConstraintValidationResult handle(PPrq o) {
        String messageVersion = o.getMessageVersion();
        if (isBlank(messageVersion)) {
            return ConstraintValidationResult.failure(NOT_BLANK, "messageVersion");
        }

        if (!messageProperties.getValidMessageVersions().contains(messageVersion)) {
            return ConstraintValidationResult.failure(PATTERN, "messageVersion");
        }

        return ConstraintValidationResult.success();
    }
}
