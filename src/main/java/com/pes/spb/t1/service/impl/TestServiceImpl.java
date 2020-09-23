package com.pes.spb.t1.service.impl;

import com.pes.spb.t1.exception.TestServiceException;
import com.pes.spb.t1.model.TestModel;
import com.pes.spb.t1.repository.TestRepository;
import com.pes.spb.t1.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Optional;

@Component
public class TestServiceImpl implements TestService {
    public static final String NO_TEST_MODEL_WITH_ID = "No TestModel with id = %d.";
    public static final String TEST_MODEL_WITH_EXIST_NAME = "Exist TestModel with same name = %s.";

    private TestRepository testRepository;
    private TestServiceImpl thisTestServiceImpl;

    @Autowired
    public TestServiceImpl(TestRepository testRepository) {
        this.testRepository = testRepository;
    }

    @Override
    public TestModel findById(Integer id) {
        Optional<TestModel> optionalTestModel = testRepository.findById(id);
        optionalTestModel.orElseThrow(
                () -> TestServiceException.getFromQuery(String.format(NO_TEST_MODEL_WITH_ID, id))
        );
        return optionalTestModel.get();
    }

    @Override
    public TestModel save(TestModel testModel) {
        return testRepository.save(testModel);
    }

    @Override
    public TestModel updateBuId(TestModel testModel) {
        Integer oldId = testModel.getId();
        TestModel existTestModel = thisTestServiceImpl.findById(oldId);

        String newName = testModel.getName();
        Optional<TestModel> optionalTestModelWithSameName = testRepository.findByName(newName);
        if(optionalTestModelWithSameName.isPresent()) {
            if(!optionalTestModelWithSameName.get().getId().equals(oldId)) {
                throw TestServiceException.getFromQuery(String.format(TEST_MODEL_WITH_EXIST_NAME, newName));
            }
        }
        return testRepository.updateById(testModel);
    }

    @Autowired
    @PostConstruct
    protected void addThis(TestServiceImpl thisTestServiceImpl) {
        this.thisTestServiceImpl = thisTestServiceImpl;
    }
}
