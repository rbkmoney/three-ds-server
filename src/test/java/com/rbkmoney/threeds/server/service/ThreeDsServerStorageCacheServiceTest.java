package com.rbkmoney.threeds.server.service;

import com.rbkmoney.damsel.three_ds_server_storage.CardRangesStorageSrv;
import com.rbkmoney.damsel.three_ds_server_storage.ChallengeFlowTransactionInfoStorageSrv;
import com.rbkmoney.damsel.three_ds_server_storage.GetCardRangesResponse;
import com.rbkmoney.threeds.server.converter.thrift.CardRangesConverter;
import com.rbkmoney.threeds.server.converter.thrift.ChallengeFlowTransactionInfoConverter;
import com.rbkmoney.threeds.server.domain.acs.AcsDecConInd;
import com.rbkmoney.threeds.server.domain.device.DeviceChannel;
import com.rbkmoney.threeds.server.dto.ChallengeFlowTransactionInfo;
import com.rbkmoney.threeds.server.service.cache.CacheService;
import com.rbkmoney.threeds.server.service.cache.ThreeDsServerStorageCacheService;
import org.apache.thrift.TException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDateTime;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.*;

public class ThreeDsServerStorageCacheServiceTest {

    private static final String TEST_TAG = "TEST_TAG";

    private CardRangesStorageSrv.Iface cardRangesStorageClient;
    private ChallengeFlowTransactionInfoStorageSrv.Iface challengeFlowTransactionInfoStorageClient;

    private CacheService cacheService;

    @Before
    public void setUp() {
        challengeFlowTransactionInfoStorageClient = mock(ChallengeFlowTransactionInfoStorageSrv.Iface.class);
        cardRangesStorageClient = mock(CardRangesStorageSrv.Iface.class);
        cacheService = new ThreeDsServerStorageCacheService(
                challengeFlowTransactionInfoStorageClient,
                new ChallengeFlowTransactionInfoConverter(),
                100L,
                cardRangesStorageClient,
                new CardRangesConverter(),
                1L);
    }

    @Test
    public void shouldSaveAndGetTransactionInfo() throws TException {
        // Given
        ChallengeFlowTransactionInfo transactionInfo = ChallengeFlowTransactionInfo.builder()
                .acsDecConInd(AcsDecConInd.DECOUPLED_AUTH_WILL_BE_USED)
                .decoupledAuthMaxTime(LocalDateTime.MIN)
                .deviceChannel(DeviceChannel.APP_BASED)
                .build();

        ArgumentCaptor<com.rbkmoney.damsel.three_ds_server_storage.ChallengeFlowTransactionInfo> expected =
                ArgumentCaptor.forClass(com.rbkmoney.damsel.three_ds_server_storage.ChallengeFlowTransactionInfo.class);

        // When
        cacheService.saveChallengeFlowTransactionInfo(TEST_TAG, transactionInfo);
        ChallengeFlowTransactionInfo result = cacheService.getChallengeFlowTransactionInfo(TEST_TAG);

        // Then
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
        // Given
        var stored = new com.rbkmoney.damsel.three_ds_server_storage.ChallengeFlowTransactionInfo()
                .setTransactionId(TEST_TAG)
                .setAcsDecConInd(AcsDecConInd.DECOUPLED_AUTH_WILL_BE_USED.getValue())
                .setDecoupledAuthMaxTime(LocalDateTime.MIN.toString())
                .setDeviceChannel(DeviceChannel.APP_BASED.getValue());

        when(challengeFlowTransactionInfoStorageClient.getChallengeFlowTransactionInfo(TEST_TAG))
                .thenReturn(stored);

        // When
        ChallengeFlowTransactionInfo result = cacheService.getChallengeFlowTransactionInfo(TEST_TAG);

        // Then
        verify(challengeFlowTransactionInfoStorageClient, only())
                .getChallengeFlowTransactionInfo(TEST_TAG);
        assertThat(result.getAcsDecConInd())
                .isEqualTo(AcsDecConInd.DECOUPLED_AUTH_WILL_BE_USED);
        assertThat(result.getDecoupledAuthMaxTime())
                .isEqualTo(LocalDateTime.MIN);
        assertThat(result.getDeviceChannel())
                .isEqualTo(DeviceChannel.APP_BASED);
    }

    @Test
    public void shouldReturnFalseIfBothCachedAndStoredCardRangesAreEmpty() throws TException {
        // Given
        String acctNumber = "0000";

        when(cardRangesStorageClient.getCardRanges(any()))
                .thenReturn(new GetCardRangesResponse().setCardRanges(emptyList()));

        // When
        boolean isInCardRange = cacheService.isInCardRange(TEST_TAG, acctNumber);

        // Then
        assertFalse(isInCardRange);
    }
}
