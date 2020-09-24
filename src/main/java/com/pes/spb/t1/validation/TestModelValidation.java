package com.pes.spb.t1.validation;

import com.pes.spb.t1.validation.impl.TestModelValidationImpl;

import javax.validation.Constraint;
import java.lang.annotation.*;

@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy= TestModelValidationImpl.class)
public @interface TestModelValidation {
    String message() default "wrong arg";
    Class<?>[] groups() default {};
    Class<? extends javax.validation.Payload>[] payload() default {};
}
