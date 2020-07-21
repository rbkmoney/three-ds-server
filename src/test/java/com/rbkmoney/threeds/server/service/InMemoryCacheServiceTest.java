package com.rbkmoney.threeds.server.service;

import com.rbkmoney.threeds.server.domain.acs.AcsDecConInd;
import com.rbkmoney.threeds.server.domain.cardrange.ActionInd;
import com.rbkmoney.threeds.server.domain.cardrange.CardRange;
import com.rbkmoney.threeds.server.domain.device.DeviceChannel;
import com.rbkmoney.threeds.server.dto.ChallengeFlowTransactionInfo;
import com.rbkmoney.threeds.server.service.cache.InMemoryCacheService;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.List;

import static com.rbkmoney.threeds.server.helper.CardRangeHelper.cardRange;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

public class InMemoryCacheServiceTest {

    private static final String TEST_TAG = "TEST_TAG";
    private static final String TRAP = "TRAP";

    private CacheService cacheService;

    @Before
    public void setUp() {
        cacheService = new InMemoryCacheService(100L);
    }

    @Test
    public void shouldSaveAndGetSerialNum() {
        // Given
        String serialNum = "1";
        cacheService.saveSerialNum(TRAP, TRAP);

        // When
        cacheService.saveSerialNum(TEST_TAG, serialNum);
        String result = cacheService.getSerialNum(TEST_TAG);

        // Then
        assertThat(result).isEqualTo(serialNum);
    }

    @Test
    public void shouldClearSerialNum() {
        // Given
        cacheService.saveSerialNum(TEST_TAG, "1");
        cacheService.saveSerialNum(TRAP, TRAP);

        // When
        cacheService.clearSerialNum(TEST_TAG);
        String result = cacheService.getSerialNum(TEST_TAG);

        // Then
        assertNull(result);
        assertThat(cacheService.getSerialNum(TRAP)).isEqualTo(TRAP);
    }

    @Test
    public void shouldSaveAndGetTransactionInfo() {
        // Given
        ChallengeFlowTransactionInfo transactionInfo = ChallengeFlowTransactionInfo.builder()
                .acsDecConInd(AcsDecConInd.DECOUPLED_AUTH_WILL_BE_USED)
                .decoupledAuthMaxTime(LocalDateTime.MIN)
                .deviceChannel(DeviceChannel.APP_BASED)
                .build();

        cacheService.saveChallengeFlowTransactionInfo(TRAP, ChallengeFlowTransactionInfo.builder().build());

        // When
        cacheService.saveChallengeFlowTransactionInfo(TEST_TAG, transactionInfo);
        ChallengeFlowTransactionInfo result = cacheService.getChallengeFlowTransactionInfo(TEST_TAG);

        // Then
        assertThat(result).isEqualTo(transactionInfo);
    }

    @Test
    public void shouldReturnTrueIfCachedCardRangesAreEmpty() {
        // Given
        String acctNumber = "0000";

        // When
        boolean isInCardRange = cacheService.isInCardRange(TEST_TAG, acctNumber);

        // Then
        assertTrue(isInCardRange);
    }

    @Test
    public void shouldReturnTrueForAcctNumberInRange() {
        // Given
        List<CardRange> cachedCardRanges = List.of(
                cardRange(ActionInd.ADD_CARD_RANGE_TO_CACHE, "1000", "1099"),
                cardRange(ActionInd.ADD_CARD_RANGE_TO_CACHE, "1112", "1120"));
        cacheService.updateCardRanges(TEST_TAG, cachedCardRanges);

        String acctNumber = "1099";

        // When
        boolean isInCardRange = cacheService.isInCardRange(TEST_TAG, acctNumber);

        // Then
        assertTrue(isInCardRange);
    }

    @Test
    public void shouldReturnFalseForAcctNumberNotInRange() {
        // Given
        List<CardRange> cachedCardRanges = List.of(
                cardRange(ActionInd.ADD_CARD_RANGE_TO_CACHE, "1000", "1099"),
                cardRange(ActionInd.ADD_CARD_RANGE_TO_CACHE, "1112", "1120"));
        cacheService.updateCardRanges(TEST_TAG, cachedCardRanges);

        String acctNumber = "1111";

        // When
        boolean isInCardRange = cacheService.isInCardRange(TEST_TAG, acctNumber);

        // Then
        assertFalse(isInCardRange);
    }

    @Test
    public void shouldAddNewCardRanges() {
        // Given
        List<CardRange> newCardRanges = List.of(
                cardRange(ActionInd.ADD_CARD_RANGE_TO_CACHE, "1000", "1099"));

        // When
        cacheService.updateCardRanges(TEST_TAG, newCardRanges);
        boolean isAdded = cacheService.isInCardRange(TEST_TAG, "1050");

        // Then
        assertTrue(isAdded);
    }

    @Test
    public void shouldModifyExistingCardRanges() {
        // Given
        List<CardRange> existingCardRanges = List.of(
                cardRange(ActionInd.ADD_CARD_RANGE_TO_CACHE, "1000", "1099"));
        cacheService.updateCardRanges(TEST_TAG, existingCardRanges);

        List<CardRange> modification = List.of(
                cardRange(ActionInd.MODIFY_CARD_RANGE_DATA, "1000", "1099"));

        // When
        cacheService.updateCardRanges(TEST_TAG, modification);
        boolean isModified = cacheService.isInCardRange(TEST_TAG, "1050");

        // Then
        assertTrue(isModified);
    }

    @Test
    public void shouldDeleteExistingCardRanges() {
        // Given
        List<CardRange> existingCardRanges = List.of(
                cardRange(ActionInd.ADD_CARD_RANGE_TO_CACHE, "0000", "999"),
                cardRange(ActionInd.ADD_CARD_RANGE_TO_CACHE, "1000", "1099"));
        cacheService.updateCardRanges(TEST_TAG, existingCardRanges);

        List<CardRange> deletion = List.of(
                cardRange(ActionInd.DELETE_CARD_RANGE_FROM_CACHE, "1000", "1099"));

        // When
        cacheService.updateCardRanges(TEST_TAG, deletion);
        boolean isDeleted = !cacheService.isInCardRange(TEST_TAG, "1050");

        // Then
        assertTrue(isDeleted);
    }
}
