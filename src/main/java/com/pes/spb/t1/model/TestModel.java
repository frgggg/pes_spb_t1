package com.pes.spb.t1.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TestModel {
    private Integer id;
    private Integer name;
    private Integer surname;

    protected TestModel() {}
}
