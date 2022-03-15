package univ.orleans.ws3.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import univ.orleans.ws3.modele.Utilisateur;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    /*
        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.inMemoryAuthentication()
                    .withUser("fred").password("{noop}fred").roles("USER")
                    .and()
                    .withUser("admin").password("{noop}admin").roles("USER","ADMIN");
            {bcrypt} for BCryptPasswordEncoder,
            {noop} for NoOpPasswordEncoder,
            {pbkdf2} for Pbkdf2PasswordEncoder,
            {scrypt} for SCryptPasswordEncoder,
            {sha256} for StandardPasswordEncoder.
        }
    */
    @Bean
    @Override
    protected UserDetailsService userDetailsService() {
        UserDetails etudiant = Utilisateur.builder()
                .login("etu").motdepasse("{bcrypt}etu").roles("ETUDIANT").build();
        UserDetails prof = Utilisateur.builder()
                .login("prof").motdepasse("{bcrypt}prof").roles("ENSEIGNANT").build();
        return new InMemoryUserDetailsManager(etudiant,prof);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST,"/inscription").permitAll()
                .antMatchers(HttpMethod.POST, "/question").hasRole("ETUDIANT")
                //.antMatchers(HttpMethod.GET,"")
                .anyRequest().hasRole("USER")
                .and().httpBasic()
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }



    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}