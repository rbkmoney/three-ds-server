package com.rbkmoney.threeds.server.handle.constraint.rreq.whiteliststatus;

import com.rbkmoney.threeds.server.domain.root.emvco.RReq;
import com.rbkmoney.threeds.server.dto.ConstraintValidationResult;
import com.rbkmoney.threeds.server.handle.constraint.rreq.RReqConstraintValidationHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.rbkmoney.threeds.server.utils.WrapperUtil.getEnumWrapperValue;
import static com.rbkmoney.threeds.server.utils.WrapperUtil.validateRequiredConditionField;

@Component
@RequiredArgsConstructor
public class RReqWhiteListStatusContentConstraintValidationHandlerImpl implements RReqConstraintValidationHandler {

    @Override
    public boolean canHandle(RReq o) {
        return getEnumWrapperValue(o.getWhiteListStatus()) != null;
    }

    @Override
    public ConstraintValidationResult handle(RReq o) {
        return validateRequiredConditionField(o.getWhiteListStatusSource(), "whiteListStatusSource");
    }
}
