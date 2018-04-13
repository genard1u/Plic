package yal.arbre.expression;

import yal.analyse.tds.TDS;
import yal.analyse.tds.entree.EntreeVariable;
import yal.analyse.tds.symbole.Symbole;
import yal.exceptions.AnalyseSemantiqueException;

public class Identifiant extends Expression {

	protected String idf;
	protected String type;
	
	protected int deplVar;
	protected int numeroRegion;
	
	
	public Identifiant(String idf, int n) {
		super(n);
		this.idf = idf;
	}

	@Override
	public String getType() {
		return type;
	}

	@Override
	public String operation() {
		return " Variable ";
	}

	public void setRegion(int region) {
		numeroRegion = region;
	}
	
	@Override
	public void verifier() {
		EntreeVariable e = new EntreeVariable(idf);
		Symbole s = TDS.getInstance().identifier(e);
		
		if (s == null) {
			throw new AnalyseSemantiqueException(getNoLigne(), "aucune déclaration de `" + idf + "`");
		}
		
   	    type = s.getType();
		deplVar = s.getDeplacement();
		numeroRegion = s.numeroRegion();
	}

	@Override
	public String toMIPS() {
		StringBuilder var = new StringBuilder(50);
		
		var.append("# Place la valeur d'une variable dans $v0\n");
		
		/* Base courante */
		var.append("# Récupère la base courante\n");
		var.append("move $t2, $s7\n");
		
		/* Numéro de région où est déclarée la variable */
		var.append("# Récupère le numéro de région où est déclarée la variable\n");
		var.append("li $v1, ");
		var.append(numeroRegion);
		var.append("\n");
		
		/* Entrée TantQue */
		var.append("tq_");
		var.append(hashCode());
		var.append(" :\n");
		
		/* On récupère le numéro de région de l'environnement courant */
		var.append("lw $v0, 4($t2) \n");
		var.append("sub $v0, $v0, $v1\n");
		
		/* Branchement vers la fin si les deux numéros sont égaux */
		var.append("beqz $v0, fintq_");
		var.append(hashCode());
		var.append("\n");
		
		/* On repart au début et on essaye avec l'environnement précédent sinon */
		var.append("lw $t2, 8($t2) \n");
		var.append("j tq_");
		var.append(hashCode());
		var.append("\n");
		
		/* Sortie TantQue */
		var.append("fintq_");
		var.append(hashCode());
		var.append(" :\n");
		
		/* Chargement dans $v0 */
		var.append("# Chargement dans $v0\n");
		var.append("lw $v0, ");
		var.append(deplVar);
		var.append("($t2)");
		var.append("\n");
		
		return var.toString();
	}

	@Override
	public String toString() {
		return idf;
	}
	
}
