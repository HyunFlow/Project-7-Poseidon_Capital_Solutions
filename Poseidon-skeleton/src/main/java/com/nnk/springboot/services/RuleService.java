package com.nnk.springboot.services;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.dto.RuleDto;
import com.nnk.springboot.repositories.RuleNameRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RuleService {

    private final RuleNameRepository ruleNameRepository;

    @Transactional
    public void createRule(RuleDto request) {
        RuleName ruleName = new RuleName();
        ruleName.setName(request.getName());
        ruleName.setDescription(request.getDescription());
        ruleName.setJson(request.getJson());
        ruleName.setTemplate(request.getTemplate());
        ruleName.setSqlStr(request.getSqlStr());
        ruleName.setSqlPart(request.getSqlPart());

        ruleNameRepository.save(ruleName);
    }

    @Transactional
    public void updateRule(Integer id, RuleDto request) {
        RuleName currentRuleName = ruleNameRepository.findById(id)
            .orElseThrow(()-> new IllegalArgumentException("RuleName not found"));

        currentRuleName.setName(request.getName());
        currentRuleName.setDescription(request.getDescription());
        currentRuleName.setJson(request.getJson());
        currentRuleName.setTemplate(request.getTemplate());
        currentRuleName.setSqlStr(request.getSqlStr());
        currentRuleName.setSqlPart(request.getSqlPart());

        ruleNameRepository.save(currentRuleName);
    }

    @Transactional
    public void deleteRule(Integer id) {
        if(!ruleNameRepository.existsById(id)){
            throw new IllegalArgumentException("RuleName not found");
        }
        ruleNameRepository.deleteById(id);
    }

    public RuleDto loadRuleById(Integer id) {
        RuleName currentRuleName = ruleNameRepository.findById(id)
            .orElseThrow(()-> new IllegalArgumentException("RuleName not found"));

        return getRuleDto(currentRuleName);
    }

    public List<RuleDto> loadAllRules() {
        return ruleNameRepository.findAll().stream()
            .map(this::getRuleDto).toList();
    }

    private RuleDto getRuleDto(RuleName currentRuleName) {
        RuleDto ruleDto = new RuleDto();
        ruleDto.setId(currentRuleName.getId());
        ruleDto.setName(currentRuleName.getName());
        ruleDto.setDescription(currentRuleName.getDescription());
        ruleDto.setJson(currentRuleName.getJson());
        ruleDto.setTemplate(currentRuleName.getTemplate());
        ruleDto.setSqlStr(currentRuleName.getSqlStr());
        ruleDto.setSqlPart(currentRuleName.getSqlPart());

        return ruleDto;
    }
}
