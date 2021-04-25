package com.atlantbh.auctionapp.controller;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class WishlistControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void addToWishlist() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/wishlist/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"productId\": \"1\"}")
        ).andExpect(status().isUnauthorized());
    }

    @Test
    public void removeFromWishlist() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/wishlist/remove")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"productId\": \"1\"}")
        ).andExpect(status().isUnauthorized());
    }
}
