package com.rbkmoney.threeds.server.handle.constraint.testplatform.parq.notificationurl;

import com.rbkmoney.threeds.server.domain.root.proprietary.PArq;
import com.rbkmoney.threeds.server.dto.ConstraintValidationResult;
import com.rbkmoney.threeds.server.handle.constraint.commonplatform.utils.StringValidator;
import com.rbkmoney.threeds.server.handle.constraint.testplatform.parq.PArqConstraintValidationHandler;
import lombok.RequiredArgsConstructor;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.stereotype.Component;

import static com.rbkmoney.threeds.server.dto.ConstraintType.PATTERN;

@Component
@RequiredArgsConstructor
public class NotificationURLContentConstraintValidationHandlerImpl implements PArqConstraintValidationHandler {

    private final UrlValidator urlValidator = new UrlValidator();
    private final StringValidator stringValidator;

    @Override
    public boolean canHandle(PArq o) {
        return stringValidator.isNotNull(o.getNotificationURL());
    }

    @Override
    public ConstraintValidationResult handle(PArq o) {
        ConstraintValidationResult validationResult =
                stringValidator.validateStringWithMaxLength("notificationURL", 256, o.getNotificationURL());
        if (!validationResult.isValid()) {
            return validationResult;
        }

        if (!urlValidator.isValid(o.getNotificationURL())) {
            return ConstraintValidationResult.failure(PATTERN, "notificationURL");
        }

        return validationResult;
    }
}
