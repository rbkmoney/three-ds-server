package com.rbkmoney.threeds.server.service;

import com.rbkmoney.threeds.server.domain.cardrange.ActionInd;
import com.rbkmoney.threeds.server.domain.cardrange.CardRange;
import com.rbkmoney.threeds.server.domain.root.emvco.PReq;
import com.rbkmoney.threeds.server.domain.root.emvco.PRes;
import com.rbkmoney.threeds.server.service.testplatform.TestPlatformCardRangesStorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static com.rbkmoney.threeds.server.helper.CardRangeHelper.cardRange;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SuppressWarnings({"checkstyle:localvariablename", "checkstyle:methodname"})
public class TestPlatformCardRangesStorageServiceTest {

    private static final String TEST_TAG = UUID.randomUUID().toString();

    private TestPlatformCardRangesStorageService testPlatformCardRangesStorageService;

    @BeforeEach
    public void setUp() {
        testPlatformCardRangesStorageService = new TestPlatformCardRangesStorageService();
    }

    @Test
    public void shouldReturnTrueIfCachedCardRangesAreEmpty() {
        String acctNumber = "0000";

        boolean isInCardRange = testPlatformCardRangesStorageService.isInCardRange(TEST_TAG, acctNumber);

        assertTrue(isInCardRange);
    }

    @Test
    public void shouldReturnTrueForAcctNumberInRange() {
        PRes pRes = pRes(
                cardRange(ActionInd.ADD_CARD_RANGE_TO_CACHE, "1000", "1099"),
                cardRange(ActionInd.ADD_CARD_RANGE_TO_CACHE, "1112", "1120"));

        testPlatformCardRangesStorageService.updateCardRanges(pRes);

        boolean isInCardRange = testPlatformCardRangesStorageService.isInCardRange(TEST_TAG, "1099");

        assertTrue(isInCardRange);
    }

    @Test
    public void shouldReturnFalseForAcctNumberNotInRange() {
        PRes pRes = pRes(
                cardRange(ActionInd.ADD_CARD_RANGE_TO_CACHE, "1000", "1099"),
                cardRange(ActionInd.ADD_CARD_RANGE_TO_CACHE, "1112", "1120"));

        testPlatformCardRangesStorageService.updateCardRanges(pRes);

        boolean isInCardRange = testPlatformCardRangesStorageService.isInCardRange(TEST_TAG, "1111");

        assertFalse(isInCardRange);
    }

    @Test
    public void shouldAddNewCardRanges() {
        PRes pRes = pRes(
                cardRange(ActionInd.ADD_CARD_RANGE_TO_CACHE, "1000", "1099"));

        testPlatformCardRangesStorageService.updateCardRanges(pRes);

        boolean isAdded = testPlatformCardRangesStorageService.isInCardRange(TEST_TAG, "1050");

        assertTrue(isAdded);
    }

    @Test
    public void shouldModifyExistingCardRanges() {
        PRes pRes = pRes(
                cardRange(ActionInd.ADD_CARD_RANGE_TO_CACHE, "1000", "1099"));

        testPlatformCardRangesStorageService.updateCardRanges(pRes);

        pRes = pRes(
                cardRange(ActionInd.MODIFY_CARD_RANGE_DATA, "1000", "1099"));

        testPlatformCardRangesStorageService.updateCardRanges(pRes);

        boolean isModified = testPlatformCardRangesStorageService.isInCardRange(TEST_TAG, "1050");

        assertTrue(isModified);
    }

    @Test
    public void shouldDeleteExistingCardRanges() {
        PRes pRes = pRes(
                cardRange(ActionInd.ADD_CARD_RANGE_TO_CACHE, "0000", "999"),
                cardRange(ActionInd.ADD_CARD_RANGE_TO_CACHE, "1000", "1099"));

        testPlatformCardRangesStorageService.updateCardRanges(pRes);

        boolean isAdded = testPlatformCardRangesStorageService.isInCardRange(TEST_TAG, "1050");

        assertTrue(isAdded);

        pRes = pRes(
                cardRange(ActionInd.DELETE_CARD_RANGE_FROM_CACHE, "1000", "1099"));

        testPlatformCardRangesStorageService.updateCardRanges(pRes);

        boolean isDeleted = !testPlatformCardRangesStorageService.isInCardRange(TEST_TAG, "1050");

        assertTrue(isDeleted);
    }

    @Test
    public void shouldReturnValidForCardRangeWithNullActionInd() {
        CardRange cardRange = new CardRange();
        cardRange.setActionInd(null);

        boolean isValid = testPlatformCardRangesStorageService.isValidCardRanges(pRes(cardRange));

        assertTrue(isValid);
    }

    @Test
    public void shouldReturnValidIfStorageCardRangesAreEmpty() {
        PRes pRes = pRes(
                cardRange(ActionInd.ADD_CARD_RANGE_TO_CACHE, "0000", "1111"),
                cardRange(ActionInd.MODIFY_CARD_RANGE_DATA, "0000", "1111"),
                cardRange(ActionInd.DELETE_CARD_RANGE_FROM_CACHE, "0000", "1111"));

        boolean isValid = testPlatformCardRangesStorageService.isValidCardRanges(pRes);

        assertTrue(isValid);
    }

    @Test
    public void shouldReturnValidForAddCardRange() {
        PRes pRes = pRes(
                cardRange(ActionInd.ADD_CARD_RANGE_TO_CACHE, "1000", "1099"),
                cardRange(ActionInd.ADD_CARD_RANGE_TO_CACHE, "1112", "1120"));

        testPlatformCardRangesStorageService.updateCardRanges(pRes);

        CardRange addRange = cardRange(ActionInd.ADD_CARD_RANGE_TO_CACHE, "1100", "1111");

        boolean isValid = testPlatformCardRangesStorageService.isValidCardRanges(pRes(addRange));

        assertTrue(isValid);
    }

    @Test
    public void shouldReturnInvalidForClashingAddCardRange() {
        PRes pRes = pRes(
                cardRange(ActionInd.ADD_CARD_RANGE_TO_CACHE, "1000", "1099"),
                cardRange(ActionInd.ADD_CARD_RANGE_TO_CACHE, "1112", "1120"));

        testPlatformCardRangesStorageService.updateCardRanges(pRes);

        CardRange addRange = cardRange(ActionInd.ADD_CARD_RANGE_TO_CACHE, "1099", "1114");

        boolean isValid = testPlatformCardRangesStorageService.isValidCardRanges(pRes(addRange));

        assertFalse(isValid);
    }

    @Test
    public void shouldReturnValidForModifyCardRange() {
        PRes pRes = pRes(
                cardRange(ActionInd.ADD_CARD_RANGE_TO_CACHE, "1000", "1099"),
                cardRange(ActionInd.ADD_CARD_RANGE_TO_CACHE, "1112", "1120"));

        testPlatformCardRangesStorageService.updateCardRanges(pRes);

        CardRange modifyRange = cardRange(ActionInd.MODIFY_CARD_RANGE_DATA, "1000", "1099");

        boolean isValid = testPlatformCardRangesStorageService.isValidCardRanges(pRes(modifyRange));

        assertTrue(isValid);
    }

    @Test
    public void shouldReturnInvalidForMissingModifyCardRange() {
        PRes pRes = pRes(
                cardRange(ActionInd.ADD_CARD_RANGE_TO_CACHE, "1000", "1099"),
                cardRange(ActionInd.ADD_CARD_RANGE_TO_CACHE, "1112", "1120"));

        testPlatformCardRangesStorageService.updateCardRanges(pRes);

        CardRange addRange = cardRange(ActionInd.MODIFY_CARD_RANGE_DATA, "1000", "1112");

        boolean isValid = testPlatformCardRangesStorageService.isValidCardRanges(pRes(addRange));

        assertFalse(isValid);
    }

    @Test
    public void shouldReturnValidForDeleteCardRange() {
        PRes pRes = pRes(
                cardRange(ActionInd.ADD_CARD_RANGE_TO_CACHE, "1000", "1099"),
                cardRange(ActionInd.ADD_CARD_RANGE_TO_CACHE, "1112", "1120"));

        testPlatformCardRangesStorageService.updateCardRanges(pRes);

        CardRange addRange = cardRange(ActionInd.DELETE_CARD_RANGE_FROM_CACHE, "1000", "1099");

        boolean isValid = testPlatformCardRangesStorageService.isValidCardRanges(pRes(addRange));

        assertTrue(isValid);
    }

    @Test
    public void shouldReturnInvalidForMissingDeleteCardRange() {
        PRes pRes = pRes(
                cardRange(ActionInd.ADD_CARD_RANGE_TO_CACHE, "1000", "1099"),
                cardRange(ActionInd.ADD_CARD_RANGE_TO_CACHE, "1112", "1120"));

        testPlatformCardRangesStorageService.updateCardRanges(pRes);

        CardRange addRange = cardRange(ActionInd.DELETE_CARD_RANGE_FROM_CACHE, "1000", "1112");

        boolean isValid = testPlatformCardRangesStorageService.isValidCardRanges(pRes(addRange));

        assertFalse(isValid);
    }

    private PRes pRes(CardRange... elements) {
        PRes pRes = PRes.builder()
                .cardRangeData(List.of(elements))
                .build();
        pRes.setUlTestCaseId(TEST_TAG);
        pRes.setRequestMessage(PReq.builder().serialNum("1").build());
        return pRes;
    }
}
