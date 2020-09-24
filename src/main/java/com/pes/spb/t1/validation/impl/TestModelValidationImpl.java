package com.pes.spb.t1.validation.impl;

import com.pes.spb.t1.model.TestModel;
import com.pes.spb.t1.validation.TestModelValidation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static com.pes.spb.t1.model.TestModel.*;

public class TestModelValidationImpl implements ConstraintValidator<TestModelValidation, TestModel> {

    public static final String TEST_MODEL_NULL = "null TestModel";

    public static final String TEST_MODEL_SURNAME_NULL = "null surname";
    public static final String TEST_MODEL_SURNAME_WRONG_LEN = "wrong surname len";

    public static final String TEST_MODEL_NAME_NULL = "null name";
    public static final String TEST_MODEL_NAME_WRONG_LEN = "wrong name len";

    @Override
    public boolean isValid(TestModel value, ConstraintValidatorContext context) {
        if(value == null) {
            setContext(context, TEST_MODEL_NULL);
            return false;
        }

        if(value.getName() == null) {
            setContext(context, TEST_MODEL_NAME_NULL);
            return false;
        }
        if(NAME_MIN_LEN > value.getName().length() || value.getName().length() > NAME_MAX_LEN) {
            setContext(context, TEST_MODEL_NAME_WRONG_LEN);
            return false;
        }

        if(value.getSurname() == null) {
            setContext(context, TEST_MODEL_SURNAME_NULL);
            return false;
        }
        if(SURNAME_MIN_LEN > value.getSurname().length() || value.getSurname().length() > SURNAME_MAX_LEN) {
            setContext(context, TEST_MODEL_SURNAME_WRONG_LEN);
            return false;
        }
        return true;
    }

    private void setContext(ConstraintValidatorContext context, String msg) {
        context.disableDefaultConstraintViolation();
        context
                .buildConstraintViolationWithTemplate(msg)
                .addConstraintViolation();
    }
}
