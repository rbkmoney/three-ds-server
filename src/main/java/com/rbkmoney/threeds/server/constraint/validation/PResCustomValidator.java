package com.rbkmoney.threeds.server.constraint.validation;

import com.rbkmoney.threeds.server.domain.root.emvco.PRes;
import com.rbkmoney.threeds.server.handle.constraint.ConstraintValidationHandler;
import com.rbkmoney.threeds.server.service.ValidatorContextEnricherService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PResCustomValidator extends CustomValidator<PRes> {

    public PResCustomValidator(List<ConstraintValidationHandler<PRes>> constraintValidationHandlers,
                               ValidatorContextEnricherService contextEnricher) {
        super(constraintValidationHandlers, contextEnricher);
    }
}
