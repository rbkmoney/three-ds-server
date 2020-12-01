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
            if (!cardRanges.isEmpty()) {
                // fill default value in null field
                cardRanges.forEach(this::fillEmptyActionInd);

                var tCardRanges = cardRangeConverter.toThrift(cardRanges);

                boolean isNeedStorageClear = isNeedStorageClear(pRes);

                log.info(
                        "Update CardRanges in storage (during the current 'Initialization PreparationFlow'), " +
                                "dsProviderId={}, isNeedStorageClear={}, serialNumber={}, cardRanges={}",
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
                        "Finish update CardRanges in storage (during the current 'Initialization PreparationFlow'), " +
                                "dsProviderId={}, isNeedStorageClear={}, serialNumber={}, cardRanges={}",
                        dsProviderId,
                        isNeedStorageClear,
                        pRes.getSerialNum(),
                        tCardRanges.size());
            } else {
                log.info(
                        "CardRanges does NOT need to update in storage BECAUSE CardRanges is empty (during the current 'Initialization PreparationFlow'), " +
                                "dsProviderId={}, serialNumber={}, cardRanges={}",
                        dsProviderId,
                        pRes.getSerialNum(),
                        cardRanges.size());
            }
        } catch (TException e) {
            throw new ExternalStorageException(e);
        }
    }

    public boolean isValidCardRanges(String dsProviderId, PRes pRes) {
        try {
            List<CardRange> cardRanges = safeList(pRes.getCardRangeData());

            boolean isEmptyCardRanges = cardRanges.isEmpty();
            boolean isNeedStorageClear = isNeedStorageClear(pRes);
            boolean isEmptyStorage = storageIsEmpty(dsProviderId);

            if (!isEmptyCardRanges && !isNeedStorageClear && !isEmptyStorage) {
                var tCardRanges = cardRanges.stream()
                        .peek(this::fillEmptyActionInd)
                        .map(cardRangeConverter::toThrift)
                        .collect(Collectors.toList());

                boolean isValidCardRanges = cardRangesStorageClient.isValidCardRanges(dsProviderId, tCardRanges);

                log.info("CardRanges is valid = '{}', dsProviderId={}, cardRanges={}", isValidCardRanges, dsProviderId, cardRanges.size());

                return isValidCardRanges;
            } else {
                log.info(
                        "CardRanges for dsProviderId = '{}' does NOT need to validate in storage BECAUSE (one of them):\n" +
                                "- CardRanges is empty = '{}';\n" +
                                "- Storage needs to be cleaned the obsolete CardRanges (OR just add the new CardRanges in empty storage) = '{}';\n" +
                                "- Storage is empty = '{}'.",
                        dsProviderId,
                        isEmptyCardRanges,
                        isNeedStorageClear,
                        isEmptyStorage);

                return true;
            }
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

            log.info("ProviderId by AccountNumber has been found, dsProviderId={}, accountNumber={}", dsProviderId, hideAccountNumber(accountNumber));

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

            log.info("Storage is empty = '{}', dsProviderId={}", isStorageEmpty, dsProviderId);

            return isStorageEmpty;
        } catch (TException e) {
            throw new ExternalStorageException(e);
        }
    }

    private boolean isNeedStorageClear(PRes pRes) {
        return Optional.ofNullable((pRes.getRequestMessage()))
                .map(message -> (PReq) message)
                .map(PReq::getSerialNum)
                .isEmpty();
    }

    private void fillEmptyActionInd(CardRange cardRange) {
        if (cardRange.getActionInd() == null) {
            EnumWrapper<ActionInd> addAction = new EnumWrapper<>();
            addAction.setValue(ADD_CARD_RANGE_TO_CACHE);

            cardRange.setActionInd(addAction);
        }
    }
}
