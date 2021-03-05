package com.atlantbh.auctionapp.controller;

import com.atlantbh.auctionapp.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService categoryService;

    @Test
    public void getFeaturedRandomProducts() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .get("/products/featured/random")
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void getNewProducts() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .get("/products/new")
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void getLastProducts() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .get("/products/last")
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
    }
}
