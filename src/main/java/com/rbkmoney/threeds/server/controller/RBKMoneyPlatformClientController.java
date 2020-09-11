package com.rbkmoney.threeds.server.controller;

import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.service.LogWrapper;
import com.rbkmoney.threeds.server.service.SenderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class RBKMoneyPlatformClientController {

    private final SenderService senderService;
    private final LogWrapper logWrapper;

    @PostMapping
    public ResponseEntity<Message> processMessage(@RequestBody Message requestMessage) {
        logWrapper.info("Start /sdk process handle", requestMessage);

        Message responseMessage = senderService.sendToDs(requestMessage);

        logWrapper.info("End /sdk process handle", responseMessage);

        return ResponseEntity.ok(responseMessage);
    }
}
