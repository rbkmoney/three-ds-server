package com.rbkmoney.threeds.server.config.properties;

import lombok.Data;

@Data
public class PreparationFlowScheduleProperties {

    private String jobIdPrefix;
    private Long revisionId;
    private Integer schedulerId;
    private Integer calendarId;
}
