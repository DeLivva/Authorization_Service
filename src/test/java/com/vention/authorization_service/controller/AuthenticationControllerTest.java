package com.vention.authorization_service.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vention.authorization_service.dto.request.UserRegistrationRequest;
import com.vention.authorization_service.dto.response.GlobalResponse;
import com.vention.authorization_service.service.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private AuthenticationController authenticationController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authenticationController).build();
    }

    @Test
    void testRegisterUser() throws Exception {
        // given
        GlobalResponse globalResponse = mock();
        UserRegistrationRequest request = new UserRegistrationRequest("test@gmail.com", "12345678");
        // when
        doReturn(globalResponse).when(authenticationService).registerUser(any());
        String requestBody = objectMapper.writeValueAsString(request);
        // then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isCreated());
        verify(authenticationService, times(1)).registerUser(any());
    }

    @Test
    void testRegisterUser2() throws Exception {
        // given
        GlobalResponse globalResponse = mock();
        UserRegistrationRequest request = new UserRegistrationRequest("test@gmail.com", "1234567891011111");
        // when
        doReturn(globalResponse).when(authenticationService).registerUser(any());
        String requestBody = objectMapper.writeValueAsString(request);
        // then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isCreated());
        verify(authenticationService, times(1)).registerUser(any());
    }

    @Test
    void testRegisterUserWillFail1() throws Exception {
        // given
        UserRegistrationRequest request = new UserRegistrationRequest("1324test@gmail.com", "123");
        // when
        String requestBody = objectMapper.writeValueAsString(request);
        // then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
        verify(authenticationService, never()).registerUser(any());
    }

    @Test
    void testRegisterUserWillFail2() throws Exception {
        // given
        UserRegistrationRequest request = new UserRegistrationRequest(" ", "123");
        // when
        String requestBody = objectMapper.writeValueAsString(request);
        // then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
        verify(authenticationService, never()).registerUser(any());
    }

    @Test
    void testRegisterUserWillFail3() throws Exception {
        // given
        UserRegistrationRequest request = new UserRegistrationRequest("test@gmail.com", "12345678910111116");
        // when
        String requestBody = objectMapper.writeValueAsString(request);
        // then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
        verify(authenticationService, never()).registerUser(any());
    }
}