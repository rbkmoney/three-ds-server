package com.rbkmoney.threeds.server.controller;

import com.rbkmoney.threeds.server.domain.versioning.ThreeDsVersion;
import com.rbkmoney.threeds.server.service.rbkmoneyplatform.RBKMoneyCardRangesStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.rbkmoney.threeds.server.utils.AccountNumberUtils.hideAccountNumber;

@RestController
@ConditionalOnProperty(name = "platform.mode", havingValue = "RBK_MONEY_PLATFORM")
@RequiredArgsConstructor
@Slf4j
public class RBKMoneyVersioningClientController {

    private final RBKMoneyCardRangesStorageService rbkMoneyCardRangesStorageService;

    @GetMapping("/versioning")
    public ResponseEntity<ThreeDsVersion> versioningHandler(@RequestParam("account_number") String accountNumber) {
        if (!StringUtils.isNumeric(accountNumber)) {
            return ResponseEntity.badRequest().build();
        }

        log.info("Trying to get ThreeDsVersion, accountNumber={}", hideAccountNumber(accountNumber));

        return ResponseEntity.of(
                rbkMoneyCardRangesStorageService.getThreeDsVersion(
                        Long.parseLong(accountNumber)));
    }
}
