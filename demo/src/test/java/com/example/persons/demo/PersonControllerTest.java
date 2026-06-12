package com.example.persons.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @Test
    void shouldReturnFirstFiftyPersons() throws Exception {
        mockMvc.perform(get("/persons/page?page=0&size=50"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(50));
    }
    @Test
    void shouldReturnGenderCounts() throws Exception {
        mockMvc.perform(get("/persons/genders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.Male").exists())
                .andExpect(jsonPath("$.Female").exists());
    }

    @Test
    void shouldFilterByGender() throws Exception {
        mockMvc.perform(get("/persons/gender/Male"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].gender").value("Male"));
    }

    @Test
    void shouldReturnGenreCounts() throws Exception {
        mockMvc.perform(get("/persons/genres"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.Drama").exists())
                .andExpect(jsonPath("$.Comedy").exists());
    }

    @Test
    void shouldReturnPaginatedPersons() throws Exception {
        mockMvc.perform(get("/persons/page?page=1&size=50"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(50));
    }
}