package com.rbkmoney.threeds.server.handle.constraint.rbkmoneyplatform.pres.cardrangedata;

import com.rbkmoney.threeds.server.domain.cardrange.CardRange;
import com.rbkmoney.threeds.server.domain.root.emvco.PRes;
import com.rbkmoney.threeds.server.ds.DsProviderHolder;
import com.rbkmoney.threeds.server.dto.ConstraintType;
import com.rbkmoney.threeds.server.dto.ConstraintValidationResult;
import com.rbkmoney.threeds.server.handle.constraint.commonplatform.pres.PResConstraintValidationHandler;
import com.rbkmoney.threeds.server.service.CardRangesStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.List;
import java.util.Set;

import static com.rbkmoney.threeds.server.dto.ConstraintType.PATTERN;
import static com.rbkmoney.threeds.server.utils.Collections.safeList;
import static java.lang.Long.parseLong;

@Component
@ConditionalOnProperty(name = "platform.mode", havingValue = "RBK_MONEY_PLATFORM")
@RequiredArgsConstructor
public class RBKMoneyCardRangeContentConstraintValidationHandlerImpl implements PResConstraintValidationHandler {

    private final DsProviderHolder dsProviderHolder;
    private final Validator validator;
    private final CardRangesStorageService cardRangesStorageService;

    @Override
    public boolean canHandle(PRes o) {
        return o.getCardRangeData() != null;
    }

    @Override
    public ConstraintValidationResult handle(PRes o) {
        // todo
//        if (o.getCardRangeData().isGarbage()) {
//            return ConstraintValidationResult.failure(PATTERN, "cardRangeData");
//        }

        List<CardRange> cardRangeData = safeList(o.getCardRangeData());

        for (CardRange cardRange : cardRangeData) {
            long startRange = parseLong(cardRange.getStartRange());
            long endRange = parseLong(cardRange.getEndRange());
            if (startRange > endRange) {
                return ConstraintValidationResult.failure(PATTERN, "cardRangeData");
            }

            Set<ConstraintViolation<CardRange>> cardRangeErrors = validator.validate(cardRange);
            if (!cardRangeErrors.isEmpty()) {

                ConstraintViolation<CardRange> cardRangeError = cardRangeErrors.iterator().next();
                ConstraintType constraintType = getConstraintType(cardRangeError);
                String fieldName = cardRangeError.getPropertyPath().toString();
                return ConstraintValidationResult.failure(constraintType, fieldName);
            }
        }

        String tag = dsProviderHolder.getTag(o).orElseThrow();
        if (!cardRangesStorageService.isValidCardRanges(tag, cardRangeData)) {
            return ConstraintValidationResult.failure(PATTERN, "cardRangeData");
        }

        return ConstraintValidationResult.success();
    }

    private ConstraintType getConstraintType(ConstraintViolation<CardRange> cardRangeError) {
        return ConstraintType.of(cardRangeError.getMessageTemplate());
    }
}