package com.rbkmoney.threeds.server.handle.constraint.rbkmoneyplatform.rbkmoneypreparationrequest.providerid;

import com.rbkmoney.threeds.server.domain.root.rbkmoney.RBKMoneyPreparationRequest;
import com.rbkmoney.threeds.server.ds.DsProvider;
import com.rbkmoney.threeds.server.dto.ConstraintValidationResult;
import com.rbkmoney.threeds.server.handle.constraint.commonplatform.utils.StringValidator;
import com.rbkmoney.threeds.server.handle.constraint.rbkmoneyplatform.rbkmoneypreparationrequest.RBKMoneyPreparationRequestConstraintValidationHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.rbkmoney.threeds.server.dto.ConstraintType.PATTERN;

@Component
@RequiredArgsConstructor
public class RBKMoneyPreparationRequestThreeDSReqIdContentConstraintValidationHandlerImpl
        implements RBKMoneyPreparationRequestConstraintValidationHandler {

    private final StringValidator stringValidator;

    @Override
    public boolean canHandle(RBKMoneyPreparationRequest o) {
        return stringValidator.isNotNull(o.getProviderId());
    }

    @Override
    public ConstraintValidationResult handle(RBKMoneyPreparationRequest o) {
        for (DsProvider dsProvider : DsProvider.values()) {
            if (dsProvider.getId().equals(o.getProviderId())) {
                return ConstraintValidationResult.success();
            }
        }

        return ConstraintValidationResult.failure(PATTERN, "providerId");
    }
}
