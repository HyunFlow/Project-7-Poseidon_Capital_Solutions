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

/**
 * Contrôleur de gestion des utilisateurs.
 * <p>
 * Points sensibles sécurité :
 * <ul>
 *   <li>Accès à la liste soumis au rôle <b>ADMIN</b>.</li>
 *   <li>Validation côté serveur via Bean Validation sur les formulaires.</li>
 *   <li>Mots de passe encodés au niveau du service.</li>
 * </ul>
 */
@RequiredArgsConstructor
@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    /**
     * Affiche la liste des utilisateurs.
     * <p>Contrôle d'accès : autorisé uniquement aux utilisateurs authentifiés ayant le rôle {@code ADMIN}.
     *
     * @param auth  contexte d'authentification courant
     * @param model modèle de vue pour exposer les données
     * @return la vue {@code user/list} si autorisé, sinon lève une exception
     * @throws AccessDeniedException si l'utilisateur authentifié n'a pas le rôle requis
     */
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

    /**
     * Affiche le formulaire de création d'utilisateur.
     *
     * @param request DTO initialisé (utilisé par Thymeleaf pour le binding)
     * @param model   modèle de vue
     * @return la vue {@code user/add}
     */
    @GetMapping("/add")
    public String addUser(UserDto request, Model model) {
        model.addAttribute("user", request);
        return "user/add";
    }

    /**
     * Traite la soumission du formulaire de création.
     * <p>
     * Sécurité : le mot de passe est encodé dans le service. Les erreurs de
     * validation et les conflits (username déjà utilisé) sont renvoyés à la vue.
     *
     * @param request DTO validé par Bean Validation
     * @param result  résultats de validation
     * @param model   modèle de vue
     * @return redirection vers {@code /user/list} en cas de succès, sinon {@code user/add}
     */
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

    /**
     * Affiche le formulaire de mise à jour d'un utilisateur.
     *
     * @param id identifiant de l'utilisateur à modifier
     * @param model modèle de vue
     * @param ra attributs flash pour porter un message en cas d'erreur
     * @return la vue {@code user/update} si l'utilisateur existe, sinon redirection vers {@code /user/list}
     */
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

    /**
     * Traite la soumission du formulaire de mise à jour.
     * <p>
     * Sécurité : le mot de passe soumis est encodé par le service.
     *
     * @param id      identifiant de l'utilisateur à modifier
     * @param request DTO validé
     * @param result  résultats de validation
     * @return redirection vers {@code /user/list} en cas de succès, sinon {@code user/update}
     */
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

    /**
     * Supprime un utilisateur par identifiant.
     *
     * @param id identifiant de l'utilisateur à supprimer
     * @return redirection vers {@code /user/list}
     */
    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Integer id) {
        userService.deleteUser(id);
        return "redirect:/user/list";
    }
}
