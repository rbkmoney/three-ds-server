package com.rbkmoney.threeds.server.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.rbkmoney.threeds.server.config.properties.EnvironmentMessageProperties;
import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.domain.root.rbkmoney.RBKMoneyGetChallengeRequest;
import com.rbkmoney.threeds.server.domain.root.rbkmoney.RBKMoneyGetChallengeResponse;
import com.rbkmoney.threeds.server.dto.ValidationResult;
import com.rbkmoney.threeds.server.utils.CReqEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RBKMoneyGetChallengeRequestToRBKMoneyGetChallengeResponseConverter implements Converter<ValidationResult, Message> {

    private final CReqEncoder cReqEncoder;
    private final EnvironmentMessageProperties messageProperties;

    @Override
    public Message convert(ValidationResult validationResult) {
        try {
            RBKMoneyGetChallengeRequest request = (RBKMoneyGetChallengeRequest) validationResult.getMessage();
            request.setMessageVersion(messageProperties.getMessageVersion());

            return RBKMoneyGetChallengeResponse.builder()
                    .encodeCReq(cReqEncoder.createAndEncodeCReq(request))
                    .build();
        } catch (JsonProcessingException ex) {
            throw new RuntimeException("Parse error", ex);
        }
    }
}
