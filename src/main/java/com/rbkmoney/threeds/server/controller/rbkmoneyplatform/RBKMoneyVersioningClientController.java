package com.rbkmoney.threeds.server.controller.rbkmoneyplatform;

import com.rbkmoney.threeds.server.domain.versioning.ThreeDsVersionRequest;
import com.rbkmoney.threeds.server.domain.versioning.ThreeDsVersionResponse;
import com.rbkmoney.threeds.server.service.rbkmoneyplatform.RBKMoneyCardRangesStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ConditionalOnProperty(name = "platform.mode", havingValue = "RBK_MONEY_PLATFORM")
@RequiredArgsConstructor
@Slf4j
public class RBKMoneyVersioningClientController {

    private final RBKMoneyCardRangesStorageService rbkMoneyCardRangesStorageService;

    @PostMapping("/versioning")
    public ResponseEntity<ThreeDsVersionResponse> threeDsVersioningHandler(@RequestBody ThreeDsVersionRequest threeDsVersionRequest) {
        String accountNumber = threeDsVersionRequest.getAccountNumber();

        if (!StringUtils.isNumeric(accountNumber)) {
            return ResponseEntity.badRequest().build();
        }

        log.info("Trying to get ThreeDsVersion, threeDsVersionRequest={}", threeDsVersionRequest.toString());

        return ResponseEntity.of(
                rbkMoneyCardRangesStorageService.getThreeDsVersionResponse(
                        Long.parseLong(accountNumber)));
    }
}
