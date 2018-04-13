package yal.arbre.instruction;

import yal.analyse.tds.TDS;
import yal.analyse.tds.entree.EntreeVariable;
import yal.analyse.tds.symbole.Symbole;
import yal.arbre.expression.Expression;
import yal.exceptions.AnalyseSemantiqueException;

public class Affectation extends Instruction {
	
	protected Expression exp; 
	
	protected String idf;
	protected String type;
	
	protected int deplVar;
	protected int numeroRegion;
	
	
	public Affectation(String idf, Expression exp) {
		super(exp.getNoLigne());
		this.exp = exp;
		this.idf = idf;
	}
	
	@Override
	public void verifier() {
		EntreeVariable e = new EntreeVariable(idf);
		Symbole s = TDS.getInstance().identifier(e);
		
		if (s == null) {
			throw new AnalyseSemantiqueException(getNoLigne(), "aucune déclaration de `" + idf + "`");
		}
		
		type = s.getType();
		exp.verifier();
		
		if (!type.equals(exp.getType())) {
			StringBuilder erreur = new StringBuilder(40);
			
			erreur.append("erreur de type :\t");
			erreur.append(idf);
			erreur.append(" = ");
			erreur.append(exp.toString());
			erreur.append("\n\t");
	    	erreur.append("les deux opérandes doivent être de même type");
	    	
			throw new AnalyseSemantiqueException(getNoLigne(),erreur.toString());
		}
		
		deplVar = s.getDeplacement();	
		numeroRegion = s.numeroRegion();
	}

	@Override
	public String toMIPS() {
		StringBuilder aff = new StringBuilder(50);
		
		aff.append("# Affectation\n");
		aff.append(exp.toMIPS());
		
		aff.append("# Empile la valeur à mettre dans la variable\n");
		aff.append("sw $v0, 0($sp)\n");
		aff.append("add $sp, $sp, -4\n");
		
		/* Base courante */
		aff.append("# Récupère la base courante\n");
		aff.append("move $t2, $s7\n");
		
		/* Numéro de région où est déclarée la variable */
		aff.append("# Récupère le numéro de région où est déclarée la variable\n");
		aff.append("li $v1, ");
		aff.append(numeroRegion);
		aff.append("\n");
		
		/* Entrée TantQue */
		aff.append("tq_");
		aff.append(hashCode());
		aff.append(" :\n");
		
		/* On récupère le numéro de région de l'environnement courant */
		aff.append("lw $v0, 4($t2) \n");
		aff.append("sub $v0, $v0, $v1\n");
		
		/* Branchement vers la fin si les deux numéros sont égaux */
		aff.append("beqz $v0, fintq_");
		aff.append(hashCode());
		aff.append("\n");
		
		/* On repart au début et on essaye avec l'environnement précédent sinon */
		aff.append("lw $t2, 8($t2) \n");
		aff.append("j tq_");
		aff.append(hashCode());
		aff.append("\n");
		
		/* Sortie TantQue */
		aff.append("fintq_");
		aff.append(hashCode());
		aff.append(" :\n");
		
		aff.append("# Dépile la valeur à mettre dans la variable\n");
	    aff.append("add $sp, $sp, 4\n");
	    aff.append("lw $v0, 0($sp)\n");
	    
		aff.append("sw $v0, ");
		aff.append(deplVar);
		aff.append("($t2)");
		aff.append("\n");
		
        return aff.toString();
	}

	@Override
	public String toString() {
		return idf + " = " + exp.toString() + " ;";
	}

}
