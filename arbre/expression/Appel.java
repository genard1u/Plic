package yal.arbre.expression;

import java.util.ArrayList;

import yal.analyse.tds.TDS;
import yal.analyse.tds.entree.EntreeFonction;
import yal.analyse.tds.symbole.Symbole;
import yal.exceptions.AnalyseSemantiqueException;

public class Appel extends Expression {

	private String idf;
	private String type;
	private String etiquette; 
	private int nombreParametres;
	
	// private String etiquette;
	

	public Appel(int n) {
		super(n);
		nombreParametres = 0;
	}
	
	public Appel(ArrayList<Expression> par, int n) {
		super(n);
		nombreParametres = par.size();
	}
	
	@Override
	public String getType() {
		return type;
	}

	@Override
	public String operation() {
		return " Appel ";
	}

	@Override
	public void verifier() {
		EntreeFonction e = new EntreeFonction(idf, nombreParametres);
		Symbole s = TDS.getInstance().identifier(e);
		
		if (s == null) {
			throw new AnalyseSemantiqueException(getNoLigne(), "aucune déclaration de `" + idf + "()`");
		}
	}

	@Override
	public String toMIPS() {	
		StringBuilder appel = new StringBuilder(50);
		appel.append("# Appel d'un fonction \n");
		appel.append("# Allocation de la place de la valeur retour\n");
		appel.append("add $sp, $sp, -4\n");
		
		appel.append("#Jump sur la fonction "+etiquette+"\n");
		appel.append("jal "+etiquette+"\n");
		
		appel.append("#Dépiler dans v0\n");
		appel.append("lw $v0, 0($sp)\n");
		appel.append("add $sp, $sp, 4\n");
		
		return appel.toString();
	}

	@Override
	public String toString() {
		StringBuilder appel = new StringBuilder(20);
		
		appel.append(idf);
		appel.append("(");
		// paramètres
		appel.append(")");
		
		return appel.toString();
	}

}
