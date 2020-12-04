package com.rbkmoney.threeds.server.converter.rbkmoneyplatform;

import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.domain.root.rbkmoney.RBKMoneyGetChallengeRequest;
import com.rbkmoney.threeds.server.domain.root.rbkmoney.RBKMoneyGetChallengeResponse;
import com.rbkmoney.threeds.server.dto.CReq;
import com.rbkmoney.threeds.server.dto.ValidationResult;
import com.rbkmoney.threeds.server.utils.Base64Encoder;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;

@RequiredArgsConstructor
public class RBKMoneyGetChallengeRequestToRBKMoneyGetChallengeResponseConverter implements Converter<ValidationResult, Message> {

    private final Base64Encoder base64Encoder;

    @Override
    public Message convert(ValidationResult validationResult) {
        RBKMoneyGetChallengeRequest request = (RBKMoneyGetChallengeRequest) validationResult.getMessage();

        RBKMoneyGetChallengeResponse response = RBKMoneyGetChallengeResponse.builder()
                .encodeCReq(base64Encoder.encode(buildCReq(request)))
                .build();
        response.setMessageVersion(request.getMessageVersion());
        return response;
    }

    private CReq buildCReq(RBKMoneyGetChallengeRequest request) {
        return CReq.builder()
                .acsTransID(request.getAcsTransID())
                .challengeWindowSize(request.getChallengeWindowSize().getValue())
                .messageVersion(request.getMessageVersion())
                .threeDSServerTransID(request.getThreeDSServerTransID())
                .build();
    }
}
