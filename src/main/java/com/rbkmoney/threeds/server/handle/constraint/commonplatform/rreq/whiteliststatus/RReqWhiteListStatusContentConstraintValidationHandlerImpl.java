package com.rbkmoney.threeds.server.handle.constraint.commonplatform.rreq.whiteliststatus;

import com.rbkmoney.threeds.server.domain.root.emvco.RReq;
import com.rbkmoney.threeds.server.dto.ConstraintValidationResult;
import com.rbkmoney.threeds.server.handle.constraint.commonplatform.rreq.RReqConstraintValidationHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.rbkmoney.threeds.server.utils.Wrappers.getValue;
import static com.rbkmoney.threeds.server.utils.Wrappers.validateRequiredConditionField;

@Component
@RequiredArgsConstructor
public class RReqWhiteListStatusContentConstraintValidationHandlerImpl implements RReqConstraintValidationHandler {

    @Override
    public boolean canHandle(RReq o) {
        return getValue(o.getWhiteListStatus()) != null;
    }

    @Override
    public ConstraintValidationResult handle(RReq o) {
        return validateRequiredConditionField(o.getWhiteListStatusSource(), "whiteListStatusSource");
    }
}
