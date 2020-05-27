package com.rbkmoney.threeds.server.service;

import com.rbkmoney.threeds.server.domain.ActionInd;
import com.rbkmoney.threeds.server.domain.CardRange;
import com.rbkmoney.threeds.server.service.cache.CacheService;
import com.rbkmoney.threeds.server.service.cache.InMemoryCacheService;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static com.rbkmoney.threeds.server.helper.CardRangeHelper.cardRange;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AbstractCacheServiceTest {

    private static final String TEST_TAG = "TEST_TAG";

    private CacheService cacheService;

    @Before
    public void setUp() {
        cacheService = new InMemoryCacheService(100L);
    }

    @Test
    public void shouldReturnValidForCardRangeWithNullActionInd() {
        // Given
        CardRange cardRange = new CardRange();
        cardRange.setActionInd(null);

        // When
        boolean isValid = cacheService.isValidCardRange(TEST_TAG, cardRange);

        // Then
        assertTrue(isValid);
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
        assertTrue(isAddValid);
        assertTrue(isModifyValid);
        assertTrue(isDeleteValid);
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
        assertTrue(isValid);
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
        assertFalse(isValid);
    }

    @Test
    public void shouldReturnValidForModifyCardRange() {
        // Given
        List<CardRange> cachedCardRanges = List.of(
                cardRange(ActionInd.ADD_CARD_RANGE_TO_CACHE, "1000", "1099"),
                cardRange(ActionInd.ADD_CARD_RANGE_TO_CACHE, "1112", "1120"));
        cacheService.updateCardRanges(TEST_TAG, cachedCardRanges);

        CardRange modifyRange = cardRange(ActionInd.MODIFY_CARD_RANGE_DATA, "1000", "1099");

        // When
        boolean isValid = cacheService.isValidCardRange(TEST_TAG, modifyRange);

        // Then
        assertTrue(isValid);
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
        assertFalse(isValid);
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
        assertTrue(isValid);
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
        assertFalse(isValid);
    }
}
