package univ.orleans.ws3.modele;

public class Utilisateur {

    private String login;
    private String motDePasse;
    private int idUtilisateur;
    private static int IDS=0;
    private boolean isEtudiant;


    public Utilisateur(String login, String motDePasse, boolean isEtudiant) {
        this.login = login;
        this.motDePasse = motDePasse;
        this.isEtudiant = isEtudiant;
        this.idUtilisateur = IDS++;
    }

    public String getLogin() {
        return login;
    }

    public int getIdUtilisateur() {
        return idUtilisateur;
    }


    public boolean verifierMotDePasse(String motDePasse){
        return this.motDePasse.equals(motDePasse);
    }

    public String getMotDePasse() {
        return this.motDePasse;
    }

}