package com._4dconcept.evaluation.controller;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class RestApiControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void completeScenario() throws Exception {

        createDeveloper("alice", "2");
        createDeveloper("bob", "2");
        createDeveloper("carol", "1");
        createDeveloper("dave", "1");
        createDeveloper("eve", "1");
        createDeveloper("franck");

        MvcResult result = this.mockMvc.perform(post("/api/developers/list?all=true"))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.[*]", hasSize(6)))
                .andExpect(jsonPath("$.[?(@.name == 'bob' && @.projectName == 'Second Project')]").exists())
                .andExpect(jsonPath("$.[?(@.name == 'carol' && @.projectName == 'First Project')]").exists())
                .andExpect(jsonPath("$.[?(@.name == 'franck' && @.projectName == null)]").exists())
                .andReturn();
    }

    private void createDeveloper(String name, String projectId) throws Exception {
        this.mockMvc.perform(post("/api/developers/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \""+ name +"\", \"projectId\": \""+ projectId +"\"}"))
                .andExpect(status().isCreated());
    }
    private void createDeveloper(String name) throws Exception {
        this.mockMvc.perform(post("/api/developers/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \""+ name +"\"}"))
                .andExpect(status().isCreated());
    }

}