package com.atlantbh.auctionapp.controller;

import com.atlantbh.auctionapp.service.SubcategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SubcategoryController.class)
public class SubcategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SubcategoryService subcategoryService;

    @Test
    public void getRandomSubcategories() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .get("/subcategories/random")
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
    }
}
