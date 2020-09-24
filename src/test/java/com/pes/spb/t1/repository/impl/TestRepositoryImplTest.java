package com.pes.spb.t1.repository.impl;

import com.pes.spb.t1.model.TestModel;
import com.pes.spb.t1.repository.TestRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static com.pes.spb.t1.util.UtilParams.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TestRepositoryImplTest {

    @Autowired
    private TestRepository repository;

    @Test
    public void findByIdSuccess() {
        TestModel saved = repository.save(new TestModel(null, NEW_TEST_MODEL_NAME, NEW_TEST_MODEL_SURNAME));
        assertNotNull(saved.getId());
        assertEquals(saved.getName(), NEW_TEST_MODEL_NAME);
        assertEquals(saved.getSurname(), NEW_TEST_MODEL_SURNAME);
    }

    @Test
    public void findByNotExistId() {
        assertTrue(repository.findById(ANOTHER_NOT_EXIST_TEST_MODEL_ID).isEmpty());
    }

    @Test
    public void findByNameAndNotId() {
        assertTrue(
                repository.findByNameAndNotId(NOT_EXIST_TEST_MODEL_NAME, null).isEmpty()
        );

        TestModel save = repository.save(new TestModel(null, NOT_EXIST_TEST_MODEL_NAME, EXIST_TEST_MODEL_SURNAME));
        assertTrue(
                repository.findByNameAndNotId(NOT_EXIST_TEST_MODEL_NAME, save.getId()).isEmpty()
        );
    }

    @Test
    public void saveSuccess() {
        TestModel saved = repository.save(new TestModel(null, ANOTHER_FIRST_NEW_TEST_MODEL_NAME, ANOTHER_FIRST_NEW_TEST_MODEL_SURNAME));
        assertNotNull(saved.getId());
        assertEquals(saved.getName(), ANOTHER_FIRST_NEW_TEST_MODEL_NAME);
        assertEquals(saved.getSurname(), ANOTHER_FIRST_NEW_TEST_MODEL_SURNAME);
    }

    @Test
    public void updateById() {
        TestModel saved = repository.save(new TestModel(null, ANOTHER_SECOND_NEW_TEST_MODEL_NAME, ANOTHER_SECOND_NEW_TEST_MODEL_SURNAME));
        repository.updateById(new TestModel(saved.getId(), ANOTHER_SECOND_UPDATED_NEW_TEST_MODEL_NAME, ANOTHER_SECOND_NEW_TEST_MODEL_SURNAME));

        TestModel find = repository.findById(saved.getId()).get();
        assertEquals(find.getName(), ANOTHER_SECOND_UPDATED_NEW_TEST_MODEL_NAME);
    }
}