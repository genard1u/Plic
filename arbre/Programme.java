package yal.arbre;

import yal.analyse.tds.TDS;
import yal.arbre.instruction.Instruction;

public class Programme extends ArbreAbstrait {

	private String nom;
	
	private BlocDInstructions fonctions;
	private BlocDInstructions instructions;
	private BlocDInstructions tableaux;
	
	private int tailleZoneDesVariables;
	
	
	public Programme(BlocDInstructions li, String p, int noLigne) {
		super(noLigne);
		fonctions = new BlocDInstructions(noLigne + 1);
		tableaux = new BlocDInstructions(noLigne + 1);
		instructions = li;
		nom = p;
	}

	public Programme(BlocDInstructions ld, BlocDInstructions li, String p, int noLigne) {
		super(noLigne);
		fonctions = new BlocDInstructions(noLigne + 1);
		tableaux = new BlocDInstructions(noLigne + 1);
		
		for (int i = 0; i < ld.nombreInstructions(); i ++) {
			Instruction declaration = ld.recuperer(i);
			
			if (declaration.estDeclarationTableau()) {
				tableaux.ajouter(declaration);
			}
			else {
				fonctions.ajouter(declaration);
			}
		}
		
		instructions = li;
		nom = p;
	}
	
	public void base(StringBuilder mips) {
		mips.append("# Empile le numéro de région\n");
		mips.append("li $t8, 0\n");
		mips.append("sw $t8, 0($sp)\n");
		mips.append("addi $sp, $sp, -4\n");
		mips.append("\n");
		
		mips.append("# Initialisation de la base des variables\n");
        mips.append("move $s7, $sp\n");
        mips.append("\n");
        
        mips.append("# Réservation de l'espace pour ");
        mips.append(tailleZoneDesVariables / 4);
        mips.append(" variable(s)\n");
        
        mips.append("addi $sp, $sp, ");
        mips.append(- tailleZoneDesVariables);
        mips.append("\n\n");
        
        mips.append("# Initialisation des variables\n");
        mips.append("li $t8, 0\n");
        
        for (int depl = 0; depl > - tailleZoneDesVariables; depl -= 4) {
        	mips.append("sw $t8, ");
        	mips.append(depl);
        	mips.append("($s7)\n");
        }
        
        mips.append("\n");
	}
	
	public void data(StringBuilder mips) {
    	mips.append(".data\n");
    	
        mips.append("err_div :\t");
        mips.append(".asciiz \"ERREUR EXECUTION :\\n\\t division par zéro\"\n");
        mips.append("\n");
        
        mips.append("vrai :\t");
        mips.append(".asciiz \"vrai\"\n");
        mips.append("\n");
        
        mips.append("faux :\t");
        mips.append(".asciiz \"faux\"\n");
        mips.append("\n");
        
        mips.append("longueurInvalide :\t");
        mips.append(".asciiz \"déclaration de tableau avec une longueur négative ou nulle\"\n");
        mips.append("\n");
        
        mips.append("accesTableauInvalide :\t");
        mips.append(".asciiz \"accès à un tableau avec un indice qui n'est pas dans les bornes\"\n");
        mips.append("\n");
    }
	
	public void fin(StringBuilder mips) {
    	mips.append("end :\n");
        mips.append("# fin du programme\n");
        mips.append("move $v1, $v0\t");
        mips.append("# copie de $v0 dans $v1 pour permettre les tests de yal0\n");
        mips.append("li $v0, 10\t");
        mips.append("# retour au système\n");
        mips.append("syscall\n");
        mips.append("\n");
        
        mips.append("erreurLongueurInvalide :\n");
        mips.append("li $v0, 4\n");
        mips.append("la $a0, longueurInvalide\n");
        mips.append("syscall\n");
        mips.append("j end\n");
        mips.append("\n");
        
        mips.append("erreurAccesTableauInvalide :\n");
        mips.append("li $v0, 4\n");
        mips.append("la $a0, accesTableauInvalide\n");
        mips.append("syscall\n");
        mips.append("j end\n");
        mips.append("\n");
    }
	
	public void instructions(StringBuilder mips) {
		mips.append(instructions.toMIPS());	
	}
	
	public void fonctions(StringBuilder mips) {
		mips.append(fonctions.toMIPS());
	}
	
	public void tableaux(StringBuilder mips) {
		mips.append(tableaux.toMIPS());
	}
	
	public void main(StringBuilder mips) {
		mips.append(".text");
		mips.append("\n");
		mips.append("main :");
		mips.append("\n\n");
	}
	
	@Override
	public void verifier() {
		TDS.getInstance().entreeBloc();
		tailleZoneDesVariables = TDS.getInstance().tailleZoneDesVariables();
		tableaux.verifier();
		fonctions.verifier();
		instructions.verifier();
		TDS.getInstance().sortieBloc();
	}

	@Override
	public String toMIPS() {
		StringBuilder mips = new StringBuilder(200);
		
		data(mips);		
		main(mips);
		base(mips);
		tableaux(mips);
		instructions(mips);	
		fin(mips);
		fonctions(mips);
		
		return mips.toString();
	}

	@Override
	public String toString() {
		StringBuilder yal = new StringBuilder(30);
		
		yal.append("Programme ");
		yal.append(nom);
		yal.append("\n");
		yal.append(instructions.toString());
		
		return yal.toString();
	}

}
