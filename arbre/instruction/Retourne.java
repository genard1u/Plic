package yal.arbre.instruction;

import yal.analyse.tds.TDS;
import yal.arbre.expression.Expression;
import yal.exceptions.AnalyseSemantiqueException;

public class Retourne extends Instruction {

	private Expression exp;
	private int numeroRegion;
	
	
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
		
		if (!exp.getType().equals("entier")) {
			StringBuilder erreur = new StringBuilder(30);
			
			erreur.append("erreur de type :\t");
			erreur.append("retourne ");
			erreur.append(exp);
			erreur.append("\n\t");
			erreur.append("une fonction doit retourner un entier");
			
			throw new AnalyseSemantiqueException(getNoLigne(), erreur.toString());
		}
		
		numeroRegion = TDS.getInstance().numeroRegion();
	}

	public void depileEspaceVariables(StringBuilder retour) {
		/* retour.append("# Dépile l'espace alloué aux variables\n");
		retour.append("add $sp, $sp, " + espaceVariables + "\n"); 
		retour.append("\n"); */
		retour.append("# Dépile l'espace alloué aux variables\n");
		retour.append("move $sp, $s7\n");
	}
	
	public void depileNumeroRegion(StringBuilder retour) {
		retour.append("# Dépile le numéro de région\n");
		retour.append("add $sp, $sp, 4\n");
		retour.append("\n");
	}
	
	public void depileChainageDynamique(StringBuilder retour) {
		retour.append("# Dépile le chaînage dynamique\n");
		retour.append("add $sp, $sp, 4\n");
		retour.append("\n");
	}
	
	public void depileAdresseRetour(StringBuilder retour) {
		retour.append("# Dépile l'adresse de retour\n");
		retour.append("add $sp, $sp, 4\n");
		retour.append("lw $ra, 0($sp)\n");
		retour.append("\n");
	}
	
	public void deplacementBase(StringBuilder retour) {
		retour.append("lw $s7, 8($sp)\n");	
		retour.append("\n");
	}
	
	@Override
	public String toMIPS() {
		StringBuilder retour = new StringBuilder(50);
		
		retour.append("# Retourne\n");
		retour.append("# Met la valeur de l'expression dans $v0\n");
		retour.append(exp.toMIPS());
        retour.append("\n");
        
		if (numeroRegion > 0) {
			depileEspaceVariables(retour);
			deplacementBase(retour);
			depileNumeroRegion(retour);
			depileChainageDynamique(retour);
			depileAdresseRetour(retour);
			
			retour.append("# Enregistre la valeur de $v0\n");
			retour.append("sw $v0, 4($sp)\n");
			retour.append("\n");
			
			retour.append("# Jump\n");
			retour.append("jr $ra\n");		 
			retour.append("\n");
		}
		else {
			retour.append("# Saut vers la fin\n");
			retour.append("j fin\n");
		}
		
		return retour.toString();
	}	
	
	@Override
	public String toString() {
		return "retourne " + exp + " ;";
	}
	
}
