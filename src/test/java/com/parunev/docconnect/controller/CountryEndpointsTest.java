package com.parunev.docconnect.controller;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@Tag("integration")
@Sql("classpath:db/DbCountryPreparationScript.sql")
class CountryEndpointsTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGetCountries() throws Exception {
        mockMvc.perform(
                        get("/api/v1/countries")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].countryName").value("Bulgaria"))
                .andExpect(jsonPath("$[1].countryName").value("Paraguay"))
                .andExpect(jsonPath("$[2].countryName").value("Kosovo"));
    }

    @Test
    void testGetCountryById() throws Exception {
        mockMvc.perform(
                        get("/api/v1/countries/{id}", 1)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.countryName").value("Bulgaria"));
    }
}
