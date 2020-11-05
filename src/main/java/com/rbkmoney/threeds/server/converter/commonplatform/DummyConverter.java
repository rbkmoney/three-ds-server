package com.rbkmoney.threeds.server.converter.commonplatform;

import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.dto.ValidationResult;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;

@RequiredArgsConstructor
public class DummyConverter implements Converter<ValidationResult, Message> {

    @Override
    public Message convert(ValidationResult source) {
        return source.getMessage();
    }
}
