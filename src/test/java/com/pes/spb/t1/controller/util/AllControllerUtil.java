package com.pes.spb.t1.controller.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pes.spb.t1.dto.TestModelDto;
import com.pes.spb.t1.model.TestModel;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import java.io.IOException;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

public class AllControllerUtil {

    public static String testModelDtoToString(TestModelDto testModelDto) {
        return String.format(
                "{\"id\":%d,\"name\":\"%s\",\"surname\":\"%s\"}"
                , testModelDto.getId()
                , testModelDto.getName()
                , testModelDto.getSurname()
        );
    }


    public static byte[] convertObjectToJsonBytes(Object object) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper.writeValueAsBytes(object);
    }

    public static void testUtilPost(MockMvc mockMvc, String url, Object inObject, String answer, ResultMatcher resultMatcherStatus) throws Exception{
        mockMvc.perform(
                post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertObjectToJsonBytes(inObject))
        )
                .andExpect(resultMatcherStatus)
                .andExpect(content().string(answer));

    }

    public static void testUtilPut(MockMvc mockMvc, String url, Object inObject, String answer, ResultMatcher resultMatcherStatus) throws Exception{
        mockMvc.perform(
                put(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(convertObjectToJsonBytes(inObject))
        )
                .andExpect(resultMatcherStatus)
                .andExpect(content().string(answer));

    }

    public static void testUtilGet(MockMvc mockMvc, String url, String answer, ResultMatcher resultMatcherStatus) throws Exception{
        mockMvc.perform(get(url))
                .andExpect(resultMatcherStatus)
                .andExpect(content().string(containsString(answer)));

    }

    public static void testUtilDelete(MockMvc mockMvc, String url, String answer, ResultMatcher resultMatcherStatus) throws Exception{
        mockMvc.perform(delete(url))
                .andExpect(resultMatcherStatus)
                .andExpect(content().string(containsString(answer)));

    }
}
