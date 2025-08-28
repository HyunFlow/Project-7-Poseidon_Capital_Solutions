package com.nnk.springboot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Configure la sécurité Spring Security pour utiliser une authentification basée sur la session
     * avec un formulaire de connexion personnalisé.
     *
     * <ul>
     *     <li>Autorise l'accès public aux ressources statique (CSS, JS, images) et à la page de login.</li>
     *     <li>Restreint l'accès aux pages d'administration aux utilisateurs avec l'autorité {@code ADMIN}.</li>
     *     <li>Configure un formulaire de login avec URL et paramètres personnalisés.</li>
     *     <li>Active la gestion de session (une seule session active par utilisateur).</li>
     * </ul>
     *
     * @param http l'objet {@link HttpSecurity} à configurer
     * @return la chaîne de filtres de sécurité configurée
     * @throws Exception si la configuration échoue
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/","/css/**", "/js/**", "/images/**", "/app/login",
                    "/app/perform-login", "/register")
                .permitAll()
                .requestMatchers("/user/**", "/app/secure/**").hasAuthority("ADMIN")
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/app/login")
                .loginProcessingUrl("/app/perform-login")
                .defaultSuccessUrl("/bidList/list", true)
                .failureUrl("/app/login?error")
                .usernameParameter("username")
                .passwordParameter("password")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/app/login")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
            )
            .sessionManagement(sm -> sm
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .sessionFixation(sessiongFixation -> sessiongFixation.migrateSession())
                .maximumSessions(1)
                .maxSessionsPreventsLogin(false)
            );

        return http.build();
    }

    /**
     * Fournit un {@link PasswordEncoder} basé sur BCrypt pour :
     * <ul>
     *   <li>Encoder les mots de passe des utilisateurs avant stockage en base.</li>
     *   <li>Vérifier les mots de passe lors de l’authentification.</li>
     * </ul>
     *
     * @return un encodeur de mot de passe sécurisé utilisant BCrypt
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
