package com.pes.spb.t1.repository.impl;

import com.pes.spb.t1.exception.TestServiceException;
import com.pes.spb.t1.model.TestModel;
import com.pes.spb.t1.repository.TestRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.*;

@Component
@Slf4j
public class TestRepositoryImpl implements TestRepository {

    public static final String TEST_REPOSITORY_NO_TEST_MODEL_WITH_ID = "No TestModel with id = %d.";
    public static final String TEST_REPOSITORY_TEST_MODEL_WITH_EXIST_NAME = "Exist TestModel with same name = %s.";

    private Validator validator;
    private HashMap<Integer, TestModel> testModelMap = new HashMap<>();
    private HashMap<String, Integer> nameMap = new HashMap<>();
    private int curId = 0;
    private ArrayDeque<Integer> freeIds = new ArrayDeque<>();

    @Autowired
    protected TestRepositoryImpl(Validator validator) {
        this.validator = validator;
    }

    @Override
    public Optional<TestModel> findById(Integer id) {
        TestModel testModel = testModelMap.get(id);
        if(testModel != null) {
            return Optional.of(new TestModel(testModel.getId(), testModel.getName(), testModel.getSurname()));
        }
        return Optional.empty();
    }

    @Override
    public Optional<TestModel> findByNameAndNotId(String name, Integer id) {
        if(nameMap.containsKey(name)) {
            if(!nameMap.get(name).equals(id)) {
                return findById(id);
            }
        }
        return Optional.empty();
    }

    @Override
    public TestModel save(TestModel testModel) {
        repositoryValidTestModel(testModel);
        if(nameMap.containsKey(testModel.getName())) {
            throw TestServiceException.getFromQuery(String.format(TEST_REPOSITORY_TEST_MODEL_WITH_EXIST_NAME, testModel.getName()));
        }

        int newId = getNewId();
        testModel.setId(newId);
        TestModel newTestModel = new TestModel(testModel.getId(), testModel.getName(), testModel.getSurname());
        testModelMap.put(newId, newTestModel);
        nameMap.put(newTestModel.getName(),newId);

        return testModel;
    }

    @Override
    public TestModel updateById(TestModel testModel) {
        repositoryValidTestModel(testModel);

        int id = testModel.getId();
        Optional<TestModel> optionalOldTestModel = findById(id);
        optionalOldTestModel.orElseThrow(
                () -> TestServiceException.getFromQuery(String.format(TEST_REPOSITORY_NO_TEST_MODEL_WITH_ID, id))
        );

        String newName = testModel.getName();
        if(findByNameAndNotId(newName, id).isPresent()) {
            throw TestServiceException.getFromQuery(String.format(TEST_REPOSITORY_TEST_MODEL_WITH_EXIST_NAME, newName));
        }

        TestModel oldTestModel = optionalOldTestModel.get();
        String oldName = oldTestModel.getName();
        nameMap.remove(oldName);
        testModelMap.remove(id);

        TestModel newTestModel = new TestModel(id, testModel.getName(), testModel.getSurname());
        nameMap.put(newTestModel.getName(), id);
        testModelMap.put(id, newTestModel);

        return testModel;
    }

    private Integer getNewId() {
        if(!freeIds.isEmpty()) {
            return freeIds.pollFirst();
        }

        if(curId < Integer.MAX_VALUE) {
            return curId++;
        }
        throw TestServiceException.getFromService("Can't generate new id!");
    }

    //same with service, but can be dif in real
    private void repositoryValidTestModel(TestModel testModel) {
        Set<ConstraintViolation<TestModel>> constraintViolations = validator.validate(testModel);
        if(!constraintViolations.isEmpty()) {
            throw TestServiceException.getFromQuery(constraintViolations.iterator().next().getMessage());
        }
    }
}
