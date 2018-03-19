package yal.arbre.expression;

import java.util.ArrayList;

import yal.analyse.tds.TDS;
import yal.analyse.tds.entree.EntreeFonction;
import yal.analyse.tds.symbole.SymboleFonction;
import yal.exceptions.AnalyseSemantiqueException;

public class Appel extends Expression {

	private String idf;
	private String type;
	private String etiquette; 
	private int nombreParametres;
	

	public Appel(String idf, int n) {
		super(n);
		this.idf = idf;
		nombreParametres = 0;
	}
	
	public Appel(String idf, ArrayList<Expression> par, int n) {
		super(n);
		this.idf = idf;
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
		SymboleFonction s = (SymboleFonction) TDS.getInstance().identifier(e);

		if (s == null) {
			throw new AnalyseSemantiqueException(getNoLigne(), "aucune déclaration de `" + idf + "()`");
		}
		
		etiquette = s.etiquette();
		type = s.getType();
	}

	@Override
	public String toMIPS() {	
		StringBuilder appel = new StringBuilder(50);
		appel.append("# Appel d'un fonction \n");
		appel.append("# Allocation de la place de la valeur retour\n");
		appel.append("add $sp, $sp, -4\n");
		
		appel.append("# Jump sur la fonction " + idf + "\n");
		appel.append("jal " + etiquette + "\n");
		
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
