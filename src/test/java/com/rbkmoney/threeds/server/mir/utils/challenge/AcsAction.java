package com.rbkmoney.threeds.server.mir.utils.challenge;

import org.springframework.http.ResponseEntity;

public interface AcsAction {

    ResponseEntity<String> apply(String testCase) throws Exception;

}
