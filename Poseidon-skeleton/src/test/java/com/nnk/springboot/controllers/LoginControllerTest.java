package com.nnk.springboot.controllers;

import com.nnk.springboot.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class LoginControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private LoginController loginController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(loginController).build();
    }

    @Test
    void login_whenAnonymous_returnsLoginView() throws Exception {
        mockMvc.perform(get("/app/login"))
            .andExpect(status().isOk())
            .andExpect(view().name("appLogin"));
    }

    @Test
    void login_whenAuthenticated_redirectsToBidList() throws Exception {
        Authentication auth = Mockito.mock(Authentication.class);
        when(auth.isAuthenticated()).thenReturn(true);

        mockMvc.perform(get("/app/login").principal(auth))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/bidList/list"));
    }

    @Test
    void login_whenErrorParam_stillShowsLoginView() throws Exception {
        mockMvc.perform(get("/app/login").param("error", ""))
            .andExpect(status().isOk())
            .andExpect(view().name("appLogin"));
    }

    @Test
    void login_whenLogoutParam_stillShowsLoginView() throws Exception {
        mockMvc.perform(get("/app/login").param("logout", ""))
            .andExpect(status().isOk())
            .andExpect(view().name("appLogin"));
    }
}
