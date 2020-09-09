package com.rbkmoney.threeds.server.controller;

import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.service.DsRequestHandleService;
import com.rbkmoney.threeds.server.service.LogWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ds")
@RequiredArgsConstructor
@Slf4j
public class DirectoryServerController {

    private final DsRequestHandleService dsRequestHandleService;
    private final LogWrapper logWrapper;

    @PostMapping
    public ResponseEntity<Message> processMessage(@RequestBody Message requestMessage) {
        logWrapper.info("Begin handling /ds message", requestMessage);

        Message responseMessage = dsRequestHandleService.handle(requestMessage);

        logWrapper.info("End handling /ds message", responseMessage);

        return ResponseEntity.ok(responseMessage);
    }
}
