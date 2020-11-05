package com.rbkmoney.threeds.server.service;

import com.rbkmoney.damsel.three_ds_server_storage.CardRangesStorageSrv;
import com.rbkmoney.threeds.server.converter.thrift.CardRangeConverter;
import com.rbkmoney.threeds.server.domain.cardrange.ActionInd;
import com.rbkmoney.threeds.server.domain.cardrange.CardRange;
import com.rbkmoney.threeds.server.service.rbkmoneyplatform.RBKMoneyCardRangesStorageService;
import org.apache.thrift.TException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static com.rbkmoney.threeds.server.helper.CardRangeHelper.cardRange;
import static java.util.List.of;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RBKMoneyCardRangesStorageServiceTest {

    private static final String TEST_TAG = UUID.randomUUID().toString();
    private static final Long ACCT_NUMBER = 0L;

    private CardRangesStorageSrv.Iface cardRangesStorageClient;
    private CardRangesStorageService cardRangesStorageService;

    @BeforeEach
    public void setUp() {
        cardRangesStorageClient = mock(CardRangesStorageSrv.Iface.class);
        cardRangesStorageService = new RBKMoneyCardRangesStorageService(
                cardRangesStorageClient,
                new CardRangeConverter());
    }

    @Test
    public void shouldReturnValidIfAcctNumberIsInCardRangesInStorage() throws TException {
        when(cardRangesStorageClient.isInCardRange(TEST_TAG, ACCT_NUMBER)).thenReturn(true);

        // When
        boolean isInCardRange = cardRangesStorageService.isInCardRange(TEST_TAG, String.valueOf(ACCT_NUMBER));

        // Then
        assertTrue(isInCardRange);
    }

    @Test
    public void shouldReturnInvalidIfAcctNumberIsNotInCardRangesInStorage() throws TException {
        when(cardRangesStorageClient.isInCardRange(TEST_TAG, ACCT_NUMBER)).thenReturn(false);

        // When
        boolean isInCardRange = cardRangesStorageService.isInCardRange(TEST_TAG, String.valueOf(ACCT_NUMBER));

        // Then
        assertFalse(isInCardRange);
    }

    @Test
    public void shouldReturnValidIfStorageIsEmpty() throws TException {
        when(cardRangesStorageClient.isStorageEmpty(TEST_TAG)).thenReturn(true);

        boolean isValidCardRanges = cardRangesStorageService.isValidCardRanges(TEST_TAG, List.of());

        assertTrue(isValidCardRanges);
    }

    @Test
    public void shouldReturnValidIfStorageIsNotEmptyAndCardRangesIsValid() throws TException {
        // Given
        List<CardRange> cardRanges = of(cardRange(ActionInd.ADD_CARD_RANGE_TO_CACHE, "0000", "1111"),
                cardRange(ActionInd.MODIFY_CARD_RANGE_DATA, "0000", "1111"),
                cardRange(ActionInd.DELETE_CARD_RANGE_FROM_CACHE, "0000", "1111"));

        when(cardRangesStorageClient.isStorageEmpty(TEST_TAG)).thenReturn(false);
        when(cardRangesStorageClient.isValidCardRanges(eq(TEST_TAG), anyList())).thenReturn(true);

        boolean isValidCardRanges = cardRangesStorageService.isValidCardRanges(TEST_TAG, cardRanges);

        assertTrue(isValidCardRanges);
    }

    @Test
    public void shouldReturnInvalidIfStorageIsNotEmptyAndCardRangesIsInvalid() throws TException {
        // Given
        List<CardRange> cardRanges = of(cardRange(ActionInd.ADD_CARD_RANGE_TO_CACHE, "0000", "1111"),
                cardRange(ActionInd.MODIFY_CARD_RANGE_DATA, "0000", "1111"),
                cardRange(ActionInd.DELETE_CARD_RANGE_FROM_CACHE, "0000", "1111"));

        when(cardRangesStorageClient.isStorageEmpty(TEST_TAG)).thenReturn(false);
        when(cardRangesStorageClient.isValidCardRanges(eq(TEST_TAG), anyList())).thenReturn(false);

        boolean isValidCardRanges = cardRangesStorageService.isValidCardRanges(TEST_TAG, cardRanges);

        assertFalse(isValidCardRanges);
    }
}
