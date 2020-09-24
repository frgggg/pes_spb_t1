package com.pes.spb.t1.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import static com.pes.spb.t1.model.TestModel.*;
import static com.pes.spb.t1.validation.impl.TestModelValidationImpl.*;

@Data
@AllArgsConstructor
public class TestModelDto {

    private Integer id;

    @NotNull(message = TEST_MODEL_NAME_NULL)
    @Size(min = NAME_MIN_LEN, max = NAME_MAX_LEN, message = TEST_MODEL_NAME_WRONG_LEN)
    private String name;

    @NotNull(message = TEST_MODEL_SURNAME_NULL)
    @Size(min = SURNAME_MIN_LEN, max = SURNAME_MAX_LEN, message = TEST_MODEL_SURNAME_WRONG_LEN)
    private String surname;

    protected TestModelDto() {}
}
