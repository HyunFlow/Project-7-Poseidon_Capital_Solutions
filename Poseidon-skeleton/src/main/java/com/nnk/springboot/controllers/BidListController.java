package com.nnk.springboot.controllers;

import com.nnk.springboot.dto.BidListDto;
import com.nnk.springboot.services.BidListService;
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


@Controller
@RequiredArgsConstructor
@RequestMapping("/bidList")
public class BidListController {

    private final BidListService bidListService;

    @GetMapping("/list")
    public String home(Model model) {
        model.addAttribute("bids", bidListService.loadAllBidList());

        return "bidList/list";
    }

    @GetMapping("/add")
    public String addBidForm(Model model) {
        model.addAttribute("bid", new BidListDto());
        return "bidList/add";
    }

    @PostMapping("/validate")
    public String validate(@Valid @ModelAttribute("bid") BidListDto request, BindingResult result) {
        if (result.hasErrors()) {
            return "bidList/add";
        }
        try {
            bidListService.createBid(request);
        } catch (IllegalArgumentException e) {
            result.reject("globalError", e.getMessage());
            return "bidList/add";
        }
        return "redirect:/bidList/list";
    }

    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model, RedirectAttributes ra) {
        try {
            model.addAttribute("bid", bidListService.loadBidById(id));
            return "bidList/update";
        } catch (IllegalArgumentException e) {
            ra.addFlashAttribute("globalError", "Bid not found");
            return "redirect:/bidList/list";
        }
    }

    @PostMapping("/update/{id}")
    public String updateBid(@PathVariable("id") Integer id,
        @Valid @ModelAttribute("bid") BidListDto request,
        BindingResult result) {
        if (result.hasErrors()) {
            return "bidList/update";
        }
        try {
            bidListService.updateBid(id, request);
        } catch (IllegalArgumentException e) {
            result.reject("globalError", e.getMessage());
            return "bidList/update";
        }
        return "redirect:/bidList/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteBid(@PathVariable("id") Integer id) {
        bidListService.deleteBid(id);
        return "redirect:/bidList/list";
    }
}
