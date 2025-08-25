package com.nnk.springboot.controllers;

import com.nnk.springboot.dto.BidAddRequest;
import com.nnk.springboot.dto.BidUpdateRequest;
import com.nnk.springboot.repositories.UserRepository;
import com.nnk.springboot.services.BidListService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
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
    private final UserRepository userRepository;

    @GetMapping("/list")
    public String home(Authentication auth, Model model) {
        // TODO: call service find all bids to show to the view
        String username = auth.getName();
        if(userRepository.findByUsername(username).isPresent()){
            model.addAttribute("username", username);
        }
        model.addAttribute("bids", bidListService.loadAllBidList());
        model.addAttribute("remoteUser", auth.getName());

        return "bidList/list";
    }

    @GetMapping("/add")
    public String addBidForm(Model model) {
        model.addAttribute("bid", new BidAddRequest());
        return "bidList/add";
    }

    @PostMapping("/validate")
    public String validate(@Valid @ModelAttribute("bid") BidAddRequest request, BindingResult result) {

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
    public String updateBid(@PathVariable("id") Integer id, @Valid @ModelAttribute("bid") BidUpdateRequest request,
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
