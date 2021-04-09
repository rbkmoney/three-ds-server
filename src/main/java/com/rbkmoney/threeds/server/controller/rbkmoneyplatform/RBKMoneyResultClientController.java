package com.rbkmoney.threeds.server.controller.rbkmoneyplatform;

import com.rbkmoney.damsel.threeds.server.storage.ChallengeFlowTransactionInfoNotFound;
import com.rbkmoney.threeds.server.domain.result.ThreeDsResultRequest;
import com.rbkmoney.threeds.server.domain.result.ThreeDsResultResponse;
import com.rbkmoney.threeds.server.domain.versioning.ThreeDsVersionRequest;
import com.rbkmoney.threeds.server.domain.versioning.ThreeDsVersionResponse;
import com.rbkmoney.threeds.server.dto.ChallengeFlowTransactionInfo;
import com.rbkmoney.threeds.server.exception.ChallengeFlowTransactionInfoNotFoundException;
import com.rbkmoney.threeds.server.service.rbkmoneyplatform.RBKMoneyCardRangesStorageService;
import com.rbkmoney.threeds.server.service.rbkmoneyplatform.RBKMoneyChallengeFlowTransactionInfoStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@ConditionalOnProperty(name = "platform.mode", havingValue = "RBK_MONEY_PLATFORM")
@RequiredArgsConstructor
@Slf4j
public class RBKMoneyResultClientController {

    private final RBKMoneyChallengeFlowTransactionInfoStorageService rbkMoneyChallengeFlowTransactionInfoStorageService;

    @PostMapping("/result")
    public ResponseEntity<ThreeDsResultResponse> threeDsResultHandler(
            @RequestBody ThreeDsResultRequest threeDsResultRequest
    ) {
        String threeDsServerTransId = threeDsResultRequest.getThreeDsServerTransId();

        if (threeDsServerTransId == null) {
            return ResponseEntity.badRequest().build();
        }
        log.info("Trying to get ThreeDsResult, threeDsServerTransId={}", threeDsServerTransId);

        try {
            ChallengeFlowTransactionInfo transactionInfo = rbkMoneyChallengeFlowTransactionInfoStorageService
                    .getChallengeFlowTransactionInfo(threeDsServerTransId);

            if (transactionInfo.getAuthenticationValue() == null) {
                return ResponseEntity.notFound().build();
            }

            ThreeDsResultResponse response = ThreeDsResultResponse.builder()
                    .authenticationValue(transactionInfo.getAuthenticationValue())
                    .eci(transactionInfo.getEci())
                    .build();

            return ResponseEntity.ok(response);
        } catch (ChallengeFlowTransactionInfoNotFoundException ex) {
            return ResponseEntity.notFound().build();
        }
    }
}
