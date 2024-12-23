package com.fernando.connected_minds_api.controllers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fernando.connected_minds_api.exceptions.EntityNotFoundException;
import com.fernando.connected_minds_api.exceptions.JWTTokenInvalidException;
import com.fernando.connected_minds_api.repositories.UserRepository;
import com.fernando.connected_minds_api.requests.LoginRequest;
import com.fernando.connected_minds_api.requests.RefreshTokenRequest;
import com.fernando.connected_minds_api.responses.AuthResponse;
import com.fernando.connected_minds_api.responses.error.ErrorResponse;
import com.fernando.connected_minds_api.services.AuthService;
import com.fernando.connected_minds_api.services.JWTService;
import java.util.UUID;

@WebMvcTest(AuthController.class)
public class AuthControllerTest {
    @MockBean
    private AuthService authService;

    @MockBean
    private JWTService jwtService;

    @MockBean
    private UserRepository userRepository;
    
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    

    @Test
    @WithMockUser(username = "user", roles = "USER")
    public void shouldAuthenticateUserWithSuccess() throws Exception {
        String instant = generateInstant(2024, 9, 5, 12, 30);
        UUID userID = UUID.randomUUID();
        
        AuthResponse authResponse = new AuthResponse(
            "jwt-token", 
            "refresh-token", 
            instant,
            userID,
            "fernandobarrosd",
            null,
            null,
            null
        );
        

        LoginRequest loginRequest = new LoginRequest("fernandotest@test.com", "ftest");

        when(authService.authenticate(loginRequest)).thenReturn(authResponse);

        MvcResult mvcResult = mockMvc
            .perform(post("/auth/login")
            .content(objectMapper.writeValueAsString(loginRequest))
            .contentType("application/json;charset=UTF-8")
            .accept("application/json;charset=UTF-8")
            .with(csrf()))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(authResponse)))
            .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        String request = mvcResult.getRequest().getContentAsString(); 

        assertEquals(authResponse, objectMapper.readValue(response, AuthResponse.class));
        assertEquals(loginRequest, objectMapper.readValue(request, LoginRequest.class));
        assertEquals("POST", mvcResult.getRequest().getMethod());
        assertEquals("/auth/login", mvcResult.getRequest().getRequestURI());
        assertEquals("application/json;charset=UTF-8", mvcResult.getRequest().getContentType());
        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    public void shouldAuthenticateUserWithError() throws Exception {
        LoginRequest loginRequest = new LoginRequest("fernandotest@test.com", "ftest");

        EntityNotFoundException exception = new EntityNotFoundException("User is not exists");

        when(authService.authenticate(loginRequest)).thenThrow(exception);

        MvcResult mvcResult = mockMvc.perform(post("/auth/login")
            .contentType("application/json;charset=UTF-8")
            .accept("application/json;charset=UTF-8")
            .with(csrf())
            .content(objectMapper.writeValueAsString(loginRequest)))
            .andExpect(status().isNotFound())
            .andReturn();

            String response = mvcResult.getResponse().getContentAsString();
            String request = mvcResult.getRequest().getContentAsString(); 

            var responseError = objectMapper.readValue(response, ErrorResponse.class);
            
            assertEquals("User is not exists", responseError.getMessage());
            assertEquals(HttpStatus.NOT_FOUND.value(), responseError.getStatusCode());
            assertEquals("/auth/login", responseError.getPath());
            assertEquals(loginRequest, objectMapper.readValue(request, LoginRequest.class));
            assertEquals("POST", mvcResult.getRequest().getMethod());
            assertEquals("/auth/login", mvcResult.getRequest().getRequestURI());
            assertEquals("application/json;charset=UTF-8", mvcResult.getRequest().getContentType());
            assertEquals(HttpStatus.NOT_FOUND.value(), mvcResult.getResponse().getStatus());

    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    public void shouldGenerateNewTokenWithSuccess() throws Exception {
        var refreshTokenRequest = new RefreshTokenRequest("refresh-token");
        var exception = new JWTTokenInvalidException("Refresh token is not valid");

        when(authService.generateNewToken(refreshTokenRequest)).thenThrow(exception);

        MvcResult mvcResult = mockMvc.perform(post("/auth/token")
            .contentType("application/json;charset=UTF-8")
            .accept("application/json;charset=UTF-8")
            .with(csrf())
            .content(objectMapper.writeValueAsString(refreshTokenRequest)))
            .andExpect(status().isBadRequest())
            .andReturn();

        String response = mvcResult.getResponse().getContentAsString();

        var responseError = objectMapper.readValue(response, ErrorResponse.class);

        assertNotNull(responseError);
        assertNotNull(responseError.getMessage());
        assertNotNull(responseError.getStatusCode());
        assertNotNull(responseError.getPath());
        


        assertEquals("Refresh token is not valid", responseError.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST.value(), responseError.getStatusCode());
        assertEquals("/auth/token", responseError.getPath());
        assertEquals("POST", mvcResult.getRequest().getMethod());
        assertEquals("application/json;charset=UTF-8", mvcResult.getRequest().getContentType());
        assertEquals(HttpStatus.BAD_REQUEST.value(), mvcResult.getResponse().getStatus());

    }

    private String generateInstant(int year, int month, int date, int hour, int minute) {
        return LocalDateTime.of(
            year, month, date, hour, minute)
        .toInstant(ZoneOffset.of("-03:00")).toString();
    }
}