package com.rbkmoney.threeds.server.handle.constraint.testplatform.parq.browserip;

import com.rbkmoney.threeds.server.domain.root.proprietary.PArq;
import com.rbkmoney.threeds.server.dto.ConstraintValidationResult;
import com.rbkmoney.threeds.server.handle.constraint.testplatform.parq.PArqConstraintValidationHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.rbkmoney.threeds.server.dto.ConstraintType.PATTERN;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.http.conn.util.InetAddressUtils.isIPv4Address;
import static org.apache.http.conn.util.InetAddressUtils.isIPv6Address;

@Component
@RequiredArgsConstructor
public class BrowserIPContentConstraintValidationHandlerImpl implements PArqConstraintValidationHandler {

    @Override
    public boolean canHandle(PArq o) {
        return o.getBrowserIP() != null;
    }

    @Override
    public ConstraintValidationResult handle(PArq o) {
        String browserIP = o.getBrowserIP();

        if (isBlank(browserIP) || !isIPv4Address(browserIP) && !isIPv6Address(browserIP)) {
            return ConstraintValidationResult.failure(PATTERN, "browserIP");
        }

        return ConstraintValidationResult.success();
    }
}
