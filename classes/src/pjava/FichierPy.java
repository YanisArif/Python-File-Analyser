package pjava;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class FichierPy {
	
    private String nom;
    private File fichierpy;
    private ArrayList<Fonction> fonctions ; 

    public FichierPy(File fichier) throws IOException {
        this.fichierpy = fichier;
        this.nom = fichier.getName();
        this.fonctions = new ArrayList<>(); 
        remplissageFonctions(fichier);
    }

    
    
    private void remplissageFonctions(File fichier) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(fichier));
        String ligne;
        while ((ligne = reader.readLine()) != null) {
            if (ligne.trim().startsWith("def")) {
                String[] def = ligne.trim().split("\\s+");
                String[] nomFonction = def[1].split("\\(");
                StringBuilder contenu = new StringBuilder(ligne + "\n");

                while ((ligne = reader.readLine()) != null && (ligne.startsWith(" ") || ligne.startsWith("\t"))) {
                    contenu.append(ligne).append("\n");
                }

                Fonction fonctionTemp = new Fonction(nomFonction[0], contenu.toString());
                fonctions.add(fonctionTemp);

                if (ligne == null) {
                    break;
                }
            }
        }
        reader.close();
    }
	
    public void ajouterPyDocSiManquant() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(fichierpy));
        ArrayList<String> toutesLesLignes = new ArrayList<>();
        String ligne;
        boolean pyDocManquant = false;

        while ((ligne = reader.readLine()) != null) {
            if (ligne.trim().startsWith("def")) {
                String[] defSplit = ligne.split("\\s+|\\(");
                String nomDeLaFonction = defSplit.length > 1 ? defSplit[1] : "";
                toutesLesLignes.add(ligne);
                String[] params = ligne.substring(ligne.indexOf('(') + 1, ligne.indexOf(')')).split(",");
                
                boolean fonctionPyDocTrouve = false;
                while ((ligne = reader.readLine()) != null) {
                    if (ligne.trim().startsWith("\"\"\"")) {
                        fonctionPyDocTrouve = true;
                        toutesLesLignes.add(ligne);
                        break;
                    } else if (!ligne.trim().isEmpty() && !ligne.trim().startsWith("#")) {
                        // PyDoc manquant, ajouter PyDoc
                        pyDocManquant = true;
                        toutesLesLignes.add("    \"\"\"");
                        toutesLesLignes.add("    @version 0.1");
                        toutesLesLignes.add("    @author ML");
                        for (String param : params) {
                            if (!param.trim().isEmpty()) {
                                String paramName = param.trim().contains(" ") ? param.trim().split(" ")[0] : param.trim();
                                toutesLesLignes.add("    @param " + paramName);
                            }
                        }
                        toutesLesLignes.add("    @return " + nomDeLaFonction);
                        toutesLesLignes.add("    \"\"\"");
                        break;
                    } else {
                        toutesLesLignes.add(ligne);
                    }
                }
                if (!fonctionPyDocTrouve && ligne != null) {
                    toutesLesLignes.add(ligne);
                }
            } else {
                toutesLesLignes.add(ligne);
            }
        }
        reader.close();

        if (pyDocManquant) {
            BufferedWriter writer = new BufferedWriter(new FileWriter(fichierpy));
            for (String uneLigne : toutesLesLignes) {
                writer.write(uneLigne);
                writer.newLine();
            }
            writer.close();
        }
    }


    
    
 
    
    public void ajouterDeuxPremieresLignes() throws IOException {
        BufferedReader verif = new BufferedReader(new FileReader(fichierpy));
        StringBuilder contenu = new StringBuilder();
        String ligne;

        while ((ligne = verif.readLine()) != null) {
            if(!ligne.startsWith("#!") && !ligne.endsWith("python3")) {
                if(!ligne.startsWith("#--") && !ligne.endsWith("--"))
                contenu.append(ligne).append("\n");

            }

        }

        verif.close();

        boolean shebang = contenu.toString().contains("#! " + fichierpy.getParentFile() + " python3");
        boolean utf8 = contenu.toString().contains("#-- utf-8 --");

        if (!shebang || !utf8) {
            BufferedWriter ecrire = new BufferedWriter(new FileWriter(fichierpy));

            ecrire.write("#! " + fichierpy.getParentFile() + " python3" + "\n");
            ecrire.write("#-- utf-8 --" + "\n");

            ecrire.write(contenu.toString());
            ecrire.close();
        }
    }
    
    
    
    
    
    
    public boolean verifierPremieresLignes() throws IOException {
    		BufferedReader verif = new BufferedReader(new FileReader(fichierpy)) ;
            String premiereLigne = null;
            String deuxiemeLigne = null ;

            while ((premiereLigne = verif.readLine()) != null && premiereLigne.trim().isEmpty());
            if (premiereLigne != null) {
                deuxiemeLigne = verif.readLine();
                while (deuxiemeLigne != null && deuxiemeLigne.trim().isEmpty()) {
                    deuxiemeLigne = verif.readLine();
                }
            }

      
            if( premiereLigne != null && deuxiemeLigne != null && premiereLigne.trim().startsWith("#") && deuxiemeLigne.trim().startsWith("#"))
            {
            	return true ;
            }else {
            	return false ;
            }
    
    }
    
    
   
    public int nbFonctions() {
    	return fonctions.size() ;
    }
    
    public int prcFonctionAnnotationType() {
    	if (nbFonctions()==0) {
    		return 0 ;
    	}else {
    	return (int) ((nbFonctionAnnotationType()*100)/nbFonctions());
    	}
    }
    
    
    public int nbFonctionAnnotationType() {
    	int somme = 0 ;
    	for (Fonction f : fonctions) {
    		if (f.getStats().getAnnotType()) {
    			somme++;
    		}
    	}
    	if (nbFonctions()==0) {
    		return 0 ;
    	}else {
    	return somme ;
    	}
    }
    
    
    public int prcFonctionPyDoc() {
   
    	if (nbFonctions()==0) {
    		return 0 ;
    	}else {
    	return (int) ((nbFonctionPyDoc()*100)/nbFonctions());
    	}
    }
    
    
    public double nbFonctionPyDoc() {
    	int somme = 0 ;
    	for (Fonction f : fonctions) {
    		if(f.getStats().getPyDoc()) {
    			somme++ ;
    		}
    	}
    	if (nbFonctions()==0) {
    		return 0 ;
    	}else {
    	}
    		return somme ;
    	}
    
    
    
    
    public String contenuFonction(){
    	String c = "";
    	for (Fonction f : fonctions) {
    		c += f.getContenu();
    	}
    	return c ;
    }
    

    public ArrayList<Fonction> getFonctions(){
    	return fonctions ;
    }
    
    public String getNom() {
        return nom;
    }

    
    
    public File getFichierpy() {
        return fichierpy;
    }
    


}



