package univ.orleans.ws3.controleur;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import univ.orleans.ws3.modele.FacadeApplication;
import univ.orleans.ws3.modele.exceptions.UtilisateurInexistantException;
import univ.orleans.ws3.modele1.FacadeUtilisateurs;
import univ.orleans.ws3.modele1.Question;
import univ.orleans.ws3.modele1.Utilisateur;
import univ.orleans.ws3.modele1.exception.LoginDejaUtiliseException;

import java.net.URI;
import java.security.Principal;

@RestController
@RequestMapping("/api")
public class Controleur {

    @Autowired
    private FacadeUtilisateurs facadeUtilisateurs;
    @Autowired
    private FacadeApplication facadeApplication;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/utilisateurs")
    public ResponseEntity<Utilisateur> inscrire(@RequestParam String email,
                                                @RequestParam String password,
                                                UriComponentsBuilder base) {
        Utilisateur utilisateur;
        try {
            utilisateur = facadeUtilisateurs.inscrireUtilisateur(email, passwordEncoder.encode(password));
        } catch (LoginDejaUtiliseException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        URI location = base.path("/api/utilisateurs/{idUtilisateur}")
                .buildAndExpand(utilisateur.getIdUtilisateur())
                .toUri();
        return ResponseEntity.created(location).body(utilisateur);
    }

    @PostMapping("/utilisateurs/{idUtilisateur}/questions")
    public ResponseEntity<Question> ajouterQuestion(@PathVariable int idUtilisateur,
                                                    @RequestBody String libelleQuestion,
                                                    Principal principal,
                                                    UriComponentsBuilder base) throws UtilisateurInexistantException {
        Utilisateur utilisateur = facadeUtilisateurs.getUtilisateurByEmail(principal.getName());
        if (idUtilisateur == utilisateur.getIdUtilisateur()) {
            Question question = facadeApplication.ajouterUneQuestion(idUtilisateur, libelleQuestion);
            URI location = base.path("/api/utilisateurs/{idUtilisateur}/questions/{idQuestion}")
                    .buildAndExpand(idUtilisateur, question.getIdQuestion())
                    .toUri();
            return ResponseEntity.created(location).body(question);
        } else
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PatchMapping("/questions/{idQuestion}")
    public ResponseEntity<String> repondreQuestion(@PathVariable String idQuestion,
                                                   @RequestBody String reponse) throws QuestionInexistanteException {
        this.facadeApplication.repondreAUneQuestion(idQuestion, reponse);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/utilisateurs/{idUtilisateur}/questions")
    public ResponseEntity<Collection<Question>> getSomeQuestionsByUtilisateur(@PathVariable int idUtilisateur,
                                                                              @RequestParam Optional<String> filtre,
                                                                              Principal principal) {
        try {
            int id = facadeUtilisateurs.getUtilisateurIntId(principal.getName());
            String f = filtre.orElse("sansFiltre");

            if (idUtilisateur == id) {
                switch (f) {
                    case "sansReponse":
                        return ResponseEntity.ok(facadeApplication.getQuestionsSansReponsesByUser(idUtilisateur));
                    case "avecReponse":
                        return ResponseEntity.ok(facadeApplication.getQuestionsAvecReponsesByUser(idUtilisateur));
                    default:
                        return ResponseEntity.ok(facadeApplication.getToutesLesQuestionsByUser(idUtilisateur));
                }
            } else
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        } catch (UtilisateurInexistantException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/utilisateurs/{idUtilisateur}/questions/{idQuestion}")
    public ResponseEntity<Question> getQuestionByUtilisateur(@PathVariable int idUtilisateur,
                                                             @PathVariable String idQuestion,
                                                             Principal principal) throws UtilisateurInexistantException, AccessIllegalAUneQuestionException, QuestionInexistanteException {
        int id = facadeUtilisateurs.getUtilisateurIntId(principal.getName());
        if (idUtilisateur == id) {
            Question question = facadeApplication.getQuestionByIdPourUnUtilisateur(idUtilisateur, idQuestion);
            return ResponseEntity.ok(question);
        } else
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @GetMapping("/questions")
    public ResponseEntity<Collection<Question>> getQuestion(@RequestParam Optional<String> filtre) {
        String f = filtre.orElse("sansFiltre");
        if ("sansReponse".equals(f)) {
            return ResponseEntity.ok(facadeApplication.getQuestionsSansReponses());
        } else {
            return ResponseEntity.ok(facadeApplication.getToutesLesQuestions());
        }
    }

}



