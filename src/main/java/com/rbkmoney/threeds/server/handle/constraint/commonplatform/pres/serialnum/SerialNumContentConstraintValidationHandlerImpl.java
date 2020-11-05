package com.rbkmoney.threeds.server.handle.constraint.commonplatform.pres.serialnum;

import com.rbkmoney.threeds.server.domain.root.emvco.PRes;
import com.rbkmoney.threeds.server.dto.ConstraintValidationResult;
import com.rbkmoney.threeds.server.handle.constraint.commonplatform.pres.PResConstraintValidationHandler;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import static com.rbkmoney.threeds.server.dto.ConstraintType.PATTERN;

@Component
@RequiredArgsConstructor
public class SerialNumContentConstraintValidationHandlerImpl implements PResConstraintValidationHandler {

    @Override
    public boolean canHandle(PRes o) {
        return o.getSerialNum() != null;
    }

    @Override
    public ConstraintValidationResult handle(PRes o) {
        String candidate = o.getSerialNum();
        if (StringUtils.isBlank(candidate)) {
            return ConstraintValidationResult.failure(PATTERN, "serialNum");
        }

        if (candidate.length() > 20) {
            return ConstraintValidationResult.failure(PATTERN, "serialNum");
        }

        return ConstraintValidationResult.success();
    }
}
