package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.dto.TradeDto;
import com.nnk.springboot.services.TradeService;
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
@RequestMapping("/trade")
public class TradeController {

    private final TradeService tradeService;

    @GetMapping("/list")
    public String home(Model model) {
        model.addAttribute("trades", tradeService.loadAllTrades());
        return "trade/list";
    }

    @GetMapping("/add")
    public String addUser(Model model) {
        model.addAttribute("trade", new TradeDto());
        return "trade/add";
    }

    @PostMapping("/validate")
    public String validate(@Valid @ModelAttribute("trade") TradeDto request, BindingResult result) {
        if (result.hasErrors()) {
            return "trade/add";
        }
        try{
            tradeService.createTrade(request);
        }catch (IllegalArgumentException e) {
            result.reject("globalError", e.getMessage());
            return "trade/add";
        }
        return "redirect:/trade/list";
    }

    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model, RedirectAttributes ra) {
        try {
            model.addAttribute("trade", tradeService.loadTradeById(id));
            return "trade/update";
        } catch (IllegalArgumentException e) {
            ra.addFlashAttribute("globalError", "Trade not found");
            return "redirect:/trade/list";
        }
    }

    @PostMapping("/update/{id}")
    public String updateTrade(@PathVariable("id") Integer id,
        @Valid @ModelAttribute("trade") TradeDto request, BindingResult result) {
        if(result.hasErrors()) {
            return "trade/update";
        }
        try {
            tradeService.updateTrade(id, request);
        } catch (IllegalArgumentException e) {
            result.reject("globalError", e.getMessage());
            return "trade/update";
        }
        return "redirect:/trade/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteTrade(@PathVariable("id") Integer id) {
        tradeService.deleteTrade(id);
        return "redirect:/trade/list";
    }
}
