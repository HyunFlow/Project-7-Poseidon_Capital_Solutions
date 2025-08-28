package com.nnk.springboot.controllers;

import com.nnk.springboot.dto.RuleDto;
import com.nnk.springboot.services.RuleService;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class RuleNameControllerTest {

    private MockMvc mockMvc;

    @Mock
    private RuleService ruleService;

    @InjectMocks
    private RuleNameController ruleNameController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(ruleNameController).build();
    }

    @Test
    void list_ok() throws Exception {
        List<RuleDto> list = new ArrayList<>();
        when(ruleService.loadAllRules()).thenReturn(list);

        mockMvc.perform(get("/ruleName/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("ruleName/list"))
                .andExpect(model().attribute("ruleNames", list));
    }

    @Test
    void addForm_ok() throws Exception {
        mockMvc.perform(get("/ruleName/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("ruleName/add"))
                .andExpect(model().attributeExists("ruleName"));
    }

    @Test
    void validate_success_redirectList() throws Exception {
        mockMvc.perform(post("/ruleName/validate")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", "N")
                        .param("description", "D")
                        .param("json", "{}")
                        .param("template", "T")
                        .param("sqlStr", "select 1")
                        .param("sqlPart", "part"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/ruleName/list"));
    }

    @Test
    void validate_failure_returnAdd() throws Exception {
        String tooLong = "a".repeat(126);
        mockMvc.perform(post("/ruleName/validate")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", tooLong))
                .andExpect(status().isOk())
                .andExpect(view().name("ruleName/add"));
    }

    @Test
    void showUpdate_success() throws Exception {
        RuleDto dto = new RuleDto();
        when(ruleService.loadRuleById(anyInt())).thenReturn(dto);

        mockMvc.perform(get("/ruleName/update/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("ruleName/update"))
                .andExpect(model().attribute("ruleName", dto));
    }

    @Test
    void showUpdate_notFound_redirectList() throws Exception {
        when(ruleService.loadRuleById(anyInt())).thenThrow(new IllegalArgumentException(""));

        mockMvc.perform(get("/ruleName/update/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/ruleName/list"));
    }

    @Test
    void update_success_redirectList() throws Exception {
        mockMvc.perform(post("/ruleName/update/1")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", "N2"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/ruleName/list"));
    }

    @Test
    void update_nameTooLong_returnUpdate() throws Exception {
        String tooLong = "a".repeat(126);

        mockMvc.perform(post("/ruleName/update/1")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", tooLong))
                .andExpect(status().isOk())
                .andExpect(view().name("ruleName/update"));
    }

    @Test
    void delete_redirects() throws Exception {
        mockMvc.perform(get("/ruleName/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/ruleName/list"));

        verify(ruleService).deleteRule(1);
    }
}
