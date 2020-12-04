package com.rbkmoney.threeds.server.converter.thrift;

import com.rbkmoney.damsel.threeds.server.storage.*;
import com.rbkmoney.threeds.server.domain.acs.AcsInfoInd;
import com.rbkmoney.threeds.server.domain.cardrange.ActionInd;
import com.rbkmoney.threeds.server.domain.cardrange.CardRange;
import com.rbkmoney.threeds.server.domain.versioning.ThreeDsVersion;
import com.rbkmoney.threeds.server.serialization.EnumWrapper;
import com.rbkmoney.threeds.server.serialization.ListWrapper;
import com.rbkmoney.threeds.server.utils.IdGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

import static com.rbkmoney.threeds.server.domain.cardrange.ActionInd.*;
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

    public List<com.rbkmoney.damsel.threeds.server.storage.CardRange> fromDomainToThrift(List<CardRange> cardRangeData) {
        var tCardRanges = new ArrayList<com.rbkmoney.damsel.threeds.server.storage.CardRange>();

        Iterator<CardRange> iterator = cardRangeData.iterator();

        while (iterator.hasNext()) {
            CardRange cardRange = iterator.next();

            tCardRanges.add(fromDomainToThrift(cardRange));

            iterator.remove();
        }

        return tCardRanges;
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

    public ThreeDsVersion fromThriftToDomain(ThreeDsSecondVersion tThreeDsSecondVersion) {
        return ThreeDsVersion.builder()
                .threeDSServerTransID(idGenerator.generateUUID())
                .dsProviderId(tThreeDsSecondVersion.getProviderId())
                .acsStartProtocolVersion(tThreeDsSecondVersion.getAcsStart())
                .acsEndProtocolVersion(tThreeDsSecondVersion.getAcsEnd())
                .dsStartProtocolVersion(tThreeDsSecondVersion.getDsStart())
                .dsEndProtocolVersion(tThreeDsSecondVersion.getDsEnd())
                .threeDSMethodURL(tThreeDsSecondVersion.getThreeDsMethodUrl())
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
