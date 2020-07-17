package com.rbkmoney.threeds.server.utils;

import com.rbkmoney.threeds.server.config.properties.EnvironmentProperties;
import com.rbkmoney.threeds.server.domain.device.DeviceChannel;
import com.rbkmoney.threeds.server.domain.threedsrequestor.ThreeDSRequestorChallengeInd;

public class CrutchChecker {

    public static boolean isMirCrutchCondition(DeviceChannel deviceChannel, EnvironmentProperties environmentProperties) {
        return deviceChannel == DeviceChannel.THREE_REQUESTOR_INITIATED
                // здесь уканазан RefNumber нспк для тестовой среды (todo если отличается, добавить RefNumber от боевой среды)
                && environmentProperties.getThreeDsServerRefNumber().equals("2200040105");
    }

    public static boolean isVisaCrutchCondition(
            ThreeDSRequestorChallengeInd threeDSRequestorChallengeInd,
            EnvironmentProperties environmentProperties) {
        return threeDSRequestorChallengeInd == ThreeDSRequestorChallengeInd.RESERVED_FOR_DS_USED_82
                && environmentProperties.getThreeDsServerRefNumber().equals("3DS_LOA_SER_DIPL_020200_00236");
    }
}
