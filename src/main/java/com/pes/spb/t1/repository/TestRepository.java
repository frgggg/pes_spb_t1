package com.pes.spb.t1.repository;

import com.pes.spb.t1.model.TestModel;

import java.util.Optional;

public interface TestRepository {
    Optional<TestModel> findById(Integer id);
    TestModel save(TestModel testModel);
    TestModel updateById(TestModel testModel);
    Optional<TestModel> findByNameAndNotId(String name, Integer id);

}
