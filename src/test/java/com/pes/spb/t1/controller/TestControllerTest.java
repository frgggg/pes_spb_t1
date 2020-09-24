package com.pes.spb.t1.controller;

import com.pes.spb.t1.controller.util.AllControllerUtil;
import com.pes.spb.t1.dto.TestModelDto;
import com.pes.spb.t1.exception.TestServiceException;
import com.pes.spb.t1.model.TestModel;
import com.pes.spb.t1.repository.TestRepository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.Optional;

import static com.pes.spb.t1.controller.ExceptionController.VALIDATION_PROBLEMS_PREFIX;
import static com.pes.spb.t1.controller.ExceptionController.VALIDATION_PROBLEM_PREFIX;
import static com.pes.spb.t1.controller.util.AllControllerUtil.testModelDtoToString;
import static com.pes.spb.t1.model.TestModel.NAME_MAX_LEN;
import static com.pes.spb.t1.model.TestModel.NAME_MIN_LEN;
import static com.pes.spb.t1.repository.impl.TestRepositoryImpl.TEST_REPOSITORY_TEST_MODEL_WITH_EXIST_NAME;
import static com.pes.spb.t1.service.impl.TestServiceImpl.TEST_SERVICE_IMPL_NO_TEST_MODEL_WITH_ID;
import static com.pes.spb.t1.util.UtilParams.*;
import static com.pes.spb.t1.validation.impl.TestModelValidationImpl.TEST_MODEL_NAME_NULL;
import static com.pes.spb.t1.validation.impl.TestModelValidationImpl.TEST_MODEL_NAME_WRONG_LEN;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Value("${base.path}")
    private String baseUrl;

    @MockBean
    private TestRepository testRepository;

    @Test
    public void getExist() throws Exception {

        TestModel existTestModel = new TestModel(EXIST_TEST_MODEL_ID, EXIST_TEST_MODEL_NAME, EXIST_TEST_MODEL_SURNAME);
        TestModelDto existTestModelDto = new TestModelDto(existTestModel.getId(), existTestModel.getName(), existTestModel.getSurname());
        String existTestModelAsString = testModelDtoToString(existTestModelDto);

        ResultMatcher resultMatcherStatus = status().isOk();
        String getExistTestModelUrl = baseUrl + "/get/" + EXIST_TEST_MODEL_ID;

        when(testRepository.findById(EXIST_TEST_MODEL_ID)).thenReturn(Optional.of(existTestModel));
        AllControllerUtil.testUtilGet(mockMvc, getExistTestModelUrl, existTestModelAsString, resultMatcherStatus);
        verify(testRepository).findById(EXIST_TEST_MODEL_ID);
    }

    @Test
    public void getNotExist() throws Exception {

        ResultMatcher resultMatcherStatus = status().isBadRequest();
        String getNotExistTestModelUrl = baseUrl + "/get/" + NOT_EXIST_TEST_MODEL_ID;

        String notExistTestModelExceptionMsg = String.format(TEST_SERVICE_IMPL_NO_TEST_MODEL_WITH_ID, NOT_EXIST_TEST_MODEL_ID);

        when(testRepository.findById(NOT_EXIST_TEST_MODEL_ID)).thenReturn(Optional.empty());
        AllControllerUtil.testUtilGet(mockMvc, getNotExistTestModelUrl, notExistTestModelExceptionMsg, resultMatcherStatus);
        verify(testRepository).findById(NOT_EXIST_TEST_MODEL_ID);
    }

    @Test
    public void postSuccess() throws Exception {

        TestModel newInTestModel = new TestModel(null, NEW_TEST_MODEL_NAME, NEW_TEST_MODEL_SURNAME);
        TestModelDto newInTestModelDto = new TestModelDto(newInTestModel.getId(), newInTestModel.getName(), newInTestModel.getSurname());

        TestModel newOutTestModel = new TestModel(NEW_TEST_MODEL_ID, newInTestModel.getName(), newInTestModel.getSurname());
        TestModelDto newOutTestModelDto = new TestModelDto(newOutTestModel.getId(), newOutTestModel.getName(), newOutTestModel.getSurname());

        String postUrl = baseUrl + "/post";
        ResultMatcher resultMatcherStatus = status().isCreated();
        String newOutTestModelDtoAsString = testModelDtoToString(newOutTestModelDto);

        when(testRepository.save(newInTestModel)).thenReturn(newOutTestModel);
        AllControllerUtil.testUtilPost(mockMvc, postUrl, newInTestModelDto, newOutTestModelDtoAsString, resultMatcherStatus);
        verify(testRepository).save(newInTestModel);
    }

    @Test
    public void postExistName() throws Exception {
        TestModel newExistNameInTestModel = new TestModel(null, NEW_EXIST_MAME_TEST_MODEL_NAME, NEW_EXIST_MAME_TEST_MODEL_SURNAME);
        TestModelDto newExistNameInTestModelDto = new TestModelDto(newExistNameInTestModel.getId(), newExistNameInTestModel.getName(), newExistNameInTestModel.getSurname());

        String postUrl = baseUrl + "/post";
        ResultMatcher resultMatcherStatus = status().isBadRequest();
        String answer = String.format(TEST_REPOSITORY_TEST_MODEL_WITH_EXIST_NAME, NEW_EXIST_MAME_TEST_MODEL_NAME);

        when(testRepository.save(newExistNameInTestModel)).thenThrow(TestServiceException.getFromQuery(String.format(TEST_REPOSITORY_TEST_MODEL_WITH_EXIST_NAME, NEW_EXIST_MAME_TEST_MODEL_NAME)));
        AllControllerUtil.testUtilPost(mockMvc, postUrl, newExistNameInTestModelDto, answer, resultMatcherStatus);
        verify(testRepository).save(newExistNameInTestModel);
    }

    @Test
    public void postToBigName() throws Exception {

        String tooBigName = getTooBig(NAME_MAX_LEN);

        TestModel newTooBigNameInTestModel = new TestModel(null, tooBigName, NEW_TEST_MODEL_SURNAME);
        TestModelDto newTooBigNameInTestModelDto = new TestModelDto(newTooBigNameInTestModel.getId(), newTooBigNameInTestModel.getName(), newTooBigNameInTestModel.getSurname());

        String postUrl = baseUrl + "/post";
        ResultMatcher resultMatcherStatus = status().isBadRequest();
        String answer = VALIDATION_PROBLEMS_PREFIX + VALIDATION_PROBLEM_PREFIX + TEST_MODEL_NAME_WRONG_LEN;

        AllControllerUtil.testUtilPost(mockMvc, postUrl, newTooBigNameInTestModelDto, answer, resultMatcherStatus);
    }

    @Test
    public void postNullName() throws Exception {

        String nullName = getNull();

        TestModel newNullNameInTestModel = new TestModel(null, nullName, NEW_TEST_MODEL_SURNAME);
        TestModelDto newNullNameInTestModelDto = new TestModelDto(newNullNameInTestModel.getId(), newNullNameInTestModel.getName(), newNullNameInTestModel.getSurname());

        String postUrl = baseUrl + "/post";
        ResultMatcher resultMatcherStatus = status().isBadRequest();
        String answer = VALIDATION_PROBLEMS_PREFIX + VALIDATION_PROBLEM_PREFIX + TEST_MODEL_NAME_NULL;

        AllControllerUtil.testUtilPost(mockMvc, postUrl, newNullNameInTestModelDto, answer, resultMatcherStatus);
    }

    @Test
    public void putSuccess() throws Exception {
        TestModel newDataInTestModel = new TestModel(OLD_TEST_MODEL_ID, NEW_TEST_MODEL_NAME, NEW_TEST_MODEL_SURNAME);
        TestModelDto newDataInTestModelDto = new TestModelDto(newDataInTestModel.getId(), newDataInTestModel.getName(), newDataInTestModel.getSurname());

        TestModel newDataOutTestModel = new TestModel(OLD_TEST_MODEL_ID, newDataInTestModel.getName(), newDataInTestModel.getSurname());
        TestModelDto newDataOutTestModelDto = new TestModelDto(newDataOutTestModel.getId(), newDataOutTestModel.getName(), newDataOutTestModel.getSurname());

        String postUrl = baseUrl + "/put" + OLD_TEST_MODEL_ID;
        ResultMatcher resultMatcherStatus = status().isOk();
        String newOutTestModelDtoAsString = testModelDtoToString(newDataOutTestModelDto);

        when(testRepository.findById(OLD_TEST_MODEL_ID)).thenReturn(Optional.of(newDataInTestModel));
        when(testRepository.findByNameAndNotId(NEW_TEST_MODEL_NAME, OLD_TEST_MODEL_ID)).thenReturn(Optional.empty());
        when(testRepository.updateById(newDataInTestModel)).thenReturn(newDataInTestModel);
        AllControllerUtil.testUtilPut(mockMvc, postUrl, newDataInTestModelDto, newOutTestModelDtoAsString, resultMatcherStatus);
        verify(testRepository).findById(OLD_TEST_MODEL_ID);
        verify(testRepository).findByNameAndNotId(NEW_TEST_MODEL_NAME, OLD_TEST_MODEL_ID);
        verify(testRepository).updateById(newDataInTestModel);
    }

    @Test
    public void putNotExistId() throws Exception {
        TestModel newNotExistIdInTestModel = new TestModel(NOT_EXIST_TEST_MODEL_ID, NEW_TEST_MODEL_NAME, NEW_TEST_MODEL_SURNAME);
        TestModelDto newNotExistIdInTestModelDto = new TestModelDto(newNotExistIdInTestModel.getId(), newNotExistIdInTestModel.getName(), newNotExistIdInTestModel.getSurname());

        String postUrl = baseUrl + "/put" + NOT_EXIST_TEST_MODEL_ID;
        ResultMatcher resultMatcherStatus = status().isBadRequest();
        String answer = String.format(TEST_SERVICE_IMPL_NO_TEST_MODEL_WITH_ID, NOT_EXIST_TEST_MODEL_ID);

        when(testRepository.findById(NOT_EXIST_TEST_MODEL_ID)).thenReturn(Optional.empty());
        AllControllerUtil.testUtilPut(mockMvc, postUrl, newNotExistIdInTestModelDto, answer, resultMatcherStatus);
        verify(testRepository).findById(NOT_EXIST_TEST_MODEL_ID);

    }

    @Test
    public void putNullName() throws Exception {
        TestModel newNullNameInTestModel = new TestModel(EXIST_TEST_MODEL_ID, getNull(), NEW_TEST_MODEL_SURNAME);
        TestModelDto newNullNameInTestModelDto = new TestModelDto(newNullNameInTestModel.getId(), newNullNameInTestModel.getName(), newNullNameInTestModel.getSurname());

        String postUrl = baseUrl + "/put" + NOT_EXIST_TEST_MODEL_ID;
        ResultMatcher resultMatcherStatus = status().isBadRequest();
        String answer = VALIDATION_PROBLEMS_PREFIX + VALIDATION_PROBLEM_PREFIX + TEST_MODEL_NAME_NULL;

        AllControllerUtil.testUtilPut(mockMvc, postUrl, newNullNameInTestModelDto, answer, resultMatcherStatus);
    }

}