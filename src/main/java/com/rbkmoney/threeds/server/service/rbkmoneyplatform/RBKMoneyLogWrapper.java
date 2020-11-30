package com.rbkmoney.threeds.server.service.rbkmoneyplatform;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.domain.root.emvco.AReq;
import com.rbkmoney.threeds.server.domain.root.emvco.PReq;
import com.rbkmoney.threeds.server.domain.root.emvco.RReq;
import com.rbkmoney.threeds.server.ds.rbkmoneyplatform.RBKMoneyDsProviderHolder;
import com.rbkmoney.threeds.server.service.LogWrapper;
import com.rbkmoney.threeds.server.utils.AccountNumberUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public class RBKMoneyLogWrapper implements LogWrapper {

    private final RBKMoneyDsProviderHolder rbkMoneyDsProviderHolder;
    private final ObjectMapper objectMapper;
    private final Gson gson;

    @Override
    @SneakyThrows
    public void info(String message, Message data) {
        // записываются чистым json только те сообщения в лог, которые связаны с запросами из/в DS
        if (data instanceof PReq || data instanceof AReq || data instanceof RReq) {
            String json = objectMapper.writeValueAsString(data);

            JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
            // удаляется часть логируемых параметров, которые могут потенциально связаться в определение персональных данных,
            // но которые, при этом не сильно будут нужны и нам и платежным системам (при их запросе)
            jsonObject.remove("shipAddrCity");
            jsonObject.remove("shipAddrCountry");
            jsonObject.remove("billAddrCountry");
            jsonObject.remove("billAddrCity");
            jsonObject.remove("mobilePhone");
            jsonObject.remove("homePhone");
            jsonObject.remove("workPhone");
            jsonObject.remove("email");
            jsonObject.remove("browserIP");
            jsonObject.remove("cardholderName");

            Optional<String> acctNumber = Optional.ofNullable(jsonObject.get("acctNumber")).map(JsonElement::getAsString);
            if (acctNumber.isPresent()) {
                jsonObject.remove("acctNumber");
                jsonObject.addProperty("acctNumber", AccountNumberUtils.hideAccountNumber(acctNumber.get()));
            }

            jsonObject.remove("authenticationValue");

            log.info("{}: dsProviderId={}, {}", message, rbkMoneyDsProviderHolder.getDsProvider().toString(), jsonObject.toString());
        } else {
            log.info("{}: dsProviderId={}, {}", message, rbkMoneyDsProviderHolder.getDsProvider().toString(), data);
        }
    }

    @Override
    public void warn(String message, Throwable ex) {
        log.warn(String.format("%s: dsProviderId=%s", message, rbkMoneyDsProviderHolder.getDsProvider().toString()), ex);
    }
}
