package com.rbkmoney.threeds.server.controller;

import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.service.testplatform.TestPlatformLogWrapper;
import com.rbkmoney.threeds.server.service.testplatform.TestPlatformSenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@ConditionalOnProperty(name = "platform.mode", havingValue = "TEST_PLATFORM")
@RequestMapping("/sdk")
@RequiredArgsConstructor
public class TestPlatformClientController {

    private final TestPlatformSenderService testPlatformSenderService;
    private final TestPlatformLogWrapper testPlatformLogWrapper;

    @PostMapping
    public ResponseEntity<Message> processMessage(
            @RequestHeader(name = "x-ul-testcaserun-id") String ulTestCaseId,
            @RequestBody Message requestMessage) {
        testPlatformLogWrapper.info("Start /sdk handle", requestMessage);

        requestMessage.setUlTestCaseId(ulTestCaseId);

        Message responseMessage = testPlatformSenderService.sendToDs(requestMessage);

        testPlatformLogWrapper.info("End /sdk handle", responseMessage);

        return ResponseEntity.ok(responseMessage);
    }
}
