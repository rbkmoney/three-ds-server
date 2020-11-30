package com.rbkmoney.threeds.server.handle.constraint.testplatform.parq.acctnumber;

import com.rbkmoney.threeds.server.domain.root.proprietary.PArq;
import com.rbkmoney.threeds.server.dto.ConstraintValidationResult;
import com.rbkmoney.threeds.server.handle.constraint.commonplatform.utils.StringValidator;
import com.rbkmoney.threeds.server.handle.constraint.testplatform.parq.PArqConstraintValidationHandler;
import com.rbkmoney.threeds.server.service.testplatform.TestPlatformCardRangesStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import static com.rbkmoney.threeds.server.dto.ConstraintType.OUT_OF_CARD_RANGE;

@Component
@ConditionalOnProperty(name = "platform.mode", havingValue = "TEST_PLATFORM")
@RequiredArgsConstructor
public class AcctNumberContentConstraintValidationHandlerImpl implements PArqConstraintValidationHandler {

    private final StringValidator stringValidator;
    private final TestPlatformCardRangesStorageService testPlatformCardRangesStorageService;

    @Override
    public boolean canHandle(PArq o) {
        return o.getAcctNumber() != null;
    }

    @Override
    public ConstraintValidationResult handle(PArq o) {
        String acctNumber = o.getAcctNumber();

        ConstraintValidationResult validationResult = stringValidator.validateStringWithMinAndMaxLength("acctNumber", 19, 13, acctNumber);
        if (!validationResult.isValid()) {
            return validationResult;
        }

        String ulTestCaseId = o.getUlTestCaseId();
        if (!testPlatformCardRangesStorageService.isInCardRange(ulTestCaseId, acctNumber)) {
            return ConstraintValidationResult.failure(OUT_OF_CARD_RANGE, "acctNumber");
        }

        return ConstraintValidationResult.success();
    }
}
