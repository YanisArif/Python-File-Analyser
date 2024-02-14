package pjava;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.io.IOException;
public class Repertoire {
    private ArrayList<Fichier> fichiers;
    private ArrayList<FichierPy> fichiersPython;

    public Repertoire() {
        fichiers = new ArrayList<>();
        fichiersPython = new ArrayList<>();
    }

    
    
    
    
    
    public void rempFichier(String chemin) throws IOException {
        parcourirRepertoire(new File(chemin));
    }
    private void parcourirRepertoire(File repertoire) throws IOException {
        if (repertoire.isDirectory()) 
        {
            File[] liste = repertoire.listFiles();

            	if (liste != null) {
                for (File fichier : liste) {
                    if (fichier.isDirectory()) {
                        parcourirRepertoire(fichier);
                    } else if (fichier.isFile()) {
                        if (fichier.getName().endsWith(".py")) {
                            fichiersPython.add(new FichierPy(fichier));
                        } else {
                            fichiers.add(new Fichier(fichier));
                        }
                    }
                }
            }
        }
    }
    
    
    public String testContenu() throws IOException {
    	for(FichierPy code : fichiersPython) {
    		if (code.verifierPremieresLignes()) {
    			return code.contenuFonction();
    		}
    	}
		return null ;
    }
    
    
    
    public double pourcentageComDeuxPremLigne () throws IOException {
    	int somme = 0 ;
 
    	for (FichierPy code : fichiersPython ) {
    		if (code.verifierPremieresLignes()) {
    			somme++ ;
    		}
    	}  
     	return (somme*100)/ fichiersTotal() ; 
    }

    
    public int nbFonctionTotal() {
    	int total = 0 ;
    	for (FichierPy code : fichiersPython ){
    		total += code.nbFonctions();
    	}
    	return total ; 
    }
    
    public int pourcentageAnnotationType() {
    	double somme= 0.0 ; 
    	for (FichierPy code : fichiersPython) {
    		somme += code.prcFonctionAnnotationType();
    	}
    	return (int) somme/fichiersTotal();
    }
    
    public int pourcentagePyDoc() {
    	double somme= 0.0 ;
    	for (FichierPy code : fichiersPython) {
    		somme += code.prcFonctionPyDoc();
    	}
    	return (int) somme/fichiersTotal();
    }
    
    
    public ArrayList<FichierPy> listerFichierPy() {
        return fichiersPython;
    }
     
    
    public int fichiersTotal () {
    	return fichiersPython.size() + fichiers.size();
    }
    
 
    
}


