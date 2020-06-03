package com.rbkmoney.threeds.server.handle.constraint.rbkprq.messageversion;

import com.rbkmoney.threeds.server.config.properties.EnvironmentMessageProperties;
import com.rbkmoney.threeds.server.domain.root.rbkmoney.RBKMoneyPreparationRequest;
import com.rbkmoney.threeds.server.dto.ConstraintValidationResult;
import com.rbkmoney.threeds.server.handle.constraint.rbkprq.RBKMoneyPreparationRequestConstraintValidationHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.rbkmoney.threeds.server.dto.ConstraintType.NOT_BLANK;
import static com.rbkmoney.threeds.server.dto.ConstraintType.PATTERN;
import static org.apache.commons.lang3.StringUtils.isBlank;

@Component
@RequiredArgsConstructor
public class RBKMoneyPreparationRequestMessageVersionContentConstraintValidationHandlerImpl implements RBKMoneyPreparationRequestConstraintValidationHandler {

    private final EnvironmentMessageProperties messageProperties;

    @Override
    public boolean canHandle(RBKMoneyPreparationRequest o) {
        return true;
    }

    @Override
    public boolean isValidMessageVersion(RBKMoneyPreparationRequest message) {
        return true;
    }

    @Override
    public ConstraintValidationResult handle(RBKMoneyPreparationRequest o) {
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
