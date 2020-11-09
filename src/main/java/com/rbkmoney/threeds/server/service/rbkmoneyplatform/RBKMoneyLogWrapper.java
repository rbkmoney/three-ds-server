package com.rbkmoney.threeds.server.service.rbkmoneyplatform;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.rbkmoney.threeds.server.domain.root.Message;
import com.rbkmoney.threeds.server.domain.root.emvco.AReq;
import com.rbkmoney.threeds.server.domain.root.emvco.PReq;
import com.rbkmoney.threeds.server.domain.root.emvco.PRes;
import com.rbkmoney.threeds.server.domain.root.emvco.RReq;
import com.rbkmoney.threeds.server.ds.DsProviderHolder;
import com.rbkmoney.threeds.server.service.LogWrapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class RBKMoneyLogWrapper implements LogWrapper {

    private final DsProviderHolder dsProviderHolder;
    private final ObjectMapper objectMapper;
    private final Gson gson;

    @Override
    @SneakyThrows
    public void info(String message, Message data) {
        String dsProviderId = dsProviderHolder.getDsProvider().orElse(null);

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

            jsonObject.remove("authenticationValue");

            json = jsonObject.toString();

            log(message, dsProviderId, json);
        } else if (data instanceof PRes) {
            log(message, dsProviderId, data.toString() + ", cardRanges=" + ((PRes) data).getCardRangeData().size());
        } else {
            log(message, dsProviderId, data.toString());
        }
    }

    @Override
    public void warn(String message, Throwable ex) {
        String dsProviderId = dsProviderHolder.getDsProvider().orElse(null);
        if (dsProviderId != null) {
            log.warn(String.format("%s: dsProviderId=%s", message, dsProviderId), ex);
        } else {
            log.warn(message, ex);
        }
    }

    private void log(String message, String dsProviderId, String data) {
        if (dsProviderId != null) {
            log.info("{}: dsProviderId={}, {}", message, dsProviderId, data);
        } else {
            log.info("{}: {}", message, data);
        }
    }
}