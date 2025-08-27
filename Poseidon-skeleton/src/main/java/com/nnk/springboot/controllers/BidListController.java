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



@Controller
@RequiredArgsConstructor
@RequestMapping("/bidList")
public class BidListController {

    private final BidListService bidListService;

    @GetMapping("/list")
    public String home(Model model) {
        // TODO: call service find all bids to show to the view
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

        try{
            bidListService.createBid(request);
        } catch(IllegalArgumentException e){
            String msg = e.getMessage();
            if(msg != null && msg.contains("must be positive")) {
                result.rejectValue("bidQuantity", "bidQuantity.negative", msg);
            }
            return "bidList/add";
        }
        return "redirect:/bidList/list";
    }

    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("bid", bidListService.loadForUpdate(id));
        return "bidList/update";
    }

    @PostMapping("/update/{id}")
    public String updateBid(@PathVariable("id") Integer id, @Valid @ModelAttribute("bid") BidListDto request,
        BindingResult result) {
        if (result.hasErrors()) {
            return "bidList/update";
        }

        bidListService.updateBid(id, request);

        return "redirect:/bidList/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteBid(@PathVariable("id") Integer id) {
        bidListService.deleteBid(id);
        return "redirect:/bidList/list";
    }
}
