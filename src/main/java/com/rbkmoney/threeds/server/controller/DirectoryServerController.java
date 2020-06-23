package com.rbkmoney.threeds.server.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.service.RequestHandleService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
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

    private final RequestHandleService requestHandleService;
    private final ObjectMapper objectMapper;

    @PostMapping
    @SneakyThrows
    public ResponseEntity<Message> processMessage(@RequestBody Message requestMessage) {
        log.info("\n" + objectMapper.writeValueAsString(requestMessage));

        Message responseMessage = requestHandleService.handle(requestMessage);

        log.info("\n" + objectMapper.writeValueAsString(responseMessage));

        return ResponseEntity.ok(responseMessage);
    }
}
