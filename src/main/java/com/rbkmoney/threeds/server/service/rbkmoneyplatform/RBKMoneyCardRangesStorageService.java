package com.rbkmoney.threeds.server.service.rbkmoneyplatform;

import com.rbkmoney.damsel.three_ds_server_storage.CardRangesStorageSrv;
import com.rbkmoney.damsel.three_ds_server_storage.DirectoryServerProviderIDNotFound;
import com.rbkmoney.damsel.three_ds_server_storage.UpdateCardRangesRequest;
import com.rbkmoney.threeds.server.converter.thrift.CardRangeConverter;
import com.rbkmoney.threeds.server.domain.cardrange.ActionInd;
import com.rbkmoney.threeds.server.domain.cardrange.CardRange;
import com.rbkmoney.threeds.server.domain.root.emvco.PReq;
import com.rbkmoney.threeds.server.domain.root.emvco.PRes;
import com.rbkmoney.threeds.server.ds.DsProvider;
import com.rbkmoney.threeds.server.exception.ExternalStorageException;
import com.rbkmoney.threeds.server.serialization.EnumWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.TException;
import org.springframework.scheduling.annotation.Async;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.rbkmoney.threeds.server.domain.cardrange.ActionInd.ADD_CARD_RANGE_TO_CACHE;
import static com.rbkmoney.threeds.server.utils.AccountNumberUtils.hideAccountNumber;
import static com.rbkmoney.threeds.server.utils.Collections.safeList;

@RequiredArgsConstructor
@Slf4j
public class RBKMoneyCardRangesStorageService {

    private final CardRangesStorageSrv.Iface cardRangesStorageClient;
    private final CardRangeConverter cardRangeConverter;

    @Async
    public void updateCardRanges(String dsProviderId, PRes pRes) {
        try {
            List<CardRange> cardRanges = safeList(pRes.getCardRangeData());

            cardRanges.forEach(this::fillEmptyActionInd);

            var tCardRanges = cardRangeConverter.toThrift(cardRanges);

            boolean isNeedStorageClear = Optional.ofNullable(((PReq) pRes.getRequestMessage()))
                    .map(PReq::getSerialNum)
                    .isEmpty();

            log.info(
                    "[async] Update CardRanges, dsProviderId={}, isNeedStorageClear={}, serialNumber={}, cardRanges={}",
                    dsProviderId,
                    isNeedStorageClear,
                    pRes.getSerialNum(),
                    tCardRanges.size());

            UpdateCardRangesRequest request = new UpdateCardRangesRequest()
                    .setProviderId(dsProviderId)
                    .setMessageVersion(pRes.getMessageVersion())
                    .setCardRanges(tCardRanges)
                    .setSerialNumber(pRes.getSerialNum())
                    .setIsNeedStorageClear(isNeedStorageClear);

            cardRangesStorageClient.updateCardRanges(request);

            log.info(
                    "[async] Finish update CardRanges, providerId={}, isNeedStorageClear={}, serialNumber={}, cardRanges={}",
                    dsProviderId,
                    isNeedStorageClear,
                    pRes.getSerialNum(),
                    tCardRanges.size());
        } catch (TException e) {
            throw new ExternalStorageException(e);
        }
    }

    public boolean isValidCardRanges(String dsProviderId, List<CardRange> cardRanges) {
        if (storageIsEmpty(dsProviderId)) {
            return true;
        }

        var tCardRanges = cardRanges.stream()
                .peek(this::fillEmptyActionInd)
                .map(cardRangeConverter::toThrift)
                .collect(Collectors.toList());

        try {
            boolean isValidCardRanges = cardRangesStorageClient.isValidCardRanges(dsProviderId, tCardRanges);

            log.info("isValidCardRanges={}, dsProviderId={}, cardRanges={}", isValidCardRanges, dsProviderId, cardRanges.size());

            return isValidCardRanges;
        } catch (TException e) {
            throw new ExternalStorageException(e);
        }
    }

    public Optional<DsProvider> getDsProvider(String accountNumber) {
        return Optional.ofNullable(getDirectoryServerProviderId(accountNumber))
                .map(DsProvider::of);
    }

    private String getDirectoryServerProviderId(String accountNumber) {
        try {
            String dsProviderId = cardRangesStorageClient.getDirectoryServerProviderId(Long.parseLong(accountNumber));

            log.info("getDirectoryServerProviderId={}, accountNumber={}", dsProviderId, hideAccountNumber(accountNumber));

            return dsProviderId;
        } catch (DirectoryServerProviderIDNotFound ex) {
            return null;
        } catch (TException e) {
            throw new ExternalStorageException(e);
        }
    }

    private boolean storageIsEmpty(String dsProviderId) {
        try {
            boolean isStorageEmpty = cardRangesStorageClient.isStorageEmpty(dsProviderId);

            log.info("isStorageEmpty={}, dsProviderId={}", isStorageEmpty, dsProviderId);

            return isStorageEmpty;
        } catch (TException e) {
            throw new ExternalStorageException(e);
        }
    }

    private void fillEmptyActionInd(CardRange cardRange) {
        if (cardRange.getActionInd() == null) {
            EnumWrapper<ActionInd> addAction = new EnumWrapper<>();
            addAction.setValue(ADD_CARD_RANGE_TO_CACHE);

            cardRange.setActionInd(addAction);
        }
    }
}
