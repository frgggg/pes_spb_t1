package com.pes.spb.t1.repository.impl;

import com.pes.spb.t1.exception.TestServiceException;
import com.pes.spb.t1.model.TestModel;
import com.pes.spb.t1.repository.TestRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.Validator;

import java.util.Optional;

import static com.pes.spb.t1.util.UtilParams.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TestRepositoryImplTest {

    @Autowired
    private Validator validator;
    private TestRepository repository;

    @Before
    public void init() {
        repository = new TestRepositoryImpl(validator);
    }

    @Test
    public void save() {
        TestModel forSave = new TestModel(null, NEW_TEST_MODEL_NAME, NEW_TEST_MODEL_SURNAME);
        TestModel save = repository.save(forSave);
        assertNotNull(save.getId());
        assertEquals(save.getName(), NEW_TEST_MODEL_NAME);
        assertEquals(save.getSurname(), NEW_TEST_MODEL_SURNAME);

        boolean exceptionInSaveExistName = false;
        try {
            repository.save(new TestModel(null, NEW_TEST_MODEL_NAME, NEW_TEST_MODEL_SURNAME));
        } catch (TestServiceException ex) {
            exceptionInSaveExistName = true;
        }
        assertTrue(exceptionInSaveExistName);
    }

    @Test
    public void find() {
        TestModel forSave = new TestModel(null, NEW_TEST_MODEL_NAME, NEW_TEST_MODEL_SURNAME);
        TestModel save = repository.save(forSave);
        TestModel find = repository.findById(save.getId()).get();
        assertEquals(save.getId(), find.getId());
        assertEquals(save.getName(), find.getName());
        assertEquals(save.getSurname(), find.getSurname());

        boolean notExistWithId1 = true;
        if(repository.findById(1).isPresent()) {
            notExistWithId1 = false;
        }
        assertTrue(notExistWithId1);
    }

    @Test
    public void updateSuccess() {
        TestModel forSave = new TestModel(null, NEW_TEST_MODEL_NAME, NEW_TEST_MODEL_SURNAME);
        TestModel save = repository.save(forSave);

        repository.updateById(new TestModel(save.getId(), NEW_ANOTHER_TEST_MODEL_NAME, NEW_ANOTHER_TEST_MODEL_SURNAME));
        TestModel find = repository.findById(save.getId()).get();
        assertEquals(save.getId(), find.getId());
        assertEquals(NEW_ANOTHER_TEST_MODEL_NAME, find.getName());
        assertEquals(NEW_ANOTHER_TEST_MODEL_SURNAME, find.getSurname());
    }

    @Test
    public void updateNotExistId() {

    }
}