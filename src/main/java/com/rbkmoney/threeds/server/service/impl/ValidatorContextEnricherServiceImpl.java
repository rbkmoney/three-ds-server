package com.rbkmoney.threeds.server.service.impl;

import com.rbkmoney.threeds.server.dto.ConstraintType;
import com.rbkmoney.threeds.server.service.ValidatorContextEnricherService;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintValidatorContext;

@Service
public class ValidatorContextEnricherServiceImpl implements ValidatorContextEnricherService {

    @Override
    public void enrich(ConstraintValidatorContext ctx, ConstraintType constraintType, String field) {
        ctx.disableDefaultConstraintViolation();
        ctx.buildConstraintViolationWithTemplate(constraintType.getValue())
                .addPropertyNode(field)
                .addConstraintViolation();
    }
}
