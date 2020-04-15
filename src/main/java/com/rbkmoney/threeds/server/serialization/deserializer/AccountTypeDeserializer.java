package com.rbkmoney.threeds.server.serialization.deserializer;

import com.rbkmoney.threeds.server.domain.account.AccountType;
import com.rbkmoney.threeds.server.serialization.AbstractEnumDeserializer;

public class AccountTypeDeserializer extends AbstractEnumDeserializer<AccountType> {

    @Override
    protected AccountType enumValueOf(String candidate) {
        return AccountType.valueOf(candidate);
    }

    @Override
    protected AccountType[] enumValues() {
        return AccountType.values();
    }
}
