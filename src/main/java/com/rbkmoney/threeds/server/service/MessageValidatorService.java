package com.rbkmoney.threeds.server.service;

import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.dto.ValidationResult;

public interface MessageValidatorService {

    ValidationResult validate(Message message);

}
