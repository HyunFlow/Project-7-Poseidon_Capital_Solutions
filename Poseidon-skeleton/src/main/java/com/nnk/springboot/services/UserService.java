package com.nnk.springboot.services;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.dto.UserDto;
import com.nnk.springboot.repositories.UserRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Crée un nouvel utilisateur après validation :
     * <ul>
     *     <li>Vérifie que le nom d'utilisateur n'est pas déjà utilisé.</li>
     *     <li>Encode le mot de passe avec {@link PasswordEncoder}.</li>
     *     <li>Enregistre l'utilisateur en base de données.</li>
     * </ul>
     *
     * @param request données de l’utilisateur (DTO)
     * @throws IllegalArgumentException si le nom d'utilisateur est déjà pris
     */
    @Transactional
    public void createUser(UserDto request) {
        if(userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username is already in use");
        }
        User user = User.builder()
            .fullName(request.getFullName())
            .username(request.getUsername())
            .password(passwordEncoder.encode(request.getPassword()))
            .role(request.getRole())
            .build();

        userRepository.save(user);
    }

    /**
     * Met à jour un utilisateur existant.
     * <ul>
     *     <li>Charge l'utilisateur par son ID.</li>
     *     <li>Met à jour les informations et encode le nouveau mot de passe.</li>
     * </ul>
     *
     * @param id identifiant de l’utilisateur
     * @param request nouvelles données utilisateur
     * @throws IllegalArgumentException si l'utilisateur n'existe pas
     */
    @Transactional
    public void updateUser(Integer id, UserDto request) {
        User currentUser = userRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));

        currentUser.setFullName(request.getFullName());
        currentUser.setUsername(request.getUsername());
        currentUser.setPassword(passwordEncoder.encode(request.getPassword()));
        currentUser.setRole(request.getRole());

        userRepository.save(currentUser);
    }

    /**
     * Supprime un utilisateur par son identifiant.
     *
     * @param id identifiant de l’utilisateur
     * @throws IllegalArgumentException si l'utilisateur n'existe pas
     */
    @Transactional
    public void deleteUser(Integer id) {
        if(!userRepository.existsById(id)) {
            throw new IllegalArgumentException("User not found");
        }
        userRepository.deleteById(id);
    }

    /**
     * Charge un utilisateur par son nom d’utilisateur.
     * <br/>⚠️ Utilisé dans le cadre de l’authentification.
     *
     * @param username nom d’utilisateur
     * @return données utilisateur (DTO)
     * @throws UsernameNotFoundException si aucun utilisateur n’est trouvé
     */
    public UserDto loadUserByUsername(String username) {
        User currentUser = userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return getUserDto(currentUser);
    }

    /**
     * Charge un utilisateur par son identifiant.
     *
     * @param id identifiant de l’utilisateur
     * @return données utilisateur (DTO)
     * @throws IllegalArgumentException si l'utilisateur n'existe pas
     */
    public UserDto loadUserById(Integer id) {
        User currentUser = userRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return getUserDto(currentUser);
    }

    /**
     * Retourne la liste de tous les utilisateurs.
     *
     * @return liste des utilisateurs (DTOs)
     */
    public List<UserDto> loadAllUsers() {
        return userRepository.findAll().stream()
            .map(this::getUserDto).toList();
    }

    private UserDto getUserDto(User currentUser) {
        UserDto userDto = new UserDto();
        userDto.setId(currentUser.getId());
        userDto.setFullName(currentUser.getFullName());
        userDto.setUsername(currentUser.getUsername());
        userDto.setPassword(currentUser.getPassword());
        userDto.setRole(currentUser.getRole());
        return userDto;
    }

}
