package com.rbkmoney.threeds.server.handle.constraint.rbkmoneyplatform.rbkmoneyauthenticationrequest.phone;

import com.rbkmoney.threeds.server.domain.phone.Phone;
import com.rbkmoney.threeds.server.dto.ConstraintValidationResult;
import com.rbkmoney.threeds.server.handle.constraint.commonplatform.utils.StringValidator;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Component;

import static com.rbkmoney.threeds.server.dto.ConstraintType.PATTERN;

@Component
@RequiredArgsConstructor
public class RBKMoneyAuthenticationRequestPhoneContentConstraintValidationHelper {

    private final StringValidator stringValidator;

    public ConstraintValidationResult validate(Phone phone) {
        if (stringValidator.isNotNull(phone.getCc())) {
            ConstraintValidationResult validationResult = stringValidator.validateStringWithMinAndMaxLength("phone.cc", 3, 1, phone.getCc());
            if (!validationResult.isValid()) {
                return validationResult;
            }

            if (!NumberUtils.isCreatable(phone.getCc())) {
                return ConstraintValidationResult.failure(PATTERN, "phone.cc");
            }
        }

        if (stringValidator.isNotNull(phone.getSubscriber())) {
            ConstraintValidationResult validationResult = stringValidator.validateStringWithMaxLength("phone.subscriber", 15, phone.getSubscriber());
            if (!validationResult.isValid()) {
                return validationResult;
            }

            if (!NumberUtils.isCreatable(phone.getSubscriber())) {
                return ConstraintValidationResult.failure(PATTERN, "phone.subscriber");
            }
        }

        return ConstraintValidationResult.success();
    }
}