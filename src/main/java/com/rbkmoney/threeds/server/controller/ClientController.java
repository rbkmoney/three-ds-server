package com.rbkmoney.threeds.server.controller;

import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.service.SenderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sdk")
@RequiredArgsConstructor
@Slf4j
public class ClientController {

    private final SenderService senderService;

    @PostMapping
    public ResponseEntity<Message> processMessage(@RequestHeader("x-ul-testcaserun-id") String xULTestCaseRunId,
                                                  @RequestBody Message requestMessage) {
        log.info("Begin handling /sdk message: message={}", requestMessage.toString());

        // todo remove or replace
        requestMessage.setXULTestCaseRunId(xULTestCaseRunId);
        Message responseMessage = senderService.sendToDs(requestMessage);

        log.info("End handling /sdk message: message={}", responseMessage.toString());

        return ResponseEntity.ok(responseMessage);
    }
}
