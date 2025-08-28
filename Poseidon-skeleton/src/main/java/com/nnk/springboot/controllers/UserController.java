package com.nnk.springboot.controllers;

import com.nnk.springboot.dto.UserDto;
import com.nnk.springboot.services.UserService;
import java.nio.file.AccessDeniedException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import jakarta.validation.Valid;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RequiredArgsConstructor
@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/list")
    public String home(Authentication auth, Model model) throws AccessDeniedException {

        if (auth != null && auth.isAuthenticated()) {
            UserDto userDto = userService.loadUserByUsername(auth.getName());
            if (!userDto.getRole().equals("ADMIN")) {
                System.out.println(userDto.getRole());
                throw new AccessDeniedException("Access denied");
            }
        }
        model.addAttribute("users", userService.loadAllUsers());
        return "user/list";
    }

    @GetMapping("/add")
    public String addUser(UserDto request, Model model) {
        model.addAttribute("user", request);
        return "user/add";
    }

    @PostMapping("/validate")
    public String validate(@Valid @ModelAttribute(name = "user") UserDto request,
        BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "user/add";
        }
        try {
            userService.createUser(request);
        } catch (IllegalArgumentException e) {
            result.reject("globalError", e.getMessage());
            return "user/add";
        }
        return "redirect:/user/list";
    }

    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model, RedirectAttributes ra) {
        try {
        model.addAttribute("user", userService.loadUserById(id));
        return "user/update";
        } catch(IllegalArgumentException e) {
            ra.addFlashAttribute("globalError", "User not found");
            return "redirect:/user/list";
        }
    }

    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable("id") Integer id, @Valid @ModelAttribute(name = "user") UserDto request,
        BindingResult result) {
        if (result.hasErrors()) {
            return "user/update";
        }
        try {
            userService.updateUser(id, request);
        } catch (IllegalArgumentException e) {
            result.reject("globalError", e.getMessage());
            return "user/update";
        }
        return "redirect:/user/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Integer id) {
        userService.deleteUser(id);
        return "redirect:/user/list";
    }
}
