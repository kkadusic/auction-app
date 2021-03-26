package com.atlantbh.auctionapp.controller;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

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

    @Test
    public void getSearchProducts() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .get("/products/search")
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void countProductsForEachCategory() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .get("/products/search/count")
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void getDefaultSorted() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .get("/products/search?page=0")
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void getDefaultSortedPageInvalid() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .get("/products/search?page=first")
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void getNewestArrivalsSorted() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .get("/products/search?page=0&sort=start-date-desc")
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void getLastChanceSorted() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .get("/products/search?page=0&sort=end-date-asc")
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void getPriceAscendingSorted() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .get("/products/search?page=0&sort=price-asc")
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void getPriceDescendingSorted() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .get("/products/search?page=0&sort=price-desc")
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void getDefaultSortedSecondPage() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .get("/products/search?page=1")
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void getDefaultSortedForCategory() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .get("/products/search?category=home&page=0")
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void getPriceAscSortedForCategorySubcategoryProduct() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .get("/products/search?category=home&page=0&query=bag&sort=price-asc&subcategory=desk")
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
    }
}
