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
		retour.append("");

		 //sp <- sp+nbVariable+n°region+chainagedynamique
		retour.append("add $sp, $sp, "+TDS.getInstance().nbVariables()+"\n");
		retour.append("add $sp, $sp, "+TDS.getInstance().numeroRegion()+"\n");
		retour.append("add $sp, $sp, "+TDS.getInstance().numeroParent()+"\n");
		 
		retour.append("# Dépilement de la base\n");
		retour.append("add $sp, $sp, 4\n");
		 //retour.append("lw $t8, 0($sp)\n");
		retour.append("\n");
		 
		retour.append("lw $ra, 0($sp)\n");
		retour.append("jr $ra+\n");
		 

		return retour.toString();
	}	
	
}
