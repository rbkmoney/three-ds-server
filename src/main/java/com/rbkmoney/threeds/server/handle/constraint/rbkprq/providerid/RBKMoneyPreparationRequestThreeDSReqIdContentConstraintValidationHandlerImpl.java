package com.rbkmoney.threeds.server.handle.constraint.rbkprq.providerid;

import com.rbkmoney.threeds.server.constants.DirectoryServerProvider;
import com.rbkmoney.threeds.server.domain.root.rbkmoney.RBKMoneyPreparationRequest;
import com.rbkmoney.threeds.server.dto.ConstraintValidationResult;
import com.rbkmoney.threeds.server.handle.constraint.common.StringValidator;
import com.rbkmoney.threeds.server.handle.constraint.rbkprq.RBKMoneyPreparationRequestConstraintValidationHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.rbkmoney.threeds.server.dto.ConstraintType.PATTERN;

@Component
@RequiredArgsConstructor
public class RBKMoneyPreparationRequestThreeDSReqIdContentConstraintValidationHandlerImpl implements RBKMoneyPreparationRequestConstraintValidationHandler {

    private final StringValidator stringValidator;

    @Override
    public boolean canHandle(RBKMoneyPreparationRequest o) {
        return stringValidator.isNotNull(o.getProviderId());
    }

    @Override
    public ConstraintValidationResult handle(RBKMoneyPreparationRequest o) {
        for (DirectoryServerProvider value : DirectoryServerProvider.values()) {
            if (value.getId().equals(o.getProviderId())) {
                return ConstraintValidationResult.success();
            }
        }

        return ConstraintValidationResult.failure(PATTERN, "providerId");
    }
}
