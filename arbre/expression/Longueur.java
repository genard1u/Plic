package yal.arbre.expression;

import yal.analyse.tds.TDS;
import yal.analyse.tds.entree.EntreeVariable;
import yal.analyse.tds.symbole.Symbole;
import yal.exceptions.AnalyseSemantiqueException;

public class Longueur extends Expression {

	private String idf;
	
	private int deplVar;
	private int numeroRegion;
	
	
	public Longueur(String identifiant, int n) {
		super(n);
		idf = identifiant;
	}

	@Override
	public String getType() {
		return "entier";
	}

	@Override
	public String operation() {
		return " Longueur ";
	}

	@Override
	public void verifier() {
		EntreeVariable e = new EntreeVariable(idf);
		Symbole s = TDS.getInstance().identifier(e);
		
		if (s == null) {
			throw new AnalyseSemantiqueException(getNoLigne(), "aucune déclaration de `" + idf + "`");
		}
		
		if (!(s.getType().equals("tableau"))) {
			StringBuilder erreur = new StringBuilder(30);
			
			erreur.append("erreur mot-clé langage :\t");
			erreur.append(idf);
			erreur.append(".longueur");
			erreur.append("\n\t");
			erreur.append("`");
			erreur.append(idf);
			erreur.append("`");
			erreur.append(" n'est pas un identifiant de tableau");			
			
			throw new AnalyseSemantiqueException(getNoLigne(), erreur.toString());
		}
		
		deplVar = s.getDeplacement();
		numeroRegion = s.numeroRegion();
	}

	@Override
	public String toMIPS() {
		StringBuilder longueur = new StringBuilder(50);
		
		longueur.append("# Place la valeur de la longueur d'un tableau dans $v0\n");
		
		/* Base courante */
		longueur.append("# Récupère la base courante\n");
		longueur.append("move $t2, $s7\n");
		
		/* Numéro de région où est déclarée la variable */
		longueur.append("# Récupère le numéro de région où est déclaré le tableau\n");
		longueur.append("li $v1, ");
		longueur.append(numeroRegion);
		longueur.append("\n");
		
		/* Entrée TantQue */
		longueur.append("tq_");
		longueur.append(hashCode());
		longueur.append(" :\n");
		
		/* On récupère le numéro de région de l'environnement courant */
		longueur.append("lw $v0, 4($t2) \n");
		longueur.append("sub $v0, $v0, $v1\n");
		
		/* Branchement vers la fin si les deux numéros sont égaux */
		longueur.append("beqz $v0, fintq_");
		longueur.append(hashCode());
		longueur.append("\n");
		
		/* On repart au début et on essaye avec l'environnement précédent sinon */
		longueur.append("lw $t2, 8($t2) \n");
		longueur.append("j tq_");
		longueur.append(hashCode());
		longueur.append("\n");
		
		/* Sortie TantQue */
		longueur.append("fintq_");
		longueur.append(hashCode());
		longueur.append(" :\n");
		
		/* Chargement de l'adresse du tableau dans $t4 */
		longueur.append("# Chargement de l'adresse du tableau dans $t4\n");
		longueur.append("lw $t4, ");
		longueur.append(deplVar);
		longueur.append("($t2)");
		longueur.append("\n");
		
		/* Chargement de la longueur dans $v0 */
		longueur.append("# Chargement de la longueur dans $v0\n");
		longueur.append("lw $v0, 0($t4)\n");
		
		return longueur.toString();
	}

	@Override
	public String toString() {
		return idf + ".longueur";
	}

}
