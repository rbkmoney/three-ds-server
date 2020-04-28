package com.rbkmoney.threeds.server.handle.constraint.parq.messageversion;

import com.rbkmoney.threeds.server.config.DirectoryServerProviderHolder;
import com.rbkmoney.threeds.server.domain.root.proprietary.PArq;
import com.rbkmoney.threeds.server.dto.ConstraintValidationResult;
import com.rbkmoney.threeds.server.handle.constraint.parq.PArqConstraintValidationHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.rbkmoney.threeds.server.dto.ConstraintType.NOT_BLANK;
import static com.rbkmoney.threeds.server.dto.ConstraintType.PATTERN;
import static org.apache.commons.lang3.StringUtils.isBlank;

@Component
@RequiredArgsConstructor
public class PArqMessageVersionContentConstraintValidationHandlerImpl implements PArqConstraintValidationHandler {

    private final DirectoryServerProviderHolder providerHolder;

    @Override
    public boolean canHandle(PArq o) {
        return true;
    }

    @Override
    public boolean isValidMessageVersion(PArq message) {
        return true;
    }

    @Override
    public ConstraintValidationResult handle(PArq o) {
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
