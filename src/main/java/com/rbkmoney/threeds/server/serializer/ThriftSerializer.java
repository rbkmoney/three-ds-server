package com.rbkmoney.threeds.server.serializer;

import lombok.NoArgsConstructor;
import org.apache.commons.lang.SerializationException;
import org.apache.thrift.TBase;
import org.apache.thrift.TException;
import org.apache.thrift.TSerializer;

@NoArgsConstructor
public class ThriftSerializer<T extends TBase> {

    public byte[] serialize(T thrift) {
        try {
            return new TSerializer().serialize(thrift);
        } catch (TException e) {
            throw new SerializationException(e);
        }
    }
}
