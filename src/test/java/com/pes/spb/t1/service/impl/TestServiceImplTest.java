package com.pes.spb.t1.service.impl;

import com.pes.spb.t1.exception.TestServiceException;
import com.pes.spb.t1.model.TestModel;
import com.pes.spb.t1.repository.TestRepository;
import com.pes.spb.t1.service.TestService;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static com.pes.spb.t1.repository.impl.TestRepositoryImpl.TEST_REPOSITORY_TEST_MODEL_WITH_EXIST_NAME;
import static com.pes.spb.t1.service.impl.TestServiceImpl.TEST_SERVICE_IMPL_NO_TEST_MODEL_WITH_ID;
import static com.pes.spb.t1.service.impl.TestServiceImpl.TEST_SERVICE_IMPL_TEST_MODEL_WITH_EXIST_NAME;
import static com.pes.spb.t1.util.UtilParams.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class TestServiceImplTest {

    @MockBean
    private TestRepository testRepository;

    @Autowired
    private TestService testService;

    @Rule
    public ExpectedException testException = ExpectedException.none();

    @Test
    public void findByIdSuccess() {
        TestModel existTestModel = new TestModel(EXIST_TEST_MODEL_ID, NEW_TEST_MODEL_NAME, NEW_TEST_MODEL_SURNAME);

        when(testRepository.findById(EXIST_TEST_MODEL_ID)).thenReturn(Optional.of(existTestModel));

        TestModel findTestModel = testService.findById(EXIST_TEST_MODEL_ID);
        assertEquals(existTestModel.getId(), findTestModel.getId());
        assertEquals(existTestModel.getName(), findTestModel.getName());
        assertEquals(existTestModel.getSurname(), findTestModel.getSurname());

        verify(testRepository).findById(EXIST_TEST_MODEL_ID);
    }

    @Test
    public void findByNotExistId() {
        testException.expect(TestServiceException.class);
        testException.expectMessage(String.format(TEST_SERVICE_IMPL_NO_TEST_MODEL_WITH_ID, NOT_EXIST_TEST_MODEL_ID));

        when(testRepository.findById(EXIST_TEST_MODEL_ID)).thenReturn(Optional.empty());
        testService.findById(NOT_EXIST_TEST_MODEL_ID);
        verify(testRepository).findById(NOT_EXIST_TEST_MODEL_ID);

        testException = ExpectedException.none();
    }

    @Test
    public void saveSuccess() {
        TestModel newTestModel = new TestModel(null, NEW_TEST_MODEL_NAME, NEW_TEST_MODEL_SURNAME);
        TestModel saveRezTestModel = new TestModel(NEW_TEST_MODEL_ID, NEW_TEST_MODEL_NAME, NEW_TEST_MODEL_SURNAME);

        when(testRepository.save(newTestModel)).thenReturn(saveRezTestModel);

        TestModel savedTestModel = testService.save(newTestModel);
        assertEquals(savedTestModel.getId(), saveRezTestModel.getId());
        assertEquals(savedTestModel.getName(), saveRezTestModel.getName());
        assertEquals(savedTestModel.getSurname(), saveRezTestModel.getSurname());

        verify(testRepository).save(newTestModel);
    }

    @Test
    public void saveExistName() {
        TestModel existNameTestModel = new TestModel(null, EXIST_TEST_MODEL_NAME, NEW_TEST_MODEL_SURNAME);

        testException.expect(TestServiceException.class);
        testException.expectMessage(String.format(TEST_REPOSITORY_TEST_MODEL_WITH_EXIST_NAME, existNameTestModel.getName()));

        when(testRepository.save(existNameTestModel)).thenThrow(TestServiceException.getFromQuery(String.format(TEST_REPOSITORY_TEST_MODEL_WITH_EXIST_NAME, EXIST_TEST_MODEL_NAME)));
        testService.save(existNameTestModel);
        verify(testRepository).save(existNameTestModel);

        testException = ExpectedException.none();
    }

    @Test
    public void updateByIdSuccess() {
        TestModel testModel = new TestModel(EXIST_TEST_MODEL_ID, NEW_TEST_MODEL_NAME, NEW_TEST_MODEL_SURNAME);

        when(testRepository.findById(OLD_TEST_MODEL_ID)).thenReturn(Optional.of(testModel));
        when(testRepository.findByNameAndNotId(NEW_TEST_MODEL_NAME, OLD_TEST_MODEL_ID)).thenReturn(Optional.empty());
        when(testRepository.updateById(testModel)).thenReturn(testModel);

        TestModel updatedTestModel = testService.updateById(testModel);
        assertEquals(updatedTestModel.getId(), testModel.getId());
        assertEquals(updatedTestModel.getName(), testModel.getName());
        assertEquals(updatedTestModel.getSurname(), testModel.getSurname());

        verify(testRepository).findById(OLD_TEST_MODEL_ID);
        verify(testRepository).findByNameAndNotId(NEW_TEST_MODEL_NAME, OLD_TEST_MODEL_ID);
        verify(testRepository).updateById(testModel);
    }

    @Test
    public void updateByExistNameSuccess() {
        TestModel testModel = new TestModel(EXIST_TEST_MODEL_ID, EXIST_TEST_MODEL_NAME, NEW_TEST_MODEL_SURNAME);
        TestModel existNameTestModel = new TestModel(ANOTHER_EXIST_TEST_MODEL_ID, EXIST_TEST_MODEL_NAME, NEW_TEST_MODEL_SURNAME);

        testException.expect(TestServiceException.class);
        testException.expectMessage(String.format(TEST_SERVICE_IMPL_TEST_MODEL_WITH_EXIST_NAME, EXIST_TEST_MODEL_NAME));

        when(testRepository.findById(EXIST_TEST_MODEL_ID)).thenReturn(Optional.of(testModel));
        when(testRepository.findByNameAndNotId(EXIST_TEST_MODEL_NAME, EXIST_TEST_MODEL_ID)).thenReturn(Optional.of(existNameTestModel));

        testService.updateById(testModel);

        verify(testRepository).findById(OLD_TEST_MODEL_ID);
        verify(testRepository).findByNameAndNotId(EXIST_TEST_MODEL_NAME, EXIST_TEST_MODEL_ID);

        testException = ExpectedException.none();
    }
}