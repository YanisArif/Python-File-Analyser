package pjava;

import java.io.BufferedReader;
import java.util.Scanner;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Statistique {
    private boolean pyDoc ; 
    private boolean annotType ;


    public Statistique(String contenu) {
        contientPydoc(contenu); 
        contientAnnotationsType(contenu);
    }
  
    
    private void contientPydoc(String contenu) {
        Scanner scanner = new Scanner(contenu);
        String ligne = scanner.nextLine(); 
        while (scanner.hasNextLine()) {
        	if (scanner.hasNextLine()) {
        		ligne = scanner.nextLine().trim(); 
        		if (ligne.startsWith("\"\"\"")) {
        			pyDoc = true;
        			break ; 
        		}
            }
        }
        scanner.close(); 
    }

    
    
    private void contientAnnotationsType(String contenu) {
        Scanner scanner = new Scanner(contenu);
        while (scanner.hasNextLine()) {
            String ligne = scanner.nextLine();
            if (ligne.trim().startsWith("def") && (ligne.contains(":") && ligne.contains("->"))) {
                annotType = true;
                break;
            }
        }
        scanner.close();
    }
  
 


	public boolean getPyDoc() {
		return pyDoc;
	}




	public boolean getAnnotType() {
		return annotType;
	}


}

