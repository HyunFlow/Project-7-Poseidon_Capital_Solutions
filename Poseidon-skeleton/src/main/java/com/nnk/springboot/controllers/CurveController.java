package com.nnk.springboot.controllers;

import com.nnk.springboot.dto.CurvePointDto;
import com.nnk.springboot.services.CurveService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import jakarta.validation.Valid;

@RequiredArgsConstructor
@Controller
@RequestMapping("/curvePoint")
public class CurveController {

    private final CurveService curveService;

    @RequestMapping("/list")
    public String home(Model model) {
        model.addAttribute("curvePoints", curveService.loadAllCurvePoints());

        return "curvePoint/list";
    }

    @GetMapping("/add")
    public String addBidForm(Model model) {
        model.addAttribute("curvePoint", new CurvePointDto());
        return "curvePoint/add";
    }

    @PostMapping("/validate")
    public String validate(@Valid @ModelAttribute("curvePoint") CurvePointDto request, BindingResult result) {
        if (result.hasErrors()) {
            return "curvePoint/add";
        }
            curveService.createCurvePoint(request);

        return "redirect:/curvePoint/list";
    }

    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("curvePoint", curveService.loadCurvePointById(id));
        return "curvePoint/update";
    }

    @PostMapping("/update/{id}")
    public String updateBid(@PathVariable("id") Integer id, @Valid @ModelAttribute("curvePoint") CurvePointDto request,
        BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "curvePoint/update";
        }

        curveService.updateCurvePoint(id, request);

        return "redirect:/curvePoint/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteBid(@PathVariable("id") Integer id, Model model) {
        curveService.deleteCurvePoint(id);
        return "redirect:/curvePoint/list";
    }
}
