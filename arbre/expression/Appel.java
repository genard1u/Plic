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
	
	private ArrayList<Expression> parametresEffectifs;
	private int nombreParametres;
	

	public Appel(String identifiant, int noLigne) {
		super(noLigne);
		idf = identifiant;
		parametresEffectifs = new ArrayList<Expression>();
		nombreParametres = 0;
	}
	
	public Appel(String identifiant, ArrayList<Expression> parEff, int noLigne) {
		super(noLigne);
		idf = identifiant;
		parametresEffectifs = parEff;
		nombreParametres = parEff.size();
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
		verifierParametres(s);
	}

	public void verifierParametres(SymboleFonction s) {
        ArrayList<String> types = s.types();
		
		assert types.size() == nombreParametres;
		
		for (int i = 0; i < types.size(); i ++) {
			Expression parEff = parametresEffectifs.get(i);
			String typeFormel = types.get(i);
			String typeEffectif = parEff.getType();
			
			parEff.verifier();
			
			if (!typeFormel.equals(typeEffectif)) {
				StringBuilder erreur = new StringBuilder(30);
				
				erreur.append("erreur de type :\t");
				erreur.append("appel avec parmi les parametres : ");
				erreur.append(parEff.toString());
				erreur.append("\n\t");
				erreur.append("un type effectif est différent du type formel");
				erreur.append("une fonction doit retourner un entier");
				
				throw new AnalyseSemantiqueException(getNoLigne(), erreur.toString());
			}
		}		
	}
	
	@Override
	public String toMIPS() {	
		StringBuilder appel = new StringBuilder(50);
		
		appel.append("# Allocation de la place pour les paramètres\n");
		appel.append("add $sp, $sp, -");
		appel.append(nombreParametres * 4);
		appel.append("\n\n");
		
		for (int i = 1; i <= nombreParametres; i ++) {
			Expression parEff = parametresEffectifs.get(i - 1);
			
			appel.append(parEff.toMIPS());
			appel.append("sw $v0, ");
			appel.append(i * 4);
			appel.append("($sp)\n");
		}
		
	    appel.append("# Appel d'une fonction \n");
		appel.append("# Allocation de la place pour la valeur retour\n");
		appel.append("add $sp, $sp, -4\n");
		appel.append("\n");
		
		appel.append("# Saut vers l'étiquette de la fonction " + idf + "\n");
		appel.append("jal " + etiquette + "\n");
		appel.append("\n");
		
		appel.append("# Dépiler dans $v0\n"); 
		appel.append("add $sp, $sp, 4\n");
		appel.append("lw $v0, 0($sp)\n");
		appel.append("\n");
		
		appel.append("# Dépiler les paramètres\n");
		appel.append("add $sp, $sp, ");
		appel.append(nombreParametres * 4);
		appel.append("\n\n");
		
		return appel.toString();
	}

	@Override
	public String toString() {
		StringBuilder appel = new StringBuilder(20);
		
		appel.append(idf);
		appel.append("(");
		
		for (int i = 0; i < nombreParametres - 1; i ++) {
			Expression par = parametresEffectifs.get(i);
			appel.append(par.toString());
			appel.append(", ");
		}
		
		if (nombreParametres > 0) {
			Expression par = parametresEffectifs.get(nombreParametres - 1);
			appel.append(par.toString());
		}
		
		appel.append(")");
		
		return appel.toString();
	}

}
