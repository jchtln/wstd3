package univ.orleans.ws3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import univ.orleans.ws3.modele1.FacadeUtilisateurs;
import univ.orleans.ws3.modele1.exception.LoginDejaUtiliseException;

@SpringBootApplication
public class Ws3Application {

    public static void main(String[] args) {
        SpringApplication.run(Ws3Application.class, args);
    }

    @Autowired
    FacadeUtilisateurs facadeUtilisateurs;

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {

            try {
                facadeUtilisateurs.inscrireUtilisateur("yohan.boichut@univ-orleans.fr",
                        "monMotDePasse");
            } catch (LoginDejaUtiliseException e) {
                e.printStackTrace();
            }

            try {
                facadeUtilisateurs.inscrireUtilisateur("gerard.menvussaa@etu.univ-orleans.fr",
                        "sonMotDePasse");
            } catch (LoginDejaUtiliseException e) {
                e.printStackTrace();
            }


        };
    }

}
