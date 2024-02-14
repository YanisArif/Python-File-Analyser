package pjava;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class GUI extends JFrame {
    private JTextArea textArea;
    private JButton btnChoixRepertoire, btnChoixFichier;
    private JButton btnListerFichiersPython, btnStatsRepertoire;
    private JButton btnVerifAnnotations, btnVerifPydoc, btnVerifPremieresLignes, btnCorrectionPydoc, btnCorrectionPremieresLignes;
    private boolean listerFichiersPythonClicked = false;
    private boolean statsRepertoireClicked = false;
    private boolean verifAnnotationsClicked = false;
    private boolean verifPydocClicked = false;
    private boolean verifPremieresLignesClicked = false;
    private boolean correctionPydocClicked = false;
    private boolean correctionPremieresLignesClicked = false;

    public GUI(String title) {
        super(title);
        initUI();
    }

    private void initUI() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);

        textArea = new JTextArea();
        textArea.setEditable(false);
        add(new JScrollPane(textArea), BorderLayout.CENTER);

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnChoixRepertoire = new JButton("Choisir répertoire");
        btnChoixFichier = new JButton("Choisir fichier");
        topPanel.add(btnChoixRepertoire);
        topPanel.add(btnChoixFichier);
        add(topPanel, BorderLayout.NORTH);

        // Initialiser et cacher les boutons, ils seront affichés en fonction des choix de l'utilisateur
        btnListerFichiersPython = new JButton("Lister fichiers Python");
        btnStatsRepertoire = new JButton("Statistiques du répertoire");
        btnVerifAnnotations = new JButton("Vérification annotation de type");
        btnVerifPydoc = new JButton("Vérif pydoc");
        btnVerifPremieresLignes = new JButton("Vérif 2 premières lignes de com");
        btnCorrectionPydoc = new JButton("Correction pydoc");
        btnCorrectionPremieresLignes = new JButton("Correction 2 premières lignes");

        JPanel leftPanel = new JPanel(new GridLayout(7, 1));
        leftPanel.add(btnListerFichiersPython);
        leftPanel.add(btnStatsRepertoire);
        leftPanel.add(btnVerifAnnotations);
        leftPanel.add(btnVerifPydoc);
        leftPanel.add(btnVerifPremieresLignes);
        leftPanel.add(btnCorrectionPydoc);
        leftPanel.add(btnCorrectionPremieresLignes);
        add(leftPanel, BorderLayout.WEST);

        // Par défaut, cacher tous les boutons sauf les deux principaux
        hideAllButtons();
        
        // Méthodes pour gérer les clics sur les boutons
        btnVerifAnnotations.addActionListener(e -> verifAnnotationsClicked = true);
        btnVerifPydoc.addActionListener(e -> verifPydocClicked = true);
        btnVerifPremieresLignes.addActionListener(e -> verifPremieresLignesClicked = true);
        btnCorrectionPydoc.addActionListener(e -> correctionPydocClicked = true);
        btnCorrectionPremieresLignes.addActionListener(e -> correctionPremieresLignesClicked = true);
        btnListerFichiersPython.addActionListener(e -> listerFichiersPythonClicked = true);
        btnStatsRepertoire.addActionListener(e -> statsRepertoireClicked = true);
        btnChoixRepertoire.addActionListener(e -> handleChoixRepertoire());
        btnChoixFichier.addActionListener(e -> handleChoixFichier());
        
        setVisible(true);
    }

    private void hideAllButtons() {
        btnListerFichiersPython.setVisible(false);
        btnStatsRepertoire.setVisible(false);
        btnVerifAnnotations.setVisible(false);
        btnVerifPydoc.setVisible(false);
        btnVerifPremieresLignes.setVisible(false);
        btnCorrectionPydoc.setVisible(false);
        btnCorrectionPremieresLignes.setVisible(false);
    }

    private void handleChoixRepertoire() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int returnVal = chooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File directory = chooser.getSelectedFile();
            textArea.setText("");
            hideAllButtons();
            btnListerFichiersPython.setVisible(true);
            btnStatsRepertoire.setVisible(true);

            btnListerFichiersPython.addActionListener(e -> {
                listerFichiersPythonClicked = true;
                performDirectoryAction(directory);
            });
            btnStatsRepertoire.addActionListener(e -> {
                statsRepertoireClicked = true;
                performDirectoryAction(directory);
            });
        }
    }

    private void performDirectoryAction(File directory) {
        Repertoire monRepertoire = new Repertoire();
        try {
            monRepertoire.rempFichier(directory.getAbsolutePath());
            if (listerFichiersPythonClicked) {
                ArrayList<FichierPy> fichiersPython = monRepertoire.listerFichierPy();
                StringBuilder sb = new StringBuilder();
                for (FichierPy fichierPy : fichiersPython) {
                    sb.append(fichierPy.getNom()).append("\n");
                }
                textArea.setText(sb.toString());
                listerFichiersPythonClicked = false;
            } else if (statsRepertoireClicked) {
                String res = "";
                res += "- nombre de fichiers analysés : " + monRepertoire.fichiersTotal() + "\n";
                res += "- nombre de fonctions : " + monRepertoire.nbFonctionTotal() + "\n";
                res += "- pourcentage des fichiers avec shebang python et enodage UTF-8 : " + monRepertoire.pourcentageComDeuxPremLigne() + "%\n";
                res += "- pourcentage des fonctions ayant des annotations de typage : " + monRepertoire.pourcentageAnnotationType() + "%\n";
                res += "- pourcentage fonctions avec commentaires pydoc : " + monRepertoire.pourcentagePyDoc() + "%\n";
                textArea.setText(res);
                statsRepertoireClicked = false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            textArea.setText("Erreur lors du remplissage du répertoire: " + e.getMessage());
        }
    }

    private void handleChoixFichier() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int returnVal = chooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            textArea.setText("");
            hideAllButtons();

            // Rendre les boutons visibles
            btnVerifAnnotations.setVisible(true);
            btnVerifPydoc.setVisible(true);
            btnVerifPremieresLignes.setVisible(true);
            btnCorrectionPydoc.setVisible(true);
            btnCorrectionPremieresLignes.setVisible(true);

            // Attacher les écouteurs d'événements aux boutons
            btnVerifAnnotations.addActionListener(e -> {
                verifAnnotationsClicked = true;
                performFileAction(file);
            });
            btnVerifPydoc.addActionListener(e -> {
                verifPydocClicked = true;
                performFileAction(file);
            });
            btnVerifPremieresLignes.addActionListener(e -> {
                verifPremieresLignesClicked = true;
                performFileAction(file);
            });
            btnCorrectionPydoc.addActionListener(e -> {
                correctionPydocClicked = true;
                performFileAction(file);
            });
            btnCorrectionPremieresLignes.addActionListener(e -> {
                correctionPremieresLignesClicked = true;
                performFileAction(file);
            });
        }
    }

    private void performFileAction(File file) {
        try {
            FichierPy monFichierPy = new FichierPy(file);

            if (verifAnnotationsClicked) {
                double annotationPercentage = monFichierPy.prcFonctionAnnotationType();
                textArea.setText("Pourcentage de fonctions avec annotations de type: " + annotationPercentage + "%");
                verifAnnotationsClicked = false;
            }
            if (verifPydocClicked) {
                double nbPyDoc = monFichierPy.nbFonctionPyDoc();
                textArea.setText("Il y a : " + nbPyDoc + " sur "+ monFichierPy.nbFonctions()+" fonctions avec les commentaires pydoc ");
                verifPydocClicked = false;
            }
            if (verifPremieresLignesClicked) {
                boolean hasHeader = monFichierPy.verifierPremieresLignes();
                if (hasHeader) {
                    textArea.setText("Le fichier contient les deux premières lignes de commentaire.");
                } else {
                    textArea.setText("Le fichier ne contient pas les deux premières lignes de commentaire.");
                }
                verifPremieresLignesClicked = false;
            }
            
            if (correctionPydocClicked) {
                if (monFichierPy.nbFonctionPyDoc() < monFichierPy.nbFonctions()) {
                    monFichierPy.ajouterPyDocSiManquant();
                    textArea.setText("Correction PyDoc effectuée.");
                }else {
                    textArea.setText("Le PyDoc est déjà présent dans toutes les fonctions.");
                }
                correctionPydocClicked = false;
            }

            if (correctionPremieresLignesClicked) {
                if (!monFichierPy.verifierPremieresLignes()) {
                    monFichierPy.ajouterDeuxPremieresLignes();
                    textArea.setText("Correction des deux premières lignes effectuée.");
                } else {
                    textArea.setText("Les deux premières lignes sont déjà présentes dans le fichier.");
                }
                correctionPremieresLignesClicked = false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            textArea.setText("Erreur lors de la manipulation du fichier: " + e.getMessage());
        }
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GUI("Analyseur de Fichiers Python"));
    }
}
