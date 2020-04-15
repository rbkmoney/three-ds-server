package com.rbkmoney.threeds.server.handle.constraint.pgcq;

import com.rbkmoney.threeds.server.domain.root.proprietary.PGcq;
import com.rbkmoney.threeds.server.dto.ConstraintValidationResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.rbkmoney.threeds.server.dto.ConstraintType.PATTERN;
import static com.rbkmoney.threeds.server.utils.WrapperUtil.getEnumWrapperGarbageValue;

@Component
@RequiredArgsConstructor
public class PGcqNotRequiredEnumGarbageWrapperContentConstraintValidationHandlerImpl implements PGcqConstraintValidationHandler {

    @Override
    public boolean canHandle(PGcq o) {
        return true;
    }

    @Override
    public ConstraintValidationResult handle(PGcq o) {
        if (getEnumWrapperGarbageValue(o.getChallengeWindowSize()) != null) {
            return ConstraintValidationResult.failure(PATTERN, "challengeWindowSize");
        }

        return ConstraintValidationResult.success();
    }
}
