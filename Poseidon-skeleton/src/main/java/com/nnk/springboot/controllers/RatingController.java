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
        ratingService.createRating(request);
        return "redirect:/rating/list";
    }

    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("rating", ratingService.loadRatingById(id));
        return "rating/update";
    }

    @PostMapping("/update/{id}")
    public String updateRating(@PathVariable("id") Integer id, @Valid @ModelAttribute("rating") RatingDto request,
        BindingResult result) {
        if (result.hasErrors()) {
            return "rating/update";
        }
        ratingService.updateRating(id, request);
        return "redirect:/rating/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteRating(@PathVariable("id") Integer id) {
        ratingService.deleteRating(id);
        return "redirect:/rating/list";
    }
}
