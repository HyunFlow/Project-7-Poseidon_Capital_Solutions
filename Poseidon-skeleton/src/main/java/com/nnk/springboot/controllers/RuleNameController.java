package com.nnk.springboot.controllers;

import com.nnk.springboot.dto.RuleDto;
import com.nnk.springboot.services.RuleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequiredArgsConstructor
@Controller
@RequestMapping("/ruleName")
public class RuleNameController {

    private final RuleService ruleService;

    @GetMapping("/list")
    public String home(Model model) {
        model.addAttribute("ruleNames", ruleService.loadAllRules());
        return "ruleName/list";
    }

    @GetMapping("/add")
    public String addRuleForm(Model model) {
        model.addAttribute("ruleName", new RuleDto());
        return "ruleName/add";
    }

    @PostMapping("/validate")
    public String validate(@Valid @ModelAttribute("ruleName") RuleDto request,
        BindingResult result) {
        if (result.hasErrors()) {
            return "ruleName/add";
        }
        try {
            ruleService.createRule(request);
        }catch (IllegalArgumentException e) {
            result.reject("globalError", e.getMessage());
            return "ruleName/add";
        }
        return "redirect:/ruleName/list";
    }

    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model, RedirectAttributes ra) {
        try {
            model.addAttribute("ruleName", ruleService.loadRuleById(id));
            return "ruleName/update";
        } catch (IllegalArgumentException e) {
            ra.addFlashAttribute("globalError", "RuleName not found");
            return "redirect:/ruleName/list";
        }
    }

    @PostMapping("/update/{id}")
    public String updateRuleName(@PathVariable("id") Integer id,
        @Valid @ModelAttribute("ruleName") RuleDto request,
        BindingResult result) {
        if (result.hasErrors()) {
            return "ruleName/update";
        }
        try {
            ruleService.updateRule(id, request);
        }catch (IllegalArgumentException e) {
            result.reject("globalError", e.getMessage());
            return "ruleName/update";
        }
        return "redirect:/ruleName/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteRuleName(@PathVariable("id") Integer id) {
        ruleService.deleteRule(id);
        return "redirect:/ruleName/list";
    }
}
