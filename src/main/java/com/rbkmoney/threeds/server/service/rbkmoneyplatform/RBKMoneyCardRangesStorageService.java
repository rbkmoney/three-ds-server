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
import org.apache.thrift.TException;
import org.springframework.scheduling.annotation.Async;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.rbkmoney.threeds.server.domain.cardrange.ActionInd.ADD_CARD_RANGE_TO_CACHE;
import static com.rbkmoney.threeds.server.utils.Collections.safeList;

@RequiredArgsConstructor
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

            UpdateCardRangesRequest request = new UpdateCardRangesRequest()
                    .setProviderId(dsProviderId)
                    .setMessageVersion(pRes.getMessageVersion())
                    .setCardRanges(tCardRanges)
                    .setSerialNumber(pRes.getSerialNum())
                    .setIsNeedStorageClear(isNeedStorageClear);

            cardRangesStorageClient.updateCardRanges(request);
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
            return cardRangesStorageClient.isValidCardRanges(dsProviderId, tCardRanges);
        } catch (TException e) {
            throw new ExternalStorageException(e);
        }
    }

    public Optional<DsProvider> getDsProvider(String accountNumber) {
        try {
            return Optional.of(
                    DsProvider.of(
                            cardRangesStorageClient.getDirectoryServerProviderId(Long.parseLong(accountNumber))));
        } catch (NullPointerException | IllegalArgumentException | DirectoryServerProviderIDNotFound ex) {
            return Optional.empty();
        } catch (TException e) {
            throw new ExternalStorageException(e);
        }
    }

    private boolean storageIsEmpty(String dsProviderId) {
        try {
            return cardRangesStorageClient.isStorageEmpty(dsProviderId);
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
