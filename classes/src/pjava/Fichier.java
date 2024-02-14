package pjava;
import java.io.File;

public class Fichier {
    private String nom;
    private File fichier;

    public Fichier(File fichier) {
        this.fichier = fichier;
        this.nom = fichier.getName();
    }

    public String getNom() {
        return nom;
    }

    public File getFichier() {
        return fichier;
    }
}

