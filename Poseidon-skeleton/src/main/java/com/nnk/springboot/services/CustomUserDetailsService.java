package com.nnk.springboot.services;

import com.nnk.springboot.domain.CustomUserDetails;
import com.nnk.springboot.domain.User;
import com.nnk.springboot.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Service de chargement des utilisateurs pour Spring Security.
 * <p>
 * Implémente {@link UserDetailsService} pour fournir les informations d'authentification
 * (identifiant, mot de passe, rôles) à partir de la base de données.
 */
@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Charge un utilisateur par son nom d'utilisateur.
     *
     * @param username le nom d'utilisateur fourni lors de la connexion
     * @return un {@link UserDetails} construit à partir de l'entité {@link User}
     * @throws UsernameNotFoundException si aucun utilisateur n'est trouvé pour ce nom
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException(username));
        return new CustomUserDetails(user);
    }
}
