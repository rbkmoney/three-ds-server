package com.rbkmoney.threeds.server.service;

import com.rbkmoney.threeds.server.dto.ConstraintType;

import javax.validation.ConstraintValidatorContext;

public interface ValidatorContextEnricherService {

    void enrich(ConstraintValidatorContext ctx, ConstraintType constraintType, String field);

}
