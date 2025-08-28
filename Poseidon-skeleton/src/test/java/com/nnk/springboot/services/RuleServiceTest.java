package com.nnk.springboot.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.dto.RuleDto;
import com.nnk.springboot.repositories.RuleNameRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class RuleServiceTest {

    @Mock
    private RuleNameRepository ruleNameRepository;

    @InjectMocks
    private RuleService ruleService;

    @Test
    void createRule_savesEntityWithRequest() {
        RuleDto request = new RuleDto();
        request.setName("test");
        request.setDescription("description");
        request.setJson("json");
        request.setTemplate("template");
        request.setSqlStr("sql");
        request.setSqlPart("sqlPart");

        ruleService.createRule(request);

        ArgumentCaptor<RuleName> captor = ArgumentCaptor.forClass(RuleName.class);
        verify(ruleNameRepository).save(captor.capture());
        verifyNoMoreInteractions(ruleNameRepository);

        RuleName saved = captor.getValue();
        assertEquals("test", saved.getName());
        assertEquals("description", saved.getDescription());
        assertEquals("json", saved.getJson());
        assertEquals("template", saved.getTemplate());
        assertEquals("sql", saved.getSqlStr());
        assertEquals("sqlPart", saved.getSqlPart());
    }

    @Test
    void updateRule_updatesFoundEntityAndSaves() {
        Integer tradeId = 1;

        RuleName existing = new RuleName();
        existing.setId(tradeId);
        existing.setName("test");
        existing.setDescription("description");
        existing.setJson("json");
        existing.setTemplate("template");
        existing.setSqlStr("sql");
        existing.setSqlPart("sqlPart");

        when(ruleNameRepository.findById(tradeId)).thenReturn(Optional.of(existing));

        RuleDto request = new RuleDto();
        request.setName("test2");
        request.setDescription("description2");
        request.setJson("json2");
        request.setTemplate("template2");
        request.setSqlStr("sql2");
        request.setSqlPart("sqlPart2");

        ruleService.updateRule(tradeId, request);

        ArgumentCaptor<RuleName> captor = ArgumentCaptor.forClass(RuleName.class);
        verify(ruleNameRepository).findById(tradeId);
        verify(ruleNameRepository).save(captor.capture());
        verifyNoMoreInteractions(ruleNameRepository);

        RuleName saved = captor.getValue();
        assertEquals("test2", saved.getName());
        assertEquals("description2", saved.getDescription());
        assertEquals("json2", saved.getJson());
        assertEquals("template2", saved.getTemplate());
        assertEquals("sql2", saved.getSqlStr());
        assertEquals("sqlPart2", saved.getSqlPart());
    }

    @Test
    void updateRule_throws_whenNotFound() {
        Integer tradeId = 1;
        when(ruleNameRepository.findById(tradeId)).thenReturn(Optional.empty());

        RuleDto request = new RuleDto();
        request.setName("test");
        request.setDescription("description");
        request.setJson("json");
        request.setTemplate("template");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> ruleService.updateRule(tradeId, request));
        assertEquals("RuleName not found", ex.getMessage());

        verify(ruleNameRepository).findById(tradeId);
        verifyNoMoreInteractions(ruleNameRepository);
    }

    @Test
    void deleteRule_deletes_whenExists() {
        when(ruleNameRepository.existsById(1)).thenReturn(true);
        ruleService.deleteRule(1);

        verify(ruleNameRepository).existsById(1);
        verify(ruleNameRepository).deleteById(1);
    }

    @Test
    void deletRule_throws_whenNotFound() {
        when(ruleNameRepository.existsById(1)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> ruleService.deleteRule(1));
        verify(ruleNameRepository).existsById(1);
        verifyNoMoreInteractions(ruleNameRepository);
    }

}
