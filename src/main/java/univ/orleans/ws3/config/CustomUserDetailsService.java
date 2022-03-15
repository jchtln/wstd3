package univ.orleans.ws3.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import univ.orleans.ws3.modele.Utilisateur;

public class CustomUserDetailsService implements UserDetailsService {

    private static final String[] ROLES_ETU = {"ETU"};
    private static final String[] ROLES_ENSEIGNANT = {"ENSEIGNANT"};
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
       // Utilisateur utilisateur = Controller.getUtilisateurs().get(s);
        if (utilisateur==null) {
            throw  new UsernameNotFoundException("User "+s+" not found");
        }
        String[] roles = utilisateur.isEtu() ? ROLES_PROF: ROLES_ETU
        UserDetails userDetails = User.builder()
                .username(utilisateur.getLogin())
                .password(passwordEncoder.encode(utilisateur.getPassword()))
                .roles(roles)
                .build();

        return userDetails;
    }
}
