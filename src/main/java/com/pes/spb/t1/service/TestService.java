package com.pes.spb.t1.service;

import com.pes.spb.t1.model.TestModel;

public interface TestService {
    TestModel findById(Integer id);
    TestModel save(TestModel testModel);
    TestModel updateById(TestModel testModel);
}
