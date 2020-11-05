package com.rbkmoney.threeds.server.service.rbkmoneyplatform;

import com.rbkmoney.damsel.three_ds_server_storage.CardRangesStorageSrv;
import com.rbkmoney.threeds.server.converter.thrift.CardRangeConverter;
import com.rbkmoney.threeds.server.domain.cardrange.CardRange;
import com.rbkmoney.threeds.server.exeption.ExternalStorageException;
import com.rbkmoney.threeds.server.service.CardRangesStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.TException;

import java.util.List;
import java.util.stream.Collectors;

import static com.rbkmoney.threeds.server.utils.Wrappers.getValue;

@RequiredArgsConstructor
@Slf4j
public class RBKMoneyCardRangesStorageService implements CardRangesStorageService {

    private final CardRangesStorageSrv.Iface cardRangesStorageClient;
    private final CardRangeConverter cardRangeConverter;

    @Override
    public boolean isValidCardRanges(String dsProviderId, List<CardRange> cardRanges) {
        if (storageIsEmpty(dsProviderId)) {
            return true;
        }

        var tCardRanges = cardRanges.stream()
                .filter(cardRange -> getValue(cardRange.getActionInd()) != null)
                .map(cardRangeConverter::toThrift)
                .collect(Collectors.toList());

        try {
            return cardRangesStorageClient.isValidCardRanges(dsProviderId, tCardRanges);
        } catch (TException e) {
            throw new ExternalStorageException(e);
        }
    }

    @Override
    public boolean isInCardRange(String dsProviderId, String acctNumber) {
        try {
            return cardRangesStorageClient.isInCardRange(dsProviderId, Long.parseLong(acctNumber));
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
}
