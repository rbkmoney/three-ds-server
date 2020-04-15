package com.rbkmoney.threeds.server.constraint;

import com.rbkmoney.threeds.server.constraint.validation.*;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(
        validatedBy = {
                AResCustomValidator.class,
                CardRangeCustomValidator.class,
                ErroWrapperCustomValidator.class,
                PArqCustomValidator.class,
                PGcqCustomValidator.class,
                PPrqCustomValidator.class,
                PResCustomValidator.class,
                RReqCustomValidator.class
        }
)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface CustomValidation {

    String message() default "field must be required at condition";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
