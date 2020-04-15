package com.rbkmoney.threeds.server.domain.message;

import com.fasterxml.jackson.annotation.JsonValue;
import com.rbkmoney.threeds.server.domain.Valuable;
import lombok.RequiredArgsConstructor;

/**
 * Identifies the type of message that is passed.
 */
@RequiredArgsConstructor
public enum MessageType implements Valuable {

    ERROR("Erro"),

    AREQ("AReq"),

    ARES("ARes"),

    CREQ("CReq"),

    CRES("CRes"),

    PREQ("PReq"),

    PRES("PRes"),

    RREQ("RReq"),

    RRES("RRes"),

    PARQ("pArq"),

    PARS("pArs"),

    PGCQ("pGcq"),

    PGCS("pGcs"),

    PIRQ("pIrq"),

    PIRS("pIrs"),

    PORQ("pOrq"),

    PORS("pOrs"),

    PPRQ("pPrq"),

    PPRS("pPrs"),

    PSRQ("pSrq"),

    PSRS("pSrs"),

    UNKN("Unkn");

    private final String value;

    @JsonValue
    @Override
    public String getValue() {
        return value;
    }
}
