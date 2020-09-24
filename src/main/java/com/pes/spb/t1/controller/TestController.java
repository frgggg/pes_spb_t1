package com.pes.spb.t1.controller;

import com.pes.spb.t1.dto.TestModelDto;
import com.pes.spb.t1.model.TestModel;
import com.pes.spb.t1.service.TestService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${base.path}")
public class TestController {

    private ModelMapper modelMapper;
    private TestService testService;

    @Autowired
    protected TestController(TestService testService, ModelMapper modelMapper) {
        this.testService = testService;
        this.modelMapper = modelMapper;
    }


    @GetMapping(value = "get/{id}")
    public TestModelDto get(@PathVariable("id") Integer id) {
        TestModel foundedTestModel = testService.findById(id);

        return modelMapper.map(foundedTestModel, TestModelDto.class);
    }

    @PutMapping(value = "/put{id}")
    @ResponseStatus(HttpStatus.OK)
    public TestModelDto put(@PathVariable("id") Integer id, @Validated @RequestBody TestModelDto testModelDto) {
        TestModel updateTestModel = new TestModel(id, testModelDto.getName(), testModelDto.getSurname());
        TestModel updatedTestModel = testService.updateBuId(updateTestModel);

        return modelMapper.map(updatedTestModel, TestModelDto.class);
    }

    @PostMapping(value = "/post")
    @ResponseStatus(HttpStatus.CREATED)
    public TestModelDto post(@Validated @RequestBody TestModelDto testModelDto) {
        TestModel newTestModel = new TestModel(null, testModelDto.getName(), testModelDto.getSurname());
        TestModel savedTestModel = testService.save(newTestModel);
        return modelMapper.map(
                savedTestModel,
                TestModelDto.class
        );
    }
}
