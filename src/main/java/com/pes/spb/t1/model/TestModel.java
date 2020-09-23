package com.pes.spb.t1.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
public class TestModel {

    public static final String SURNAME_NULL = "null surname";
    public static final int SURNAME_MIN_LEN = 1;
    public static final int SURNAME_MAX_LEN = 250;
    public static final String SURNAME_WRONG_LEN = "wrong surname len";

    public static final String NAME_NULL = "null name";
    public static final int NAME_MIN_LEN = 1;
    public static final int NAME_MAX_LEN = 250;
    public static final String NAME_WRONG_LEN = "wrong name len";


    private Integer id;

    @NotNull(message = NAME_NULL)
    @Size(min = NAME_MIN_LEN, max = NAME_MAX_LEN, message = NAME_WRONG_LEN)
    private String name;

    @NotNull(message = SURNAME_NULL)
    @Size(min = SURNAME_MIN_LEN, max = SURNAME_MAX_LEN, message = SURNAME_WRONG_LEN)
    private String surname;

    protected TestModel() {}
}
