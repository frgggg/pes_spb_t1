package com.pes.spb.t1.model;

import com.pes.spb.t1.validation.TestModelValidation;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

@Data
@AllArgsConstructor
@TestModelValidation
public class TestModel {
    public static final int SURNAME_MIN_LEN = 1;
    public static final int SURNAME_MAX_LEN = 250;

    public static final int NAME_MIN_LEN = 1;
    public static final int NAME_MAX_LEN = 250;

    private Integer id;
    private String name;
    private String surname;

    protected TestModel() {}
}
