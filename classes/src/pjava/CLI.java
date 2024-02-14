package pjava;

import java.io.File;
import java.io.IOException;

public class CLI {

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Erreur : Aucun argument fourni. Utilisez -h ou --help pour obtenir de l'aide.");
            return;
        }

        // Option d'aide
        if (args[0].equals("-h") || args[0].equals("--help")) {
            afficherAide();
            return;
        }

        try {
            // Analyser et agir en fonction des arguments de ligne de commande
            switch (args[0]) {
                case "-d":
                    // Traitement pour un répertoire
                    if (args.length > 1 && args[2].equals("-stat")) {
                        Repertoire monRepertoire = new Repertoire();
                        monRepertoire.rempFichier(args[1]);
                        afficherStatistiques(monRepertoire);
                    } else {
                        System.out.println("Erreur : Répertoire non spécifié.");
                    }
                    break;
                case "-f":
                    // Traitement pour un fichier spécifique
                    if (args.length > 1) {
                        traiterFichier(args[1], args);
                    } else {
                        System.out.println("Erreur : Fichier non spécifié.");
                    }
                    break;
                default:
                    System.out.println("Erreur : Argument non reconnu. Utilisez -h ou --help pour obtenir de l'aide.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void afficherAide() {
        System.out.println("Utilisation du programme CLI pour l'analyse de fichiers Python :");
        System.out.println("java -jar cli.jar : Affiche ce message d'aide.");
        System.out.println("java -jar cli.jar -h ou --help : Affiche les options d'utilisation.");
        System.out.println("java -jar cli.jar -d [chemin_dossier] : Analyse tous les fichiers .py dans le dossier spécifié.");
        System.out.println("java -jar cli.jar -f [chemin_fichier] --type : Vérifie les annotations de type dans le fichier spécifié.");
        System.out.println("java -jar cli.jar -f [chemin_fichier] --head : Vérifie les deux premières lignes de commentaire dans le fichier spécifié.");
        System.out.println("java -jar cli.jar -f [chemin_fichier] --pydoc : Vérifie les commentaires PyDoc dans le fichier spécifié.");
        System.out.println("java -jar cli.jar -f [chemin_fichier] --type --head --pydoc : Combinaison des trois vérifications précédentes.");
        System.out.println("java -jar cli.jar -f [chemin_fichier] --sbutf8 : Ajoute l'en-tête du fichier si manquant.");
        System.out.println("java -jar cli.jar -f [chemin_fichier] --comment : Ajoute un squelette de commentaire PyDoc sur les fonctions du fichier.");
        System.out.println("java -jar cli.jar -d [chemin_dossier] --stat : Affiche les statistiques qualitatives sur un ensemble de fichiers.");
    }
    
    
    
    private static void afficherStatistiques(Repertoire monRepertoire) throws IOException {
    	System.out.println("- nombre de fichiers analysés : " + monRepertoire.fichiersTotal() );
        
        System.out.println("- nombre de fonctions : " + monRepertoire.nbFonctionTotal());
           
		System.out.println("- pourcentage des fichiers avec shebang python et enodage UTF-8 : " + monRepertoire.pourcentageComDeuxPremLigne());
	
        System.out.println("- pourcentage des fonctions ayant des annotations de typage : " + monRepertoire.pourcentageAnnotationType());
              
        System.out.println("- pourcentage fonctions avec commentaires pydoc : " + monRepertoire.pourcentagePyDoc()) ;

    }

    
    
    
    private static void traiterFichier(String cheminFichier, String[] options) throws IOException {
        FichierPy monFichierPy = new FichierPy(new File(cheminFichier));
        for (int i = 2; i < options.length; i++) {
            switch (options[i]) {
                case "--type":
                	// Vérifier et afficher des informations sur les annotations de type
                    System.out.println("Analyse des annotations de type pour le fichier " + cheminFichier);
                    double typeAnnotations = monFichierPy.prcFonctionAnnotationType();
                    System.out.println("Il y a "+ typeAnnotations + "% de fonctions qui ont une annotation de Type");
                    break;
                case "--pydoc":
                	 // Vérifier et afficher des informations sur les commentaires PyDoc
                    System.out.println("Analyse des commentaires PyDoc pour le fichier " + cheminFichier);
                    double pyDocComments = monFichierPy.prcFonctionPyDoc();
                    System.out.println("Il y a "+ pyDocComments + "% de fonctions avec les commentaires pydoc");
                    break;
                case "--head":
                	System.out.println("Analyse des deux premières lignes pour le fichier " + cheminFichier);
                    boolean validHeader = monFichierPy.verifierPremieresLignes();
                    if (validHeader) {
                    	System.out.println("Le fichier " + monFichierPy.getNom() + " contient les deux premières lignes de COM.");
                    }else {
                    	System.out.println("Le fichier " + monFichierPy.getNom() + " ne contient pas les deux premières lignes de COM.");
                    }
                    break;
                case "--comment" :
                	if (monFichierPy.nbFonctionPyDoc()< monFichierPy.nbFonctions()) {
                		monFichierPy.ajouterPyDocSiManquant();
                		System.out.println("Les fonctions du fichier"+ monFichierPy.getNom()+"ont été corrigés");
                	}else {
                		System.out.println("Erreur : Il n' y a pas de fonctions qui nécessite une correction de pydoc");
                	}
                	break;
                case "--sbutf8" : 
                	if (!monFichierPy.verifierPremieresLignes()) {
                		monFichierPy.ajouterDeuxPremieresLignes();
                		System.out.println("Les 2 premières lignes de commentaires ont été corrigées");
                	}else {
                		System.out.println("Il y a déjà les 2 premières lignes de commentaires");
                	}
            }
        } 
    }

}


