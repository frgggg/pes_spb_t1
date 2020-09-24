package com.pes.spb.t1.validation.impl;

import com.pes.spb.t1.model.TestModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.Validator;

import static com.pes.spb.t1.model.TestModel.*;
import static com.pes.spb.t1.util.UtilParams.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TestModelValidationImplTest {

    @Autowired
    private Validator validator;

    @Test
    public void isValid() {
        TestModel successTestModel = new TestModel(EXIST_TEST_MODEL_ID, EXIST_TEST_MODEL_NAME, EXIST_TEST_MODEL_SURNAME);
        assertTrue(validator.validate(successTestModel).isEmpty());

        TestModel nullNameTestModel = new TestModel(EXIST_TEST_MODEL_ID, getNull(), EXIST_TEST_MODEL_SURNAME);
        assertFalse(validator.validate(nullNameTestModel).isEmpty());

        TestModel nullSurnameTestModel = new TestModel(EXIST_TEST_MODEL_ID, EXIST_TEST_MODEL_NAME, getNull());
        assertFalse(validator.validate(nullSurnameTestModel).isEmpty());


        TestModel tooBigNameTestModel = new TestModel(EXIST_TEST_MODEL_ID, getTooBig(NAME_MAX_LEN), EXIST_TEST_MODEL_SURNAME);
        assertFalse(validator.validate(tooBigNameTestModel).isEmpty());

        TestModel tooBigSurnameTestModel = new TestModel(EXIST_TEST_MODEL_ID, EXIST_TEST_MODEL_NAME, getTooBig(SURNAME_MAX_LEN));
        assertFalse(validator.validate(tooBigSurnameTestModel).isEmpty());

        TestModel tooLittleNameTestModel = new TestModel(EXIST_TEST_MODEL_ID, getTooLittle(NAME_MIN_LEN), EXIST_TEST_MODEL_SURNAME);
        assertFalse(validator.validate(tooLittleNameTestModel).isEmpty());

        TestModel tooLittleSurnameTestModel = new TestModel(EXIST_TEST_MODEL_ID, EXIST_TEST_MODEL_NAME, getTooLittle(SURNAME_MIN_LEN));
        assertFalse(validator.validate(tooLittleSurnameTestModel).isEmpty());

    }
}