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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Order(1)
    public void userRegistrationEmailNotValid() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\": \"donald.com\", \"firstName\": \"Donald\", \"lastName\": \"Duck\", \"password\": \"Donald123!\"}")
        ).andExpect(status().is4xxClientError());
    }

    @Test
    @Order(2)
    public void userRegistrationPasswordNotValid() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\": \"donald@gmail.com\", \"firstName\": \"Donald\", \"lastName\": \"Duck\", \"password\": \"donald!\"}")
        ).andExpect(status().is4xxClientError());
    }

    @Test
    @Order(3)
    public void userRegistrationFirstNameEmpty() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\": \"donald@gmail.com\", \"firstName\": \"\", \"lastName\": \"Duck\", \"password\": \"donald!\"}")
        ).andExpect(status().is4xxClientError());
    }

    @Test
    @Order(4)
    public void userRegistrationLastNameEmpty() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\": \"donald@gmail.com\", \"firstName\": \"\", \"lastName\": \"\", \"password\": \"donald!\"}")
        ).andExpect(status().is4xxClientError());
    }

    @Test
    @Order(5)
    public void userRegistrationEmailEmpty() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\": \"\", \"firstName\": \"\", \"lastName\": \"Duck\", \"password\": \"donald!\"}")
        ).andExpect(status().is4xxClientError());
    }

    @Test
    @Order(6)
    public void userRegistrationPasswordEmpty() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\": \"\", \"firstName\": \"\", \"lastName\": \"Duck\", \"password\": \"donald!\"}")
        ).andExpect(status().is4xxClientError());
    }

    @Test
    @Order(7)
    public void userRegistrationSuccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\": \"donald@gmail.com\", \"firstName\": \"Donald\", \"lastName\": \"Duck\", \"password\": \"Donald123!\"}")
        ).andExpect(status().isOk());
    }

    @Test
    @Order(8)
    public void userRegistrationEmailAlreadyExists() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\": \"donald@gmail.com\", \"firstName\": \"Donald\", \"lastName\": \"Duck\", \"password\": \"Donald123!\"}")
        ).andExpect(status().is4xxClientError());
    }

    @Test
    @Order(9)
    public void userLoginSuccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\": \"donald@gmail.com\", \"password\": \"Donald123!\"}")
        ).andExpect(status().isOk());
    }

    @Test
    @Order(10)
    public void userLoginEmailIncorrect() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\": \"donald@gmail.com\", \"password\": \"Donald12345!\"}")
        ).andExpect(status().is4xxClientError());
    }

    @Test
    @Order(11)
    public void userLoginPasswordIncorrect() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\": \"donald@gmail.com\", \"password\": \"Donald12345!\"}")
        ).andExpect(status().is4xxClientError());
    }

    @Test
    @Order(12)
    public void userLoginAccountNotExisting() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\": \"mickey@gmail.com\", \"password\": \"Mickey123!\"}")
        ).andExpect(status().is4xxClientError());
    }

    @Test
    @Order(13)
    public void forgotPassword() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/forgot_password")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\": \"auction.abh@gmail.com\"}")
        ).andExpect(status().isOk());
    }

    @Test
    @Order(14)
    public void forgotPasswordInvalidEmail() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/forgot_password")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\": \"email\"}")
        ).andExpect(status().isBadRequest());
    }

    @Test
    @Order(15)
    public void resetPassword() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/reset_password")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"password\": \"Mickey123!\", \"token\": \"token\"}")
        ).andExpect(status().is4xxClientError());
    }

    @Test
    @Order(16)
    public void validToken() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/valid_token")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"token\": \"token\"}")
        ).andExpect(status().isOk());
    }

    @Test
    @Order(17)
    public void deactivateAccount() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/deactivate")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"password\": \"User123!\"}")
        ).andExpect(status().isUnauthorized());
    }
}
