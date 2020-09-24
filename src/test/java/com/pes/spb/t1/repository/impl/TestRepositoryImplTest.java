package com.pes.spb.t1.repository.impl;

import com.pes.spb.t1.exception.TestServiceException;
import com.pes.spb.t1.model.TestModel;
import com.pes.spb.t1.repository.TestRepository;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.Validator;

import java.util.Optional;

import static com.pes.spb.t1.repository.impl.TestRepositoryImpl.TEST_REPOSITORY_NO_TEST_MODEL_WITH_ID;
import static com.pes.spb.t1.repository.impl.TestRepositoryImpl.TEST_REPOSITORY_TEST_MODEL_WITH_EXIST_NAME;
import static com.pes.spb.t1.service.impl.TestServiceImpl.TEST_SERVICE_IMPL_NO_TEST_MODEL_WITH_ID;
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

    @Rule
    public ExpectedException testException = ExpectedException.none();


    @Test
    public void saveSuccess() {
        TestModel forSave = new TestModel(null, NEW_TEST_MODEL_NAME, NEW_TEST_MODEL_SURNAME);
        TestModel save = repository.save(forSave);
        assertNotNull(save.getId());
        assertEquals(save.getName(), NEW_TEST_MODEL_NAME);
        assertEquals(save.getSurname(), NEW_TEST_MODEL_SURNAME);
    }

    @Test
    public void saveExistName() {
        TestModel forSave = new TestModel(null, NEW_TEST_MODEL_NAME, NEW_TEST_MODEL_SURNAME);
        TestModel save = repository.save(forSave);

        testException.expect(TestServiceException.class);
        testException.expectMessage(String.format(TEST_REPOSITORY_TEST_MODEL_WITH_EXIST_NAME, NEW_TEST_MODEL_NAME));

        repository.save(new TestModel(null, save.getName(), save.getSurname()));

        testException = ExpectedException.none();
    }

    @Test
    public void find() {
        TestModel forSave = new TestModel(null, NEW_TEST_MODEL_NAME, NEW_TEST_MODEL_SURNAME);
        TestModel save = repository.save(forSave);
        TestModel find = repository.findById(save.getId()).get();
        assertEquals(save.getId(), find.getId());
        assertEquals(save.getName(), find.getName());
        assertEquals(save.getSurname(), find.getSurname());

        assertTrue(repository.findById(1).isEmpty());
    }

    @Test
    public void updateSuccess() {
        TestModel forSave = new TestModel(null, NEW_TEST_MODEL_NAME, NEW_TEST_MODEL_SURNAME);
        TestModel save = repository.save(forSave);

        repository.updateById(new TestModel(save.getId(), NEW_ANOTHER_TEST_MODEL_NAME, NEW_ANOTHER_TEST_MODEL_SURNAME));
        Optional<TestModel> optional = repository.findById(save.getId());
        optional.orElseThrow(() -> new IllegalStateException("updateSuccess(): no TestModel with id = " + save.getId()));
        TestModel find = optional.get();
        assertEquals(save.getId(), find.getId());
        assertEquals(NEW_ANOTHER_TEST_MODEL_NAME, find.getName());
        assertEquals(NEW_ANOTHER_TEST_MODEL_SURNAME, find.getSurname());
    }

    @Test
    public void updateNotExistId() {
        testException.expect(TestServiceException.class);
        testException.expectMessage(String.format(TEST_REPOSITORY_NO_TEST_MODEL_WITH_ID, NOT_EXIST_TEST_MODEL_ID ));
        repository.updateById(new TestModel(NOT_EXIST_TEST_MODEL_ID, NEW_TEST_MODEL_NAME, NEW_TEST_MODEL_SURNAME));
        testException = ExpectedException.none();
    }

    @Test
    public void updateExistName() {
        TestModel save1 = repository.save(new TestModel(null, NEW_TEST_MODEL_NAME, NEW_TEST_MODEL_SURNAME));
        TestModel save2 = repository.save(new TestModel(null, NEW_ANOTHER_TEST_MODEL_NAME, NEW_TEST_MODEL_SURNAME));
        testException.expect(TestServiceException.class);
        testException.expectMessage(String.format(TEST_REPOSITORY_TEST_MODEL_WITH_EXIST_NAME,  save2.getName()));
        repository.updateById(new TestModel(save1.getId(), save2.getName(), save1.getSurname()));
        testException = ExpectedException.none();
    }

    @Test
    public void findByNameAndNotId() {
        TestModel save1 = repository.save(new TestModel(null, NEW_TEST_MODEL_NAME, NEW_TEST_MODEL_SURNAME));
        TestModel save2 = repository.save(new TestModel(null, NEW_ANOTHER_TEST_MODEL_NAME, NEW_TEST_MODEL_SURNAME));

        String notExistName = save1.getName() + save2.getName();

        assertTrue(repository.findByNameAndNotId(save1.getName(), save2.getId()).isPresent());
        assertFalse(repository.findByNameAndNotId(save1.getName(), save1.getId()).isPresent());
        assertFalse(repository.findByNameAndNotId(notExistName, save1.getId()).isPresent());
    }
}