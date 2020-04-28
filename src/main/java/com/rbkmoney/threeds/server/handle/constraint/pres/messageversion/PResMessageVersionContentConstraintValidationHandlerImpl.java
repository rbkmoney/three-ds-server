package com.rbkmoney.threeds.server.handle.constraint.pres.messageversion;

import com.rbkmoney.threeds.server.config.DirectoryServerProviderHolder;
import com.rbkmoney.threeds.server.domain.root.emvco.PRes;
import com.rbkmoney.threeds.server.dto.ConstraintValidationResult;
import com.rbkmoney.threeds.server.handle.constraint.pres.PResConstraintValidationHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.rbkmoney.threeds.server.dto.ConstraintType.NOT_BLANK;
import static com.rbkmoney.threeds.server.dto.ConstraintType.PATTERN;
import static org.apache.commons.lang3.StringUtils.isBlank;

@Component
@RequiredArgsConstructor
public class PResMessageVersionContentConstraintValidationHandlerImpl implements PResConstraintValidationHandler {

    private final DirectoryServerProviderHolder providerHolder;

    @Override
    public boolean canHandle(PRes o) {
        return true;
    }

    @Override
    public boolean isValidMessageVersion(PRes message) {
        return true;
    }

    @Override
    public ConstraintValidationResult handle(PRes o) {
        String messageVersion = o.getMessageVersion();
        if (isBlank(messageVersion)) {
            return ConstraintValidationResult.failure(NOT_BLANK, "messageVersion");
        }

        if (!providerHolder.getEnvironmentProperties().getValidMessageVersions().contains(messageVersion)) {
            return ConstraintValidationResult.failure(PATTERN, "messageVersion");
        }

        String messageVersionAReq = o.getRequestMessage().getMessageVersion();

        if (providerHolder.getEnvironmentProperties().getValidMessageVersions().contains(o.getMessageVersion()) && !messageVersionAReq.equals(o.getMessageVersion())) {
            return ConstraintValidationResult.failure(PATTERN, "messageVersion");
        }

        return ConstraintValidationResult.success();
    }
}
