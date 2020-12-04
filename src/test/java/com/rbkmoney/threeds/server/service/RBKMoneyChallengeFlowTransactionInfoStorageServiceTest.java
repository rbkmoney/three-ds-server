package com.rbkmoney.threeds.server.service;

import com.rbkmoney.damsel.threeds.server.storage.ChallengeFlowTransactionInfoStorageSrv;
import com.rbkmoney.threeds.server.converter.thrift.ChallengeFlowTransactionInfoConverter;
import com.rbkmoney.threeds.server.domain.acs.AcsDecConInd;
import com.rbkmoney.threeds.server.domain.device.DeviceChannel;
import com.rbkmoney.threeds.server.ds.DsProvider;
import com.rbkmoney.threeds.server.dto.ChallengeFlowTransactionInfo;
import com.rbkmoney.threeds.server.service.rbkmoneyplatform.RBKMoneyChallengeFlowTransactionInfoStorageService;
import org.apache.thrift.TException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class RBKMoneyChallengeFlowTransactionInfoStorageServiceTest {

    private static final String TEST_TAG = UUID.randomUUID().toString();

    private ChallengeFlowTransactionInfoStorageSrv.Iface challengeFlowTransactionInfoStorageClient;
    private RBKMoneyChallengeFlowTransactionInfoStorageService rbkMoneyChallengeFlowTransactionInfoStorageService;

    @BeforeEach
    public void setUp() {
        challengeFlowTransactionInfoStorageClient = mock(ChallengeFlowTransactionInfoStorageSrv.Iface.class);
        rbkMoneyChallengeFlowTransactionInfoStorageService = new RBKMoneyChallengeFlowTransactionInfoStorageService(
                challengeFlowTransactionInfoStorageClient,
                new ChallengeFlowTransactionInfoConverter(),
                100
        );
    }

    @Test
    public void shouldSaveAndGetTransactionInfo() throws TException {
        ChallengeFlowTransactionInfo transactionInfo = ChallengeFlowTransactionInfo.builder()
                .threeDSServerTransID(TEST_TAG)
                .deviceChannel(DeviceChannel.APP_BASED)
                .decoupledAuthMaxTime(LocalDateTime.MIN)
                .acsDecConInd(AcsDecConInd.DECOUPLED_AUTH_WILL_BE_USED)
                .dsProviderId(DsProvider.MASTERCARD.getId())
                .messageVersion("2.1.0")
                .acsUrl("asd")
                .build();

        ArgumentCaptor<com.rbkmoney.damsel.threeds.server.storage.ChallengeFlowTransactionInfo> expected =
                ArgumentCaptor.forClass(com.rbkmoney.damsel.threeds.server.storage.ChallengeFlowTransactionInfo.class);

        rbkMoneyChallengeFlowTransactionInfoStorageService.saveChallengeFlowTransactionInfo(TEST_TAG, transactionInfo);
        ChallengeFlowTransactionInfo result = rbkMoneyChallengeFlowTransactionInfoStorageService.getChallengeFlowTransactionInfo(TEST_TAG);

        assertThat(result).isEqualTo(transactionInfo);

        verify(challengeFlowTransactionInfoStorageClient, only()).saveChallengeFlowTransactionInfo(expected.capture());

        var saved = expected.getValue();
        assertThat(saved.getTransactionId())
                .isEqualTo(TEST_TAG);
        assertThat(saved.getAcsDecConInd())
                .isEqualTo(transactionInfo.getAcsDecConInd().getValue());
        assertThat(saved.getDecoupledAuthMaxTime())
                .isEqualTo(transactionInfo.getDecoupledAuthMaxTime().toString());
        assertThat(saved.getDeviceChannel())
                .isEqualTo(transactionInfo.getDeviceChannel().getValue());
    }

    @Test
    public void shouldGetTransactionInfoWhenCacheIsEmpty() throws TException {
        var stored = new com.rbkmoney.damsel.threeds.server.storage.ChallengeFlowTransactionInfo()
                .setTransactionId(TEST_TAG)
                .setDeviceChannel(DeviceChannel.APP_BASED.getValue())
                .setDecoupledAuthMaxTime(LocalDateTime.MIN.toString())
                .setAcsDecConInd(AcsDecConInd.DECOUPLED_AUTH_WILL_BE_USED.getValue())
                .setProviderId("visa")
                .setMessageVersion("2.1.0")
                .setAcsUrl("asd");

        when(challengeFlowTransactionInfoStorageClient.getChallengeFlowTransactionInfo(TEST_TAG))
                .thenReturn(stored);

        ChallengeFlowTransactionInfo result = rbkMoneyChallengeFlowTransactionInfoStorageService.getChallengeFlowTransactionInfo(TEST_TAG);

        verify(challengeFlowTransactionInfoStorageClient, only())
                .getChallengeFlowTransactionInfo(TEST_TAG);
        assertThat(result.getAcsDecConInd())
                .isEqualTo(AcsDecConInd.DECOUPLED_AUTH_WILL_BE_USED);
        assertThat(result.getDecoupledAuthMaxTime())
                .isEqualTo(LocalDateTime.MIN);
        assertThat(result.getDeviceChannel())
                .isEqualTo(DeviceChannel.APP_BASED);
    }
}
