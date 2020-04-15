package com.rbkmoney.threeds.server.handle.constraint.ares.transstatus;

import com.rbkmoney.threeds.server.domain.message.MessageCategory;
import com.rbkmoney.threeds.server.domain.root.emvco.AReq;
import com.rbkmoney.threeds.server.domain.root.emvco.ARes;
import com.rbkmoney.threeds.server.dto.ConstraintValidationResult;
import com.rbkmoney.threeds.server.handle.constraint.ares.AResConstraintValidationHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.rbkmoney.threeds.server.domain.transaction.TransactionStatus.AUTHENTICATION_VERIFICATION_SUCCESSFUL;
import static com.rbkmoney.threeds.server.dto.ConstraintType.NOT_NULL;
import static com.rbkmoney.threeds.server.utils.WrapperUtil.getEnumWrapperValue;

@Component
@RequiredArgsConstructor
public class AResAuthSuccessConstraintValidationHandlerImpl implements AResConstraintValidationHandler {

    @Override
    public boolean canHandle(ARes o) {
        return getEnumWrapperValue(o.getTransStatus()) == AUTHENTICATION_VERIFICATION_SUCCESSFUL;
    }

    @Override
    public ConstraintValidationResult handle(ARes o) {
        MessageCategory messageCategory = ((AReq) o.getRequestMessage()).getMessageCategory();

        if (messageCategory == MessageCategory.PAYMENT_AUTH
                && o.getAuthenticationValue() == null) {
            return ConstraintValidationResult.failure(NOT_NULL, "authenticationValue");
        }

        return ConstraintValidationResult.success();
    }
}
