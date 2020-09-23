package com.pes.spb.t1.repository.impl;

import com.pes.spb.t1.model.TestModel;
import com.pes.spb.t1.repository.TestRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class TestRepositoryImpl implements TestRepository {
    @Override
    public Optional<TestModel> findById(Integer id) {
        return Optional.empty();
    }

    @Override
    public Optional<TestModel> findByName(String name) {
        return Optional.empty();
    }

    @Override
    public TestModel save(TestModel testModel) {
        return null;
    }

    @Override
    public TestModel updateById(TestModel testModel) {
        return null;
    }
}
