package com.rbkmoney.threeds.server.service;

import com.rbkmoney.threeds.server.domain.cardrange.ActionInd;
import com.rbkmoney.threeds.server.domain.cardrange.CardRange;
import com.rbkmoney.threeds.server.service.testplatform.TestPlatformCardRangesStorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static com.rbkmoney.threeds.server.helper.CardRangeHelper.cardRange;
import static java.util.List.of;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestPlatformCardRangesStorageServiceTest {

    private static final String TEST_TAG = UUID.randomUUID().toString();

    private TestPlatformCardRangesStorageService cardRangesStorageService;

    @BeforeEach
    public void setUp() {
        cardRangesStorageService = new TestPlatformCardRangesStorageService();
    }

    @Test
    public void shouldReturnTrueIfCachedCardRangesAreEmpty() {
        // Given
        String acctNumber = "0000";

        // When
        boolean isInCardRange = cardRangesStorageService.isInCardRange(TEST_TAG, acctNumber);

        // Then
        assertTrue(isInCardRange);
    }

    @Test
    public void shouldReturnTrueForAcctNumberInRange() {
        // Given
        List<CardRange> cachedCardRanges = of(
                cardRange(ActionInd.ADD_CARD_RANGE_TO_CACHE, "1000", "1099"),
                cardRange(ActionInd.ADD_CARD_RANGE_TO_CACHE, "1112", "1120"));
        cardRangesStorageService.updateCardRanges(TEST_TAG, cachedCardRanges);

        String acctNumber = "1099";

        // When
        boolean isInCardRange = cardRangesStorageService.isInCardRange(TEST_TAG, acctNumber);

        // Then
        assertTrue(isInCardRange);
    }

    @Test
    public void shouldReturnFalseForAcctNumberNotInRange() {
        // Given
        List<CardRange> cachedCardRanges = of(
                cardRange(ActionInd.ADD_CARD_RANGE_TO_CACHE, "1000", "1099"),
                cardRange(ActionInd.ADD_CARD_RANGE_TO_CACHE, "1112", "1120"));
        cardRangesStorageService.updateCardRanges(TEST_TAG, cachedCardRanges);

        String acctNumber = "1111";

        // When
        boolean isInCardRange = cardRangesStorageService.isInCardRange(TEST_TAG, acctNumber);

        // Then
        assertFalse(isInCardRange);
    }

    @Test
    public void shouldAddNewCardRanges() {
        // Given
        List<CardRange> newCardRanges = of(
                cardRange(ActionInd.ADD_CARD_RANGE_TO_CACHE, "1000", "1099"));

        // When
        cardRangesStorageService.updateCardRanges(TEST_TAG, newCardRanges);
        boolean isAdded = cardRangesStorageService.isInCardRange(TEST_TAG, "1050");

        // Then
        assertTrue(isAdded);
    }

    @Test
    public void shouldModifyExistingCardRanges() {
        // Given
        List<CardRange> existingCardRanges = of(
                cardRange(ActionInd.ADD_CARD_RANGE_TO_CACHE, "1000", "1099"));
        cardRangesStorageService.updateCardRanges(TEST_TAG, existingCardRanges);

        List<CardRange> modification = of(
                cardRange(ActionInd.MODIFY_CARD_RANGE_DATA, "1000", "1099"));

        // When
        cardRangesStorageService.updateCardRanges(TEST_TAG, modification);
        boolean isModified = cardRangesStorageService.isInCardRange(TEST_TAG, "1050");

        // Then
        assertTrue(isModified);
    }

    @Test
    public void shouldDeleteExistingCardRanges() {
        // Given
        List<CardRange> existingCardRanges = of(
                cardRange(ActionInd.ADD_CARD_RANGE_TO_CACHE, "0000", "999"),
                cardRange(ActionInd.ADD_CARD_RANGE_TO_CACHE, "1000", "1099"));

        // When
        cardRangesStorageService.updateCardRanges(TEST_TAG, existingCardRanges);
        boolean isAdded = cardRangesStorageService.isInCardRange(TEST_TAG, "1050");

        // Then
        assertTrue(isAdded);

        List<CardRange> deletion = of(
                cardRange(ActionInd.DELETE_CARD_RANGE_FROM_CACHE, "1000", "1099"));

        // When
        cardRangesStorageService.updateCardRanges(TEST_TAG, deletion);
        boolean isDeleted = !cardRangesStorageService.isInCardRange(TEST_TAG, "1050");

        // Then
        assertTrue(isDeleted);
    }

    @Test
    public void shouldReturnValidForCardRangeWithNullActionInd() {
        // Given
        CardRange cardRange = new CardRange();
        cardRange.setActionInd(null);

        // When
        boolean isValid = cardRangesStorageService.isValidCardRanges(TEST_TAG, of(cardRange));

        // Then
        assertTrue(isValid);
    }

    @Test
    public void shouldReturnValidIfStorageCardRangesAreEmpty() {
        // Given
        List<CardRange> cardRanges = of(cardRange(ActionInd.ADD_CARD_RANGE_TO_CACHE, "0000", "1111"),
                cardRange(ActionInd.MODIFY_CARD_RANGE_DATA, "0000", "1111"),
                cardRange(ActionInd.DELETE_CARD_RANGE_FROM_CACHE, "0000", "1111"));

        // When
        boolean isValid = cardRangesStorageService.isValidCardRanges(TEST_TAG, cardRanges);

        // Then
        assertTrue(isValid);
    }

    @Test
    public void shouldReturnValidForAddCardRange() {
        // Given
        List<CardRange> cardRanges = of(
                cardRange(ActionInd.ADD_CARD_RANGE_TO_CACHE, "1000", "1099"),
                cardRange(ActionInd.ADD_CARD_RANGE_TO_CACHE, "1112", "1120"));
        cardRangesStorageService.updateCardRanges(TEST_TAG, cardRanges);

        CardRange addRange = cardRange(ActionInd.ADD_CARD_RANGE_TO_CACHE, "1100", "1111");

        // When
        boolean isValid = cardRangesStorageService.isValidCardRanges(TEST_TAG, of(addRange));

        // Then
        assertTrue(isValid);
    }

    @Test
    public void shouldReturnInvalidForClashingAddCardRange() {
        // Given
        List<CardRange> cardRanges = List.of(
                cardRange(ActionInd.ADD_CARD_RANGE_TO_CACHE, "1000", "1099"),
                cardRange(ActionInd.ADD_CARD_RANGE_TO_CACHE, "1112", "1120"));
        cardRangesStorageService.updateCardRanges(TEST_TAG, cardRanges);

        CardRange addRange = cardRange(ActionInd.ADD_CARD_RANGE_TO_CACHE, "1099", "1114");

        // When
        boolean isValid = cardRangesStorageService.isValidCardRanges(TEST_TAG, of(addRange));

        // Then
        assertFalse(isValid);
    }

    @Test
    public void shouldReturnValidForModifyCardRange() {
        // Given
        List<CardRange> cachedCardRanges = List.of(
                cardRange(ActionInd.ADD_CARD_RANGE_TO_CACHE, "1000", "1099"),
                cardRange(ActionInd.ADD_CARD_RANGE_TO_CACHE, "1112", "1120"));
        cardRangesStorageService.updateCardRanges(TEST_TAG, cachedCardRanges);

        CardRange modifyRange = cardRange(ActionInd.MODIFY_CARD_RANGE_DATA, "1000", "1099");

        // When
        boolean isValid = cardRangesStorageService.isValidCardRanges(TEST_TAG, of(modifyRange));

        // Then
        assertTrue(isValid);
    }

    @Test
    public void shouldReturnInvalidForMissingModifyCardRange() {
        // Given
        List<CardRange> cachedCardRanges = List.of(
                cardRange(ActionInd.ADD_CARD_RANGE_TO_CACHE, "1000", "1099"),
                cardRange(ActionInd.ADD_CARD_RANGE_TO_CACHE, "1112", "1120"));
        cardRangesStorageService.updateCardRanges(TEST_TAG, cachedCardRanges);

        CardRange addRange = cardRange(ActionInd.MODIFY_CARD_RANGE_DATA, "1000", "1112");

        // When
        boolean isValid = cardRangesStorageService.isValidCardRanges(TEST_TAG, of(addRange));

        // Then
        assertFalse(isValid);
    }

    @Test
    public void shouldReturnValidForDeleteCardRange() {
        // Given
        List<CardRange> cachedCardRanges = List.of(
                cardRange(ActionInd.ADD_CARD_RANGE_TO_CACHE, "1000", "1099"),
                cardRange(ActionInd.ADD_CARD_RANGE_TO_CACHE, "1112", "1120"));
        cardRangesStorageService.updateCardRanges(TEST_TAG, cachedCardRanges);

        CardRange addRange = cardRange(ActionInd.DELETE_CARD_RANGE_FROM_CACHE, "1000", "1099");

        // When
        boolean isValid = cardRangesStorageService.isValidCardRanges(TEST_TAG, of(addRange));

        // Then
        assertTrue(isValid);
    }

    @Test
    public void shouldReturnInvalidForMissingDeleteCardRange() {
        // Given
        List<CardRange> cachedCardRanges = List.of(
                cardRange(ActionInd.ADD_CARD_RANGE_TO_CACHE, "1000", "1099"),
                cardRange(ActionInd.ADD_CARD_RANGE_TO_CACHE, "1112", "1120"));
        cardRangesStorageService.updateCardRanges(TEST_TAG, cachedCardRanges);

        CardRange addRange = cardRange(ActionInd.DELETE_CARD_RANGE_FROM_CACHE, "1000", "1112");

        // When
        boolean isValid = cardRangesStorageService.isValidCardRanges(TEST_TAG, of(addRange));

        // Then
        assertFalse(isValid);
    }
}
