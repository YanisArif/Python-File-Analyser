package pjava;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.IOException;

public class Fonction {
	private String nom;
	private Statistique stats;
	private String contenu ; 
	
	
    public Fonction(String nom, String contenu) {
        this.nom = nom;
        this.contenu = contenu;
        this.stats = new Statistique(contenu); 
    }
	
	
	public String getNom() {
		return nom;
	}

	
	public Statistique getStats() {
		return stats ;
	}
	
	
	public void setNom(String nom) {
		this.nom = nom;
	}

	


	public String getContenu() {
		return contenu;
	}
	

}

