package com.rbkmoney.threeds.server.handle.constraint.rbkgcq.messageversion;

import com.rbkmoney.threeds.server.config.properties.EnvironmentMessageProperties;
import com.rbkmoney.threeds.server.domain.root.rbkmoney.RBKMoneyGetChallengeRequest;
import com.rbkmoney.threeds.server.dto.ConstraintValidationResult;
import com.rbkmoney.threeds.server.handle.constraint.rbkgcq.RBKMoneyGetChallengeRequestConstraintValidationHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.rbkmoney.threeds.server.dto.ConstraintType.NOT_BLANK;
import static com.rbkmoney.threeds.server.dto.ConstraintType.PATTERN;
import static org.apache.commons.lang3.StringUtils.isBlank;

@Component
@RequiredArgsConstructor
public class RBKMoneyGetChallengeRequestMessageVersionContentConstraintValidationHandlerImpl implements RBKMoneyGetChallengeRequestConstraintValidationHandler {

    private final EnvironmentMessageProperties messageProperties;

    @Override
    public boolean canHandle(RBKMoneyGetChallengeRequest o) {
        return true;
    }

    @Override
    public boolean isValidMessageVersion(RBKMoneyGetChallengeRequest message) {
        return true;
    }

    @Override
    public ConstraintValidationResult handle(RBKMoneyGetChallengeRequest o) {
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
