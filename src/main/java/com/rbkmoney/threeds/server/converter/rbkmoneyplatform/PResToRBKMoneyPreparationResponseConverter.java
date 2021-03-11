package com.rbkmoney.threeds.server.converter.rbkmoneyplatform;

import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.domain.root.emvco.PRes;
import com.rbkmoney.threeds.server.domain.root.rbkmoney.RBKMoneyPreparationResponse;
import com.rbkmoney.threeds.server.ds.rbkmoneyplatform.RBKMoneyDsProviderHolder;
import com.rbkmoney.threeds.server.dto.ValidationResult;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;

@RequiredArgsConstructor
@SuppressWarnings({"checkstyle:localvariablename"})
public class PResToRBKMoneyPreparationResponseConverter implements Converter<ValidationResult, Message> {

    private final RBKMoneyDsProviderHolder rbkMoneyDsProviderHolder;

    @Override
    public Message convert(ValidationResult validationResult) {
        PRes pRes = (PRes) validationResult.getMessage();

        RBKMoneyPreparationResponse response = RBKMoneyPreparationResponse.builder()
                .providerId(rbkMoneyDsProviderHolder.getDsProvider().orElseThrow())
                .build();
        response.setMessageVersion(pRes.getMessageVersion());
        return response;
    }
}
