package com.rbkmoney.threeds.server.service;

import com.rbkmoney.threeds.server.domain.ActionInd;
import com.rbkmoney.threeds.server.domain.CardRange;
import com.rbkmoney.threeds.server.domain.acs.AcsDecConInd;
import com.rbkmoney.threeds.server.domain.device.DeviceChannel;
import com.rbkmoney.threeds.server.dto.RReqTransactionInfo;
import com.rbkmoney.threeds.server.serialization.EnumWrapper;
import com.rbkmoney.threeds.server.service.impl.CacheServiceImpl;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


public class CacheServiceTest {

    private static final String TEST_TAG = "TEST_TAG";
    private static final String TRAP = "TRAP";

    private CacheService cacheService;

    @Before
    public void setUp() {
        cacheService = new CacheServiceImpl();
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
        assertThat(result).isNull();
        assertThat(cacheService.getSerialNum(TRAP)).isEqualTo(TRAP);
    }

    @Test
    public void shouldSaveAndGetRReqTransactionInfo() {
        // Given
        RReqTransactionInfo transactionInfo = RReqTransactionInfo.builder()
                .acsDecConInd(AcsDecConInd.DECOUPLED_AUTH_WILL_BE_USED)
                .decoupledAuthMaxTime(LocalDateTime.MIN)
                .deviceChannel(DeviceChannel.APP_BASED)
                .build();

        cacheService.saveRReqTransactionInfo(TRAP, RReqTransactionInfo.builder().build());

        // When
        cacheService.saveRReqTransactionInfo(TEST_TAG, transactionInfo);
        RReqTransactionInfo result = cacheService.getRReqTransactionInfo(TEST_TAG);

        // Then
        assertThat(result).isEqualTo(transactionInfo);
    }

    @Test
    public void shouldClearRReqTransactionInfo() {
        // Given
        RReqTransactionInfo transactionInfo = RReqTransactionInfo.builder()
                .acsDecConInd(AcsDecConInd.DECOUPLED_AUTH_WILL_BE_USED)
                .decoupledAuthMaxTime(LocalDateTime.MIN)
                .deviceChannel(DeviceChannel.APP_BASED)
                .build();

        cacheService.saveRReqTransactionInfo(TEST_TAG, transactionInfo);
        cacheService.saveRReqTransactionInfo(TRAP, RReqTransactionInfo.builder().build());

        // When
        cacheService.clearRReqTransactionInfo(TEST_TAG);
        RReqTransactionInfo result = cacheService.getRReqTransactionInfo(TEST_TAG);

        // Then
        assertThat(result).isNull();
        assertThat(cacheService.getRReqTransactionInfo(TRAP)).isNotNull();
    }

    @Test
    public void shouldReturnValidForCardRangeWithNullActionInd() {
        // Given
        CardRange cardRange = new CardRange();
        cardRange.setActionInd(null);

        // When
        boolean isValid = cacheService.isValidCardRange(TEST_TAG, cardRange);

        // Then
        assertThat(isValid).isTrue();
    }

    @Test
    public void shouldReturnValidIfCachedCardRangesAreEmpty() {
        // Given
        CardRange addRange = cardRange(ActionInd.ADD_CARD_RANGE_TO_CACHE, "0000", "1111");
        CardRange modifyRange = cardRange(ActionInd.MODIFY_CARD_RANGE_DATA, "0000", "1111");
        CardRange deleteRange = cardRange(ActionInd.DELETE_CARD_RANGE_FROM_CACHE, "0000", "1111");

        // When
        boolean isAddValid = cacheService.isValidCardRange(TEST_TAG, addRange);
        boolean isModifyValid = cacheService.isValidCardRange(TEST_TAG, modifyRange);
        boolean isDeleteValid = cacheService.isValidCardRange(TEST_TAG, deleteRange);

        // Then
        assertThat(isAddValid).isTrue();
        assertThat(isModifyValid).isTrue();
        assertThat(isDeleteValid).isTrue();
    }

    @Test
    public void shouldReturnValidForAddCardRange() {
        // Given
        List<CardRange> cachedCardRanges = List.of(
                cardRange(ActionInd.ADD_CARD_RANGE_TO_CACHE, "1000", "1099"),
                cardRange(ActionInd.ADD_CARD_RANGE_TO_CACHE, "1112", "1120"));
        cacheService.updateCardRanges(TEST_TAG, cachedCardRanges);

        CardRange addRange = cardRange(ActionInd.ADD_CARD_RANGE_TO_CACHE, "1100", "1111");

        // When
        boolean isValid = cacheService.isValidCardRange(TEST_TAG, addRange);

        // Then
        assertThat(isValid).isTrue();
    }

    @Test
    public void shouldReturnInvalidForClashingAddCardRange() {
        // Given
        List<CardRange> cachedCardRanges = List.of(
                cardRange(ActionInd.ADD_CARD_RANGE_TO_CACHE, "1000", "1099"),
                cardRange(ActionInd.ADD_CARD_RANGE_TO_CACHE, "1112", "1120"));
        cacheService.updateCardRanges(TEST_TAG, cachedCardRanges);

        CardRange addRange = cardRange(ActionInd.ADD_CARD_RANGE_TO_CACHE, "1099", "1114");

        // When
        boolean isValid = cacheService.isValidCardRange(TEST_TAG, addRange);

        // Then
        assertThat(isValid).isFalse();
    }

    @Test
    public void shouldReturnValidForModifyCardRange() {
        // Given
        List<CardRange> cachedCardRanges = List.of(
                cardRange(ActionInd.ADD_CARD_RANGE_TO_CACHE, "1000", "1099"),
                cardRange(ActionInd.ADD_CARD_RANGE_TO_CACHE, "1112", "1120"));
        cacheService.updateCardRanges(TEST_TAG, cachedCardRanges);

        CardRange addRange = cardRange(ActionInd.MODIFY_CARD_RANGE_DATA, "1000", "1099");

        // When
        boolean isValid = cacheService.isValidCardRange(TEST_TAG, addRange);

        // Then
        assertThat(isValid).isTrue();
    }

    @Test
    public void shouldReturnInvalidForMissingModifyCardRange() {
        // Given
        List<CardRange> cachedCardRanges = List.of(
                cardRange(ActionInd.ADD_CARD_RANGE_TO_CACHE, "1000", "1099"),
                cardRange(ActionInd.ADD_CARD_RANGE_TO_CACHE, "1112", "1120"));
        cacheService.updateCardRanges(TEST_TAG, cachedCardRanges);

        CardRange addRange = cardRange(ActionInd.MODIFY_CARD_RANGE_DATA, "1000", "1112");

        // When
        boolean isValid = cacheService.isValidCardRange(TEST_TAG, addRange);

        // Then
        assertThat(isValid).isFalse();
    }

    @Test
    public void shouldReturnValidForDeleteCardRange() {
        // Given
        List<CardRange> cachedCardRanges = List.of(
                cardRange(ActionInd.ADD_CARD_RANGE_TO_CACHE, "1000", "1099"),
                cardRange(ActionInd.ADD_CARD_RANGE_TO_CACHE, "1112", "1120"));
        cacheService.updateCardRanges(TEST_TAG, cachedCardRanges);

        CardRange addRange = cardRange(ActionInd.DELETE_CARD_RANGE_FROM_CACHE, "1000", "1099");

        // When
        boolean isValid = cacheService.isValidCardRange(TEST_TAG, addRange);

        // Then
        assertThat(isValid).isTrue();
    }

    @Test
    public void shouldReturnInvalidForMissingDeleteCardRange() {
        // Given
        List<CardRange> cachedCardRanges = List.of(
                cardRange(ActionInd.ADD_CARD_RANGE_TO_CACHE, "1000", "1099"),
                cardRange(ActionInd.ADD_CARD_RANGE_TO_CACHE, "1112", "1120"));
        cacheService.updateCardRanges(TEST_TAG, cachedCardRanges);

        CardRange addRange = cardRange(ActionInd.DELETE_CARD_RANGE_FROM_CACHE, "1000", "1112");

        // When
        boolean isValid = cacheService.isValidCardRange(TEST_TAG, addRange);

        // Then
        assertThat(isValid).isFalse();
    }

    @Test
    public void shouldReturnTrueIfCachedCardRangesAreEmpty() {
        // Given
        String acctNumber = "0000";

        // When
        boolean isInCardRange = cacheService.isInCardRange(TEST_TAG, acctNumber);

        // Then
        assertThat(isInCardRange).isTrue();
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
        assertThat(isInCardRange).isTrue();
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
        assertThat(isInCardRange).isFalse();
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
        assertThat(isAdded).isTrue();
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
        assertThat(isModified).isTrue();
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
        assertThat(isDeleted).isTrue();
    }

    private CardRange cardRange(
            ActionInd actionInd,
            String startRange,
            String endRange) {
        EnumWrapper<ActionInd> action = new EnumWrapper<>();
        action.setValue(actionInd);

        CardRange cardRange = new CardRange();
        cardRange.setActionInd(action);
        cardRange.setStartRange(startRange);
        cardRange.setEndRange(endRange);

        return cardRange;
    }
}
