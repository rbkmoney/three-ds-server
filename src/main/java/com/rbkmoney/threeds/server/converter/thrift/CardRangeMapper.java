package com.rbkmoney.threeds.server.converter.thrift;

import com.rbkmoney.damsel.threeds.server.storage.Action;
import com.rbkmoney.damsel.threeds.server.storage.Add;
import com.rbkmoney.damsel.threeds.server.storage.Delete;
import com.rbkmoney.damsel.threeds.server.storage.Modify;
import com.rbkmoney.damsel.threeds.server.storage.ThreeDsSecondVersion;
import com.rbkmoney.threeds.server.domain.acs.AcsInfoInd;
import com.rbkmoney.threeds.server.domain.cardrange.ActionInd;
import com.rbkmoney.threeds.server.domain.cardrange.CardRange;
import com.rbkmoney.threeds.server.domain.versioning.ThreeDsVersionResponse;
import com.rbkmoney.threeds.server.serialization.EnumWrapper;
import com.rbkmoney.threeds.server.serialization.ListWrapper;
import com.rbkmoney.threeds.server.utils.IdGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.rbkmoney.threeds.server.domain.cardrange.ActionInd.ADD_CARD_RANGE_TO_CACHE;
import static com.rbkmoney.threeds.server.domain.cardrange.ActionInd.DELETE_CARD_RANGE_FROM_CACHE;
import static com.rbkmoney.threeds.server.domain.cardrange.ActionInd.MODIFY_CARD_RANGE_DATA;
import static com.rbkmoney.threeds.server.utils.Wrappers.getValue;
import static java.lang.Long.parseLong;

@Component
@RequiredArgsConstructor
public class CardRangeMapper {

    private static final Map<ActionInd, Action> ACTION_MAP = Map.of(
            ADD_CARD_RANGE_TO_CACHE, Action.add_card_range(new Add()),
            DELETE_CARD_RANGE_FROM_CACHE, Action.delete_card_range(new Delete()),
            MODIFY_CARD_RANGE_DATA, Action.modify_card_range(new Modify())
    );

    private final IdGenerator idGenerator;

    public List<com.rbkmoney.damsel.threeds.server.storage.CardRange> fromDomainToThrift(
            List<CardRange> cardRangeData) {
        var thriftCardRanges = new ArrayList<com.rbkmoney.damsel.threeds.server.storage.CardRange>();

        Iterator<CardRange> iterator = cardRangeData.iterator();

        while (iterator.hasNext()) {
            CardRange cardRange = iterator.next();

            thriftCardRanges.add(fromDomainToThrift(cardRange));

            iterator.remove();
        }

        return thriftCardRanges;
    }

    public com.rbkmoney.damsel.threeds.server.storage.CardRange fromDomainToThrift(CardRange domain) {
        return new com.rbkmoney.damsel.threeds.server.storage.CardRange()
                .setRangeStart(parseLong(domain.getStartRange()))
                .setRangeEnd(parseLong(domain.getEndRange()))
                .setAction(ACTION_MAP.get(getValue(domain.getActionInd())))
                .setAcsStart(domain.getAcsStartProtocolVersion())
                .setAcsEnd(domain.getAcsEndProtocolVersion())
                .setDsStart(domain.getDsStartProtocolVersion())
                .setDsEnd(domain.getDsEndProtocolVersion())
                .setAcsInformationIndicator(acsInfoInd(domain))
                .setThreeDsMethodUrl(domain.getThreeDSMethodURL());
    }

    public ThreeDsVersionResponse fromThriftToDomain(ThreeDsSecondVersion version) {
        return ThreeDsVersionResponse.builder()
                .threeDsServerTransId(idGenerator.generateUUID())
                .dsProviderId(version.getProviderId())
                .acsStartProtocolVersion(version.getAcsStart())
                .acsEndProtocolVersion(version.getAcsEnd())
                .dsStartProtocolVersion(version.getDsStart())
                .dsEndProtocolVersion(version.getDsEnd())
                .threeDsMethodUrl(version.getThreeDsMethodUrl())
                .build();
    }

    private String acsInfoInd(CardRange domain) {
        return Optional.ofNullable(domain.getAcsInfoInd())
                .map(ListWrapper::getValue)
                .map(
                        enumWrappers -> enumWrappers.stream()
                                .map(
                                        enumWrapper -> Optional.ofNullable(enumWrapper)
                                                .map(EnumWrapper::getValue))
                                .filter(Optional::isPresent)
                                .map(Optional::get)
                                .map(AcsInfoInd::getValue)
                                .collect(Collectors.joining(",")))
                .orElse(null);
    }
}
