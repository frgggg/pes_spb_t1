package com.pes.spb.t1.service.impl;

import com.pes.spb.t1.exception.TestServiceException;
import com.pes.spb.t1.model.TestModel;
import com.pes.spb.t1.repository.TestRepository;
import com.pes.spb.t1.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Optional;
import java.util.Set;


@Service
public class TestServiceImpl implements TestService {

    public static final String TEST_SERVICE_IMPL_NO_TEST_MODEL_WITH_ID = "No TestModel with id = %d.";
    public static final String TEST_SERVICE_IMPL_TEST_MODEL_WITH_EXIST_NAME = "Exist TestModel with same name = %s.";

    private TestRepository testRepository;
    private Validator validator;

    @Autowired
    public TestServiceImpl(TestRepository testRepository, Validator validator) {
        this.testRepository = testRepository;
        this.validator = validator;
    }

    @Override
    public TestModel findById(Integer id) {
        Optional<TestModel> optionalTestModel = testRepository.findById(id);
        optionalTestModel.orElseThrow(
                () -> TestServiceException.getFromQuery(String.format(TEST_SERVICE_IMPL_NO_TEST_MODEL_WITH_ID, id))
        );
        return optionalTestModel.get();
    }

    @Override
    public TestModel save(TestModel testModel) {
        serviceValidTestModel(testModel);
        return testRepository.save(testModel);
    }

    @Override
    public TestModel updateById(TestModel testModel) {
        serviceValidTestModel(testModel);
        findById(testModel.getId());

        Optional<TestModel> optionalTestModel = testRepository.findByNameAndNotId(testModel.getName(), testModel.getId());
        if(optionalTestModel.isPresent()) {
            throw TestServiceException.getFromQuery(String.format(TEST_SERVICE_IMPL_TEST_MODEL_WITH_EXIST_NAME, testModel.getName()));
        }
        return testRepository.updateById(testModel);
    }

    private void serviceValidTestModel(TestModel testModel) {
        Set<ConstraintViolation<TestModel>> constraintViolations = validator.validate(testModel);
        if(!constraintViolations.isEmpty()) {
            throw TestServiceException.getFromQuery(constraintViolations.iterator().next().getMessage());
        }
    }
}
