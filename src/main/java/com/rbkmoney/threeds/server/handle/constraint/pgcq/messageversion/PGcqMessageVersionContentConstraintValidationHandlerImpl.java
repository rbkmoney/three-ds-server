package com.rbkmoney.threeds.server.handle.constraint.pgcq.messageversion;

import com.rbkmoney.threeds.server.config.DirectoryServerProviderHolder;
import com.rbkmoney.threeds.server.domain.root.proprietary.PGcq;
import com.rbkmoney.threeds.server.dto.ConstraintValidationResult;
import com.rbkmoney.threeds.server.handle.constraint.pgcq.PGcqConstraintValidationHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.rbkmoney.threeds.server.dto.ConstraintType.NOT_BLANK;
import static com.rbkmoney.threeds.server.dto.ConstraintType.PATTERN;
import static org.apache.commons.lang3.StringUtils.isBlank;

@Component
@RequiredArgsConstructor
public class PGcqMessageVersionContentConstraintValidationHandlerImpl implements PGcqConstraintValidationHandler {

    private final DirectoryServerProviderHolder providerHolder;

    @Override
    public boolean canHandle(PGcq o) {
        return true;
    }

    @Override
    public boolean isValidMessageVersion(PGcq message) {
        return true;
    }

    @Override
    public ConstraintValidationResult handle(PGcq o) {
        String messageVersion = o.getMessageVersion();
        if (isBlank(messageVersion)) {
            return ConstraintValidationResult.failure(NOT_BLANK, "messageVersion");
        }

        if (!providerHolder.getEnvironmentProperties().getValidMessageVersions().contains(messageVersion)) {
            return ConstraintValidationResult.failure(PATTERN, "messageVersion");
        }

        return ConstraintValidationResult.success();
    }
}
