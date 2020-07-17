package com.rbkmoney.threeds.server.controller;

import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.service.SenderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@ConditionalOnProperty(name = "platform.mode", havingValue = "TEST_PLATFORM")
@RequestMapping("/sdk")
@RequiredArgsConstructor
@Slf4j
public class TestPlatformClientController {

    private final SenderService senderService;

    @PostMapping
    public ResponseEntity<Message> processMessage(
            @RequestHeader(name = "x-ul-testcaserun-id") String ulTestCaseId,
            @RequestBody Message requestMessage) {
        log.info("Begin handling /sdk message: message={}", requestMessage.toString());

        requestMessage.setUlTestCaseId(ulTestCaseId);

        Message responseMessage = senderService.sendToDs(requestMessage);

        log.info("End handling /sdk message: message={}", responseMessage.toString());

        return ResponseEntity.ok(responseMessage);
    }
}
