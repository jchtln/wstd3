package univ.orleans.ws3.modele;

import org.springframework.stereotype.Component;
import univ.orleans.ws3.modele.exception.LoginDejaUtiliseException;
import univ.orleans.ws3.modele.exception.UtilisateurInexistantException;

import java.util.HashMap;
import java.util.Map;

@Component
public class FacadeUtilisateurs {
    /**
     * Permet de stocker l'ensemble des utilisateurs inscrits au service
     */



    private Map<String,Utilisateur> utilisateursMap;

    public FacadeUtilisateurs() {
        utilisateursMap = new HashMap<>();
    }

    /**
     * Permet de récupérer l'identifiant Integer à partir du login (email)
     * @param login
     * @return
     * @throws UtilisateurInexistantException
     */
    public int getUtilisateurIntId(String login) throws UtilisateurInexistantException {
        if (utilisateursMap.containsKey(login))
            return this.utilisateursMap.get(login).getIdUtilisateur();
        else
            throw new UtilisateurInexistantException();
    }

    /**
     * Permet de récupérer un Utilisateur à partir de son login
     * @param login
     * @return
     */

    public Utilisateur getUtilisateurByLogin(String login) {
        return utilisateursMap.get(login);
    }


    /**
     * Permet d'inscrire un nouvel utilisateur à la plate-forme
     * @param login
     * @param mdp
     * @return son identifiant Integer
     * @throws LoginDejaUtiliseException
     */
    public int inscrireUtilisateur(String login, String mdp) throws LoginDejaUtiliseException {
        if (utilisateursMap.containsKey(login))
            throw new LoginDejaUtiliseException();
        else {
            Utilisateur utilisateur = new Utilisateur(login,mdp);
            utilisateursMap.put(utilisateur.getLogin(),utilisateur);
            return utilisateur.getIdUtilisateur();
        }
    }


    /**
     * Permet de vérifier si le mot de passe est correct (useless
     * dans la version finale)
     * @param login
     * @param motDePasse
     * @return
     */
    public boolean verifierMotDePasse(String login, String motDePasse){
        if (utilisateursMap.containsKey(login)){
            return utilisateursMap.get(login).verifierMotDePasse(motDePasse);
        }
        else
            return false;
    }

}