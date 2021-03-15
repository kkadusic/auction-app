package com.atlantbh.auctionapp.controller;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
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
public class BidControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getBidsForFirstProduct() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders
                .get("/bids/product/?id=1")
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void addBidForProduct() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/bids/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"price\": \"1000\", \"productId\": \"1\"}")
        ).andExpect(status().isUnauthorized());
    }
}
