package yal.arbre.instruction;

import yal.analyse.tds.TDS;
import yal.analyse.tds.entree.EntreeFonction;
import yal.analyse.tds.symbole.SymboleFonction;
import yal.arbre.BlocDInstructions;
import yal.exceptions.AnalyseSemantiqueException;

public class Fonction extends Instruction {
	
	private String idf;
	private String etiquette;
	
	private int numeroRegion;
	private int nombreParametres;
	private int espaceVariables;
	
	private BlocDInstructions instructions;

	
	public Fonction(BlocDInstructions li, String identifiant, int parametres, int noLigne) {
		super(noLigne);
		idf = identifiant;
		nombreParametres = parametres;
		instructions = li;
		numeroRegion = TDS.getInstance().numeroRegion();
		espaceVariables = TDS.getInstance().tailleZoneDesVariables();
	}

	@Override
	public boolean estRetourne() {
		assert instructions.nombreInstructions() > 0;
		
		return instructions.estRetourne();
	}
	
	@Override
	public void verifier() {
		EntreeFonction e = new EntreeFonction(idf, nombreParametres);
		SymboleFonction s = (SymboleFonction) TDS.getInstance().identifier(e);
		
		if (s == null) {
			throw new AnalyseSemantiqueException(getNoLigne(), "aucune déclaration de `" + idf + "()`");
		}
		
		etiquette = s.etiquette();
		
		TDS.getInstance().entreeBloc();		
		
		instructions.verifier();
		
		if (!estRetourne()) {
			throw new AnalyseSemantiqueException(getNoLigne(), "retourne peut ne pas être atteint dans `" + idf + "()`");
		}
		
		TDS.getInstance().sortieBloc();
	}
	
	public void empileAdresseRetour(StringBuilder fonction) {
		fonction.append("# Empilement de l'adresse retour\n");
		fonction.append("sw $ra, 0($sp)\n");
		fonction.append("add $sp, $sp, -4\n");
		fonction.append("\n");
	}
	
	public void empileChainageDynamique(StringBuilder fonction) {
		fonction.append("# Empilement du chainage dynamique\n");
		fonction.append("sw $s7, 0($sp)\n");
		fonction.append("add $sp, $sp, -4\n");
		fonction.append("\n");
	}
	
	public void empileNumeroRegion(StringBuilder fonction) {
		fonction.append("# Empilement du numero de region\n"); 
		fonction.append("li $t8, " + numeroRegion + "\n");
		fonction.append("sw $t8, 0($sp)\n");
		fonction.append("add $sp, $sp, -4\n");
		fonction.append("\n");
	}
	
	public void allocationVariables(StringBuilder fonction) {
		fonction.append("# Allocation de la place des variables\n");
		fonction.append("add $sp, $sp, -" + espaceVariables + "\n");
		fonction.append("\n");
	}
	
	public void deplacementBase(StringBuilder fonction) {
		fonction.append("# Déplacement de la base\n");
		fonction.append("move $s7, $sp\n");
		fonction.append("\n");
	}
	
	public void instructions(StringBuilder fonction) {
		fonction.append("# Instruction de la fonction\n");
		fonction.append(instructions.toMIPS());
		fonction.append("\n");
	}
	
	@Override
	public String toMIPS() {
		StringBuilder fonction = new StringBuilder(100);
		
		fonction.append("# Fonction\n");
		fonction.append(etiquette + " :\n");
		
		empileAdresseRetour(fonction);
		empileChainageDynamique(fonction);
		empileNumeroRegion(fonction);
        deplacementBase(fonction);
		allocationVariables(fonction);
		instructions(fonction);
		
		return fonction.toString();
	}

	@Override
	public String toString() {
		StringBuilder fonction = new StringBuilder(40);
		
		fonction.append("fonction");
		fonction.append(" ");
		fonction.append(idf);
		fonction.append("(");		
		fonction.append(")");
		fonction.append(" ");
		fonction.append("debut");
		fonction.append("\n");
		fonction.append(instructions.toString());
		fonction.append("fin");
		fonction.append("\n");
		
		return fonction.toString();
	}
	
}
