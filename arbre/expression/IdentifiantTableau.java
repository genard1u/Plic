package yal.arbre.expression;

import yal.analyse.tds.TDS;
import yal.analyse.tds.entree.EntreeVariable;
import yal.analyse.tds.symbole.Symbole;
import yal.exceptions.AnalyseSemantiqueException;

public class IdentifiantTableau extends Expression {

	private String idf;
	
	private Expression caseTab;
	
	private int deplVar;
	private int numeroRegion;
	
	
	public IdentifiantTableau(String identifiant, Expression exp, int n) {
		super(n);
		idf = identifiant;
		caseTab = exp;
	}
	
	@Override
	public String getType() {
		return "entier";
	}

	@Override
	public String operation() {
		return " CaseTableau ";
	}

	@Override
	public void verifier() {
		caseTab.verifier();
		
		if (!(caseTab.getType().equals("entier"))) {
			StringBuilder erreur = new StringBuilder(40);
			
			erreur.append("erreur de type :\t");
			erreur.append("indice ");
			erreur.append("`");
			erreur.append(idf);
			erreur.append("`");
			erreur.append("\n\t");
			erreur.append(caseTab);
			erreur.append(" n'est pas un entier");
			
			throw new AnalyseSemantiqueException(getNoLigne(),erreur.toString());
		}
		
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
			
			throw new AnalyseSemantiqueException(getNoLigne(),erreur.toString());
		}
		
		deplVar = s.getDeplacement();
		numeroRegion = s.numeroRegion();
	}

	@Override
	public String toMIPS() {
		StringBuilder accesCase = new StringBuilder(50);
		
		accesCase.append("# Place la valeur d'une case de tableau dans $v0\n");
		
		/* Base courante */
		accesCase.append("# Récupère la base courante\n");
		accesCase.append("move $t2, $s7\n");
		
		/* Numéro de région où est déclarée la variable */
		accesCase.append("# Récupère le numéro de région où est déclaré le tableau\n");
		accesCase.append("li $v1, ");
		accesCase.append(numeroRegion);
		accesCase.append("\n");
		
		/* Entrée TantQue */
		accesCase.append("tq_");
		accesCase.append(hashCode());
		accesCase.append(" :\n");
		
		/* On récupère le numéro de région de l'environnement courant */
		accesCase.append("lw $v0, 4($t2) \n");
		accesCase.append("sub $v0, $v0, $v1\n");
		
		/* Branchement vers la fin si les deux numéros sont égaux */
		accesCase.append("beqz $v0, fintq_");
		accesCase.append(hashCode());
		accesCase.append("\n");
		
		/* On repart au début et on essaye avec l'environnement précédent sinon */
		accesCase.append("lw $t2, 8($t2) \n");
		accesCase.append("j tq_");
		accesCase.append(hashCode());
		accesCase.append("\n");
		
		/* Sortie TantQue */
		accesCase.append("fintq_");
		accesCase.append(hashCode());
		accesCase.append(" :\n");
		
		/* Chargement de l'adresse du tableau dans $t8 */
		accesCase.append("# Chargement de l'adresse du tableau dans $t8\n");
		accesCase.append("lw $t8, ");
		accesCase.append(deplVar);
		accesCase.append("($t2)");
		accesCase.append("\n");
		
		/* Indice dans $v0 */
		accesCase.append(caseTab.toMIPS());
		
		/* Accès avec un indice inférieur à 0 */
		accesCase.append("bltz $v0, erreurAccesTableauInvalide\n");
		
		/* On charge la longueur du tableau dans $t2 */
		accesCase.append("lw $t2, 0($t8)\n");
		
		/* La longueur moins l'indice dans $t2 */
		accesCase.append("sub $t2, $t2, $v0\n");
		
		/* Accès avec un indice supérieur à la longueur du tableau */
		accesCase.append("blez $t2, erreurAccesTableauInvalide\n");
		
		/* $v0 = indiceTableau, $t8 = adresse début tableau */
		/* $t1 va contenir le déplacement pour aller au bon indice */
		accesCase.append("li $t3, -4\n");
		accesCase.append("mult $v0, $t3\n");
		accesCase.append("mflo $t1\n");
		
		/* On retire -4 à $t1 (place de la longueur) */
		accesCase.append("add $t1, $t1, -4\n");
		
		/* Ajout du déplacement de $t1 à $t8 */
		accesCase.append("add $t8, $t8, $t1\n");
		
		/* Chargement de la case dans $v0 */
		accesCase.append("lw $v0, 0($t8)\n");
		
		return accesCase.toString();
	}
	
	@Override
	public String toString() {
		return idf + "[" + caseTab + "]";
	}

}
