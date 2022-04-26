package univ.orleans.ws3.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import univ.orleans.ws3.modele1.FacadeUtilisateurs;
import univ.orleans.ws3.modele1.Utilisateur;
import univ.orleans.ws3.modele1.exception.UtilisateurInexistantException;

import javax.management.relation.Role;

public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private FacadeUtilisateurs facadeUtilisateurs;

    /**
     * Génération dynamique des détails d'authentification d'un utilisateur, selon son username.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Utilisateur utilisateur;
        try {
            utilisateur = facadeUtilisateurs.getUtilisateurByEmail(username);
        } catch (UtilisateurInexistantException e) {
            throw new UsernameNotFoundException(username);
        }

        String[] roles = getRoles(utilisateur.getEmail());

        return User.builder()
                .username(utilisateur.getEmail())
                .password(utilisateur.getEncodedPassword())
                .roles(roles)
                .build();
    }

    /**
     * Retourne les rôles d'un utilisateur, d'après son e-mail.
     */
    private static String[] getRoles(String email) {
        String domain = (email.split("@"))[1];

        switch (domain) {
            case "etu.univ-orleans.fr":
                return new String[] {
                        Role.ETUDIANT.name()
                };
            case "univ-orleans.fr":
                return new String[]{
                        Role.ETUDIANT.name(),
                        Role.ENSEIGNANT.name()
                };
            default:
                return new String[0];
        }
    }

}
