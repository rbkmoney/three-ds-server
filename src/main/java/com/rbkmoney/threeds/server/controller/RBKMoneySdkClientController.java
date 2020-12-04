package com.rbkmoney.threeds.server.controller;

import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.service.rbkmoneyplatform.RBKMoneyLogWrapper;
import com.rbkmoney.threeds.server.service.rbkmoneyplatform.RBKMoneySenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ConditionalOnProperty(name = "platform.mode", havingValue = "RBK_MONEY_PLATFORM")
@RequestMapping("/sdk")
@RequiredArgsConstructor
public class RBKMoneySdkClientController {

    private final RBKMoneySenderService rbkMoneySenderService;
    private final RBKMoneyLogWrapper rbkMoneyLogWrapper;

    @PostMapping
    public ResponseEntity<Message> processMessage(@RequestBody Message requestMessage) {
        rbkMoneyLogWrapper.info("Start /sdk process handle", requestMessage);

        Message responseMessage = rbkMoneySenderService.sendToDs(requestMessage);

        rbkMoneyLogWrapper.info("End /sdk process handle", responseMessage);

        return ResponseEntity.ok(responseMessage);
    }
}
