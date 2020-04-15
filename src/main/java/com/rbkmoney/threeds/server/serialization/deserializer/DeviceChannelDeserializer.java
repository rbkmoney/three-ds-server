package com.rbkmoney.threeds.server.serialization.deserializer;

import com.rbkmoney.threeds.server.domain.device.DeviceChannel;
import com.rbkmoney.threeds.server.serialization.AbstractEnumDeserializer;

public class DeviceChannelDeserializer extends AbstractEnumDeserializer<DeviceChannel> {

    @Override
    protected DeviceChannel enumValueOf(String candidate) {
        return DeviceChannel.valueOf(candidate);
    }

    @Override
    protected DeviceChannel[] enumValues() {
        return DeviceChannel.values();
    }
}
