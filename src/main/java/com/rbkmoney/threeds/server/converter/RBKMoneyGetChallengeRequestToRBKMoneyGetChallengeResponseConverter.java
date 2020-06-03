package com.rbkmoney.threeds.server.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
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

    @Override
    public Message convert(ValidationResult validationResult) {
        try {
            RBKMoneyGetChallengeRequest request = (RBKMoneyGetChallengeRequest) validationResult.getMessage();

            RBKMoneyGetChallengeResponse response = RBKMoneyGetChallengeResponse.builder()
                    .encodeCReq(cReqEncoder.createAndEncodeCReq(request))
                    .build();
            response.setMessageVersion(request.getMessageVersion());
            response.setRequestMessage(request);
            return response;
        } catch (JsonProcessingException ex) {
            throw new RuntimeException("Parse error", ex);
        }
    }
}
