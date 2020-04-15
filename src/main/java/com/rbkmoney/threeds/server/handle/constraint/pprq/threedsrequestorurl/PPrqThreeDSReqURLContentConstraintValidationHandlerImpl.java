package com.rbkmoney.threeds.server.handle.constraint.pprq.threedsrequestorurl;

import com.rbkmoney.threeds.server.domain.root.proprietary.PPrq;
import com.rbkmoney.threeds.server.dto.ConstraintValidationResult;
import com.rbkmoney.threeds.server.handle.constraint.common.StringValidator;
import com.rbkmoney.threeds.server.handle.constraint.pprq.PPrqConstraintValidationHandler;
import lombok.RequiredArgsConstructor;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.stereotype.Component;

import static com.rbkmoney.threeds.server.dto.ConstraintType.PATTERN;

@Component
@RequiredArgsConstructor
public class PPrqThreeDSReqURLContentConstraintValidationHandlerImpl implements PPrqConstraintValidationHandler {

    private final UrlValidator urlValidator = new UrlValidator();
    private final StringValidator stringValidator;

    @Override
    public boolean canHandle(PPrq o) {
        return stringValidator.isNotNull(o.getThreeDSRequestorURL());
    }

    @Override
    public ConstraintValidationResult handle(PPrq o) {
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
