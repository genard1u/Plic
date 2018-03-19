package yal.arbre;

import yal.analyse.tds.TDS;

public class Programme extends ArbreAbstrait {

	private String nom;
	private BlocDInstructions fonctions;
	private BlocDInstructions instructions;
	private int tailleZoneDesVariables;
	
	
	public Programme(BlocDInstructions li, String p, int noLigne) {
		super(noLigne);
		fonctions = new BlocDInstructions(noLigne + 1);
		instructions = li;
		nom = p;
	}

	public Programme(BlocDInstructions ld, BlocDInstructions li, String p, int noLigne) {
		super(noLigne);
		fonctions = ld;
		instructions = li;
		nom = p;
	}
	
	public void base(StringBuilder mips) {
		mips.append("# Initialisation de la base des variables\n");
        mips.append("move $s7, $sp\n");
        mips.append("\n");
        
        mips.append("# Réservation de l'espace pour ");
        mips.append(tailleZoneDesVariables / 4);
        mips.append(" variable(s)");
        mips.append("\n");
        
        mips.append("addi $sp, $sp, ");
        mips.append(- tailleZoneDesVariables);
        mips.append("\n\n");
        
        mips.append("# Initialisations\n");
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
        mips.append("vrai :\t");
        mips.append(".asciiz \"vrai\"\n");
        mips.append("faux :\t");
        mips.append(".asciiz \"faux\"\n");
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
    }
	
	public void instructions(StringBuilder mips) {
		mips.append(instructions.toMIPS());	
	}
	
	public void fonctions(StringBuilder mips) {
		mips.append(fonctions.toMIPS());
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
		instructions(mips);	
		fonctions(mips);
		fin(mips);
		
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
