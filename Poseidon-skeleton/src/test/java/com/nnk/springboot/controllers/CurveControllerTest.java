package com.nnk.springboot.controllers;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.nnk.springboot.dto.CurvePointDto;
import com.nnk.springboot.services.CurveService;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class CurveControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CurveService curveService;

    @InjectMocks
    private CurveController curveController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(curveController).build();
    }

    @Test
    void list_ok() throws Exception {
        List<CurvePointDto> list = new ArrayList<>();
        when(curveService.loadAllCurvePoints()).thenReturn(list);

        mockMvc.perform(get("/curvePoint/list"))
            .andExpect(status().isOk())
            .andExpect(view().name("curvePoint/list"))
            .andExpect(model().attribute("curvePoints", list));
    }

    @Test
    void addForm_ok() throws Exception {
        mockMvc.perform(get("/curvePoint/add"))
            .andExpect(status().isOk())
            .andExpect(view().name("curvePoint/add"))
            .andExpect(model().attributeExists("curvePoint"));
    }

    @Test
    void validate_success_redirectList() throws Exception {
        mockMvc.perform(post("/curvePoint/validate")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("curveId", "1")
                .param("term", "10")
                .param("value", "20"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/curvePoint/list"));
    }

    @Test
    void validate_failure_returnAdd() throws Exception {
        mockMvc.perform(post("/curvePoint/validate")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("term", "")
                .param("value", ""))
            .andExpect(status().isOk())
            .andExpect(view().name("curvePoint/add"));
    }

    @Test
    void showUpdate_success() throws Exception {
        CurvePointDto dto = new CurvePointDto();
        when(curveService.loadCurvePointById(anyInt())).thenReturn(dto);

        mockMvc.perform(get("/curvePoint/update/1"))
            .andExpect(status().isOk())
            .andExpect(view().name("curvePoint/update"))
            .andExpect(model().attribute("curvePoint", dto));
    }

    @Test
    void showUpdate_notFound_redirectList() throws Exception {
        when(curveService.loadCurvePointById(anyInt())).thenThrow(new IllegalArgumentException(""));

        mockMvc.perform(get("/curvePoint/update/1"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/curvePoint/list"));
    }

    @Test
    void update_success_redirectList() throws Exception {
        mockMvc.perform(post("/curvePoint/update/1")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("curveId","1")
                .param("term", "11")
                .param("value", "21"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/curvePoint/list"));
    }

    @Test
    void update_validationError_returnUpdate() throws Exception {
        mockMvc.perform(post("/curvePoint/update/1")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("term", "")
                .param("value", ""))
            .andExpect(status().isOk())
            .andExpect(view().name("curvePoint/update"));
    }

    @Test
    void delete_redirects() throws Exception {
        mockMvc.perform(get("/curvePoint/delete/1"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/curvePoint/list"));

        verify(curveService).deleteCurvePoint(1);
    }
}
