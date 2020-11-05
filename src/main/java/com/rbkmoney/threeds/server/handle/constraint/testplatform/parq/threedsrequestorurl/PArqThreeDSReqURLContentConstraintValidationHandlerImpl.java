package com.rbkmoney.threeds.server.handle.constraint.testplatform.parq.threedsrequestorurl;

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
public class PArqThreeDSReqURLContentConstraintValidationHandlerImpl implements PArqConstraintValidationHandler {

    private final UrlValidator urlValidator = new UrlValidator();
    private final StringValidator stringValidator;

    @Override
    public boolean canHandle(PArq o) {
        return stringValidator.isNotNull(o.getThreeDSRequestorURL());
    }

    @Override
    public ConstraintValidationResult handle(PArq o) {
        ConstraintValidationResult validationResult = stringValidator.validateStringWithMaxLength("threeDSRequestorURL", 2048, o.getThreeDSRequestorURL());
        if (!validationResult.isValid()) {
            return validationResult;
        }

        if (!urlValidator.isValid(o.getThreeDSRequestorURL())) {
            return ConstraintValidationResult.failure(PATTERN, "threeDSRequestorURL");
        }

        return validationResult;
    }
}
