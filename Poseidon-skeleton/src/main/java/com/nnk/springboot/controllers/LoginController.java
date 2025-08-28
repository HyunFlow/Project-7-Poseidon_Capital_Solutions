package com.nnk.springboot.controllers;

import com.nnk.springboot.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Contrôleur gérant l'affichage et le traitement de la page de connexion.
 */
@RequiredArgsConstructor
@Controller
@RequestMapping("/app")
public class LoginController {

    @Autowired
    private UserService userService;

    /**
     * Affiche la page de connexion.
     *
     * @param auth objet d’authentification courant (peut être anonyme ou connecté)
     * @return redirection vers la liste si l’utilisateur est déjà authentifié,
     *         sinon la vue de la page de connexion
     */
    @GetMapping("/login")
    public String login(Authentication auth) {
        if(auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {
            return "redirect:/bidList/list";
        }
        return "appLogin";
    }

    /**
     * Affiche la liste des utilisateurs.
     *
     * @param model modèle de la vue contenant les données
     * @return la vue affichant la liste complète des utilisateurs
     */
    @GetMapping("/secure/article-details")
    public String getAllUserArticles(Model model) {
        model.addAttribute("users", userService.loadAllUsers());
        return "user/list";
    }
}
