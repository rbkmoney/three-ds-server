package com.rbkmoney.threeds.server.service;

import com.rbkmoney.damsel.threeds.server.storage.AccountNumberVersion;
import com.rbkmoney.damsel.threeds.server.storage.CardRangesStorageSrv;
import com.rbkmoney.damsel.threeds.server.storage.DirectoryServerProviderIDNotFound;
import com.rbkmoney.damsel.threeds.server.storage.ThreeDsSecondVersion;
import com.rbkmoney.damsel.threeds.server.storage.UnsupportedVersion;
import com.rbkmoney.threeds.server.converter.thrift.CardRangeMapper;
import com.rbkmoney.threeds.server.domain.cardrange.ActionInd;
import com.rbkmoney.threeds.server.domain.cardrange.CardRange;
import com.rbkmoney.threeds.server.domain.root.emvco.PReq;
import com.rbkmoney.threeds.server.domain.root.emvco.PRes;
import com.rbkmoney.threeds.server.exception.ExternalStorageException;
import com.rbkmoney.threeds.server.service.rbkmoneyplatform.RBKMoneyCardRangesStorageService;
import com.rbkmoney.threeds.server.utils.IdGenerator;
import org.apache.thrift.TException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.rbkmoney.threeds.server.helper.CardRangeHelper.cardRange;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SuppressWarnings({"checkstyle:localvariablename", "checkstyle:methodname"})
public class RBKMoneyCardRangesStorageServiceTest {

    private static final String TEST_TAG = "visa";
    private static final Long ACCT_NUMBER = 0L;

    private CardRangesStorageSrv.Iface cardRangesStorageClient;
    private RBKMoneyCardRangesStorageService rbkMoneyCardRangesStorageService;

    @BeforeEach
    public void setUp() {
        cardRangesStorageClient = mock(CardRangesStorageSrv.Iface.class);
        rbkMoneyCardRangesStorageService = new RBKMoneyCardRangesStorageService(
                cardRangesStorageClient,
                new CardRangeMapper(new IdGenerator()));
    }

    @Test
    public void shouldReturnValidIfAcctNumberIsInCardRangesInStorage() throws TException {
        when(cardRangesStorageClient.getDirectoryServerProviderId(ACCT_NUMBER)).thenReturn(TEST_TAG);

        var dsProvider = rbkMoneyCardRangesStorageService.getDsProvider(String.valueOf(ACCT_NUMBER));

        assertEquals(TEST_TAG, dsProvider.get().getId());
    }

    @Test
    public void shouldReturnInvalidIfAcctNumberIsNotInCardRangesInStorage() throws TException {
        when(cardRangesStorageClient.getDirectoryServerProviderId(ACCT_NUMBER))
                .thenThrow(DirectoryServerProviderIDNotFound.class);

        var dsProvider = rbkMoneyCardRangesStorageService.getDsProvider(String.valueOf(ACCT_NUMBER));

        assertTrue(dsProvider.isEmpty());
    }

    @Test
    public void shouldReturnValidIfStorageIsEmpty() throws TException {
        when(cardRangesStorageClient.isStorageEmpty(TEST_TAG)).thenReturn(true);

        boolean isValidCardRanges = rbkMoneyCardRangesStorageService.isValidCardRanges(TEST_TAG, pRes());

        assertTrue(isValidCardRanges);
    }

    @Test
    public void shouldReturnValidIfStorageIsNotEmptyAndCardRangesIsValid() throws TException {
        PRes pRes = pRes(
                cardRange(ActionInd.ADD_CARD_RANGE_TO_CACHE, "0000", "1111"),
                cardRange(ActionInd.MODIFY_CARD_RANGE_DATA, "0000", "1111"),
                cardRange(ActionInd.DELETE_CARD_RANGE_FROM_CACHE, "0000", "1111"));

        when(cardRangesStorageClient.isStorageEmpty(TEST_TAG)).thenReturn(false);
        when(cardRangesStorageClient.isValidCardRanges(eq(TEST_TAG), anyList())).thenReturn(true);

        boolean isValidCardRanges = rbkMoneyCardRangesStorageService.isValidCardRanges(TEST_TAG, pRes);

        assertTrue(isValidCardRanges);
    }

    @Test
    public void shouldReturnInvalidIfStorageIsNotEmptyAndCardRangesIsInvalid() throws TException {
        PRes pRes = pRes(
                cardRange(ActionInd.ADD_CARD_RANGE_TO_CACHE, "0000", "1111"),
                cardRange(ActionInd.MODIFY_CARD_RANGE_DATA, "0000", "1111"),
                cardRange(ActionInd.DELETE_CARD_RANGE_FROM_CACHE, "0000", "1111"));

        when(cardRangesStorageClient.isStorageEmpty(TEST_TAG)).thenReturn(false);
        when(cardRangesStorageClient.isValidCardRanges(eq(TEST_TAG), anyList())).thenReturn(false);

        boolean isValidCardRanges = rbkMoneyCardRangesStorageService.isValidCardRanges(TEST_TAG, pRes);

        assertFalse(isValidCardRanges);
    }

    @Test
    public void shouldReturnEmptyIfAcctNumberIsUnsupportedThreeDsVersion() throws TException {
        when(cardRangesStorageClient.getAccountNumberVersion(ACCT_NUMBER))
                .thenReturn(AccountNumberVersion.unsupported_version(new UnsupportedVersion()));

        var threeDsVersion = rbkMoneyCardRangesStorageService.getThreeDsVersionResponse(ACCT_NUMBER);

        assertTrue(threeDsVersion.isEmpty());
    }

    @Test
    public void shouldReturnThreeDsVersionIfAcctNumberIsSupportedThreeDsVersion() throws TException {
        String providerId = "1";
        when(cardRangesStorageClient.getAccountNumberVersion(ACCT_NUMBER)).thenReturn(
                AccountNumberVersion.three_ds_second_version(
                        new ThreeDsSecondVersion()
                                .setProviderId(providerId)
                                .setAcsStart("2.1.0")
                                .setAcsEnd("2.1.0")
                                .setDsStart("2.1.0")
                                .setDsEnd("2.1.0")
                                .setThreeDsMethodUrl("1")));

        var threeDsVersion = rbkMoneyCardRangesStorageService.getThreeDsVersionResponse(ACCT_NUMBER);

        assertTrue(threeDsVersion.isPresent());
        assertEquals(providerId, threeDsVersion.get().getDsProviderId());
    }

    @Test
    public void shouldThrowExternalStorageException() throws TException {
        when(cardRangesStorageClient.getAccountNumberVersion(ACCT_NUMBER)).thenThrow(TException.class);

        assertThrows(ExternalStorageException.class,
                () -> rbkMoneyCardRangesStorageService.getThreeDsVersionResponse(ACCT_NUMBER));
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
