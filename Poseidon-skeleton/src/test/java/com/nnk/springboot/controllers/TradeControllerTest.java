package com.nnk.springboot.controllers;

import com.nnk.springboot.dto.TradeDto;
import com.nnk.springboot.services.TradeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TradeControllerTest {

    private MockMvc mockMvc;

    @Mock
    private TradeService tradeService;

    @InjectMocks
    private TradeController tradeController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(tradeController).build();
    }

    @Test
    void list_ok() throws Exception {
        List<TradeDto> list = new ArrayList<>();
        when(tradeService.loadAllTrades()).thenReturn(list);

        mockMvc.perform(get("/trade/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("trade/list"))
                .andExpect(model().attribute("trades", list));
    }

    @Test
    void addForm_ok() throws Exception {
        mockMvc.perform(get("/trade/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("trade/add"))
                .andExpect(model().attributeExists("trade"));
    }

    @Test
    void validate_success_redirectList() throws Exception {
        mockMvc.perform(post("/trade/validate")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("account", "acc")
                        .param("type", "type")
                        .param("buyQuantity", "10"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/trade/list"));
    }

    @Test
    void validate_failure_returnAdd() throws Exception {
        mockMvc.perform(post("/trade/validate")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("account", "")
                        .param("type", "")
                        .param("buyQuantity", "-1"))
                .andExpect(status().isOk())
                .andExpect(view().name("trade/add"));
    }

    @Test
    void showUpdate_success() throws Exception {
        TradeDto dto = new TradeDto();
        when(tradeService.loadTradeById(anyInt())).thenReturn(dto);

        mockMvc.perform(get("/trade/update/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("trade/update"))
                .andExpect(model().attribute("trade", dto));
    }

    @Test
    void showUpdate_notFound_redirectList() throws Exception {
        when(tradeService.loadTradeById(anyInt())).thenThrow(new IllegalArgumentException(""));

        mockMvc.perform(get("/trade/update/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/trade/list"));
    }

    @Test
    void update_success_redirectList() throws Exception {
        mockMvc.perform(post("/trade/update/1")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("account", "acc")
                        .param("type", "type")
                        .param("buyQuantity", "12.3"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/trade/list"));
    }

    @Test
    void update_validationError_returnUpdate() throws Exception {
        mockMvc.perform(post("/trade/update/1")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("account", "")
                        .param("type", "")
                        .param("buyQuantity", "-5"))
                .andExpect(status().isOk())
                .andExpect(view().name("trade/update"));
    }

    @Test
    void delete_redirects() throws Exception {
        mockMvc.perform(get("/trade/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/trade/list"));

        verify(tradeService).deleteTrade(1);
    }
}
