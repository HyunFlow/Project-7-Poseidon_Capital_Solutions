package com.nnk.springboot.controllers;

import com.nnk.springboot.dto.BidListDto;
import com.nnk.springboot.services.BidListService;
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

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class BidListControllerTest {

    private MockMvc mockMvc;

    @Mock
    private BidListService bidListService;

    @InjectMocks
    private BidListController bidListController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(bidListController).build();
    }

    @Test
    void list_ok() throws Exception {
        List<BidListDto> list = new ArrayList<>();
        when(bidListService.loadAllBidList()).thenReturn(list);

        mockMvc.perform(get("/bidList/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("bidList/list"))
                .andExpect(model().attribute("bids", list));
    }

    @Test
    void addForm_ok() throws Exception {
        mockMvc.perform(get("/bidList/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("bidList/add"))
                .andExpect(model().attributeExists("bid"));
    }

    @Test
    void validate_success_redirectToList() throws Exception {
        mockMvc.perform(post("/bidList/validate")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("account", "acc")
                        .param("type", "type")
                        .param("bidQuantity", "10"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/bidList/list"));
    }

    @Test
    void validate_failure_returnsAddView() throws Exception {
        mockMvc.perform(post("/bidList/validate")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("account", "")
                        .param("type", "")
                        .param("bidQuantity", "-1"))
                .andExpect(status().isOk())
                .andExpect(view().name("bidList/add"))
                .andExpect(model().attributeExists("bid"));
    }

    @Test
    void showUpdate_success() throws Exception {
        BidListDto dto = new BidListDto();
        when(bidListService.loadBidById(anyInt())).thenReturn(dto);

        mockMvc.perform(get("/bidList/update/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("bidList/update"))
                .andExpect(model().attribute("bid", dto));
    }

    @Test
    void showUpdate_notFound_redirectList() throws Exception {
        when(bidListService.loadBidById(anyInt())).thenThrow(new IllegalArgumentException("bid not found"));

        mockMvc.perform(get("/bidList/update/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/bidList/list"));
    }

    @Test
    void update_success_redirectList() throws Exception {
        mockMvc.perform(post("/bidList/update/1")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("account", "acc")
                        .param("type", "type")
                        .param("bidQuantity", "12.3"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/bidList/list"));
    }

    @Test
    void update_validationError_returnUpdateView() throws Exception {
        mockMvc.perform(post("/bidList/update/1")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("account", "")
                        .param("type", "")
                        .param("bidQuantity", "-5"))
                .andExpect(status().isOk())
                .andExpect(view().name("bidList/update"));
    }

    @Test
    void delete_redirects() throws Exception {
        mockMvc.perform(get("/bidList/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/bidList/list"));

        verify(bidListService).deleteBid(1);
    }
}
