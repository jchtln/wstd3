package univ.orleans.ws3.controleur;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import univ.orleans.ws3.modele.FacadeUtilisateurs;
import univ.orleans.ws3.modele.Question;
import univ.orleans.ws3.modele.Utilisateur;
import univ.orleans.ws3.modele.exception.LoginDejaUtiliseException;

import java.net.URI;
import java.security.Principal;

@RestController
@RequestMapping("/ws3")
public class Controleur {

    FacadeUtilisateurs facadeUtilisateurs;



    @PostMapping("/inscription")
    public ResponseEntity<Utilisateur> creerCompte(@RequestParam String login,@RequestParam String mdp){

        try {
           int num = facadeUtilisateurs.inscrireUtilisateur(login,mdp);
        } catch (LoginDejaUtiliseException e) {
            e.printStackTrace();
        }

        return ResponseEntity.created(URI.create("http://localhost:8080/ws3/inscription/"+ num))
                .body("L'étudiant a bien été crée !");


    }

    @PostMapping("/")
    public ResponseEntity<Question> poserQuestion(){

    }
}


