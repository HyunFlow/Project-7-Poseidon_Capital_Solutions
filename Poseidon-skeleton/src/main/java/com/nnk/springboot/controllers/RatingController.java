package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.dto.RatingDto;
import com.nnk.springboot.services.RatingService;
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
@RequestMapping("/rating")
public class RatingController {

    private final RatingService ratingService;

    @GetMapping("/list")
    public String home(Model model) {
        model.addAttribute("ratings", ratingService.loadAllRatings());
        return "rating/list";
    }

    @GetMapping("/add")
    public String addRatingForm(Model model) {
        model.addAttribute("rating", new RatingDto());
        return "rating/add";
    }

    @PostMapping("/validate")
    public String validate(@Valid @ModelAttribute("rating") RatingDto request,
        BindingResult result) {
        if (result.hasErrors()) {
            return "rating/add";
        }
        try {
            ratingService.createRating(request);
        } catch (IllegalArgumentException e) {
            result.reject("globalError", e.getMessage());
            return "rating/add";
        }
        return "redirect:/rating/list";
    }

    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model, RedirectAttributes ra) {
        try {
            model.addAttribute("rating", ratingService.loadRatingById(id));
            return "rating/update";
        } catch (IllegalArgumentException e) {
            ra.addFlashAttribute("globalError", "Rating not found");
            return "redirect:/rating/list";
        }
    }

    @PostMapping("/update/{id}")
    public String updateRating(@PathVariable("id") Integer id,
        @Valid @ModelAttribute("rating") RatingDto request,
        BindingResult result) {
        if (result.hasErrors()) {
            return "rating/update";
        }
        try {
            ratingService.updateRating(id, request);
        } catch (IllegalArgumentException e) {
            result.reject("globalError", e.getMessage());
            return "rating/update";
        }
        return "redirect:/rating/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteRating(@PathVariable("id") Integer id) {
        ratingService.deleteRating(id);
        return "redirect:/rating/list";
    }
}
