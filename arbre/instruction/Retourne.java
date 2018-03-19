package yal.arbre.instruction;

import yal.analyse.tds.TDS;
import yal.arbre.expression.Expression;

public class Retourne extends Instruction {

	private Expression exp;
	
	
	public Retourne(Expression expr) {
		super(expr.getNoLigne());
		exp = expr;
	}

	@Override
	public boolean estRetourne() {
		return true;
	}
	
	@Override
	public void verifier() {
		exp.verifier();
	}

	@Override
	public String toMIPS() {
		StringBuilder retour = new StringBuilder(50);
		// int hash = hashCode();
		
		retour.append("# Retourne\n");

		retour.append(exp.toMIPS() + "\n");

		retour.append("# Nettoyage de la pile\n");
		retour.append("# Nettoyage de l'espace alloué aux variables\n");
		retour.append("add $sp, $sp, "+TDS.getInstance().nbVariables()+"\n");
		retour.append("# Nettoyage de l'espace du numéro de région\n");
		retour.append("add $sp, $sp, "+TDS.getInstance().numeroRegion()+"\n");
		retour.append("# Replacement de $s7 vers la base précédente\n");
		retour.append("add $s7, $sp, 0\n");
		retour.append("# Nettoyage de l'espace du numéro du Chainage dynamique\n");
		retour.append("add $sp, $sp, "+TDS.getInstance().numeroParent()+"\n");
				
		retour.append("# Stockage de l'adresse de retour\n");
		retour.append("lw $ra, 0($sp)\n");
		retour.append("add $sp, $sp, 4\n");
		
		retour.append("# Stockage de la valeur de retour\n");
		retour.append("lw $sp, 0($v0)\n ");		
		
		retour.append("# Jump\n");
		retour.append("jr $ra+\n");		 

		return retour.toString();
	}	
	
}
