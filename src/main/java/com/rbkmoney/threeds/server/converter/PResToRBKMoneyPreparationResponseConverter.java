package com.rbkmoney.threeds.server.converter;

import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.domain.root.emvco.PRes;
import com.rbkmoney.threeds.server.domain.root.rbkmoney.RBKMoneyPreparationResponse;
import com.rbkmoney.threeds.server.dto.ValidationResult;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

@Service
public class PResToRBKMoneyPreparationResponseConverter implements Converter<ValidationResult, Message> {

    @Override
    public Message convert(ValidationResult validationResult) {
        PRes pRes = (PRes) validationResult.getMessage();

        // TODO [a.romanov]: converter

        return RBKMoneyPreparationResponse.builder()
                .build();
    }
}
