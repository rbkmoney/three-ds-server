package com.rbkmoney.threeds.server.handle.constraint.rbkmoneyplatform.rbkmoneyauthenticationrequest.browserip;

import com.rbkmoney.threeds.server.domain.root.rbkmoney.RBKMoneyAuthenticationRequest;
import com.rbkmoney.threeds.server.dto.ConstraintValidationResult;
import com.rbkmoney.threeds.server.handle.constraint.rbkmoneyplatform.rbkmoneyauthenticationrequest.RBKMoneyAuthenticationRequestConstraintValidationHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.rbkmoney.threeds.server.dto.ConstraintType.PATTERN;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.http.conn.util.InetAddressUtils.isIPv4Address;
import static org.apache.http.conn.util.InetAddressUtils.isIPv6Address;

@Component
@RequiredArgsConstructor
public class RBKMoneyAuthenticationRequestBrowserIPContentConstraintValidationHandlerImpl implements RBKMoneyAuthenticationRequestConstraintValidationHandler {

    @Override
    public boolean canHandle(RBKMoneyAuthenticationRequest o) {
        return o.getBrowserIP() != null;
    }

    @Override
    public ConstraintValidationResult handle(RBKMoneyAuthenticationRequest o) {
        String browserIP = o.getBrowserIP();

        if (isBlank(browserIP) || !isIPv4Address(browserIP) && !isIPv6Address(browserIP)) {
            return ConstraintValidationResult.failure(PATTERN, "browserIP");
        }

        return ConstraintValidationResult.success();
    }
}
