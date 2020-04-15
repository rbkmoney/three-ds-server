package com.rbkmoney.threeds.server.constraint.validation;

import com.rbkmoney.threeds.server.domain.root.emvco.RReq;
import com.rbkmoney.threeds.server.handle.constraint.ConstraintValidationHandler;
import com.rbkmoney.threeds.server.service.ValidatorContextEnricherService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RReqCustomValidator extends CustomValidator<RReq> {

    public RReqCustomValidator(List<ConstraintValidationHandler<RReq>> constraintValidationHandlers,
                               ValidatorContextEnricherService contextEnricher) {
        super(constraintValidationHandlers, contextEnricher);
    }
}
