package com.rbkmoney.threeds.server.converter;

import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.domain.root.proprietary.PGcq;
import com.rbkmoney.threeds.server.domain.root.proprietary.PGcs;
import com.rbkmoney.threeds.server.dto.ValidationResult;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PGcqToPGcsConverter implements Converter<ValidationResult, Message> {

    @Override
    public Message convert(ValidationResult validationResult) {
        PGcq pGcq = (PGcq) validationResult.getMessage();

        PGcs pGcs = PGcs.builder()
                .p_messageVersion(pGcq.getP_messageVersion())
//              .htmlCreq(null) todo fix
                .build();
        pGcs.setMessageVersion(pGcq.getMessageVersion());
        pGcs.setRequestMessage(pGcq);
        return pGcs;
    }
}
