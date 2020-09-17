package com.rbkmoney.threeds.server.serializer;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.SerializationException;
import org.apache.thrift.TBase;
import org.apache.thrift.TException;
import org.apache.thrift.TSerializer;

@Slf4j
@NoArgsConstructor
public class ThriftSerializer<T extends TBase> {

    private final ThreadLocal<TSerializer> thriftSerializer = ThreadLocal.withInitial(TSerializer::new);

    public byte[] serialize(T thrift) {
        try {
            return this.thriftSerializer.get().serialize(thrift);
        } catch (TException e) {
            log.error("Exception when trying to serialize thrift={} ", thrift, e);
            throw new SerializationException(e);
        }
    }
}
