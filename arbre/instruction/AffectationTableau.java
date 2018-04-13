package yal.arbre.instruction;

import yal.analyse.tds.TDS;
import yal.analyse.tds.entree.EntreeVariable;
import yal.analyse.tds.symbole.Symbole;
import yal.arbre.expression.Expression;
import yal.exceptions.AnalyseSemantiqueException;

public class AffectationTableau extends Instruction {

    private Expression exp; 
	
	private String idf;
	private Expression caseTab;
	
	private int deplVar;
	private int numeroRegion;
	
	
	public AffectationTableau(String identifiant, Expression indice, Expression expression) {
		super(expression.getNoLigne());
		idf = identifiant;
		caseTab = indice;
		exp = expression;
	}

	@Override
	public void verifier() {
		exp.verifier();
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
		StringBuilder aff = new StringBuilder(50);
		
		aff.append("# Affectation d'une valeur à une case d'un tableau\n");
		aff.append(exp.toMIPS());
		
		aff.append("# Empile la valeur à mettre dans la variable\n");
		aff.append("sw $v0, 0($sp)\n");
		aff.append("add $sp, $sp, -4\n");
		
		/* Code pour récupérer l'adresse de la case du tableau 
		   où mettre la valeur de l'expression */
		
		/* Base courante */
		aff.append("# Récupère la base courante\n");
		aff.append("move $t2, $s7\n");
		
		/* Numéro de région où est déclarée la variable */
		aff.append("# Récupère le numéro de région où est déclaré le tableau\n");
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
		
		/* Chargement de l'adresse du tableau dans $t8 */
		aff.append("# Chargement de l'adresse du tableau dans $t8\n");
		aff.append("lw $t8, ");
		aff.append(deplVar);
		aff.append("($t2)");
		aff.append("\n");
		
		/* Indice dans $v0 */
		aff.append(caseTab.toMIPS());
		
		/* Accès avec un indice inférieur à 0 */
		aff.append("bltz $v0, erreurAccesTableauInvalide\n");
		
		/* On charge la longueur du tableau dans $t2 */
		aff.append("lw $t2, 0($t8)\n");
		
		/* La longueur moins l'indice dans $t2 */
		aff.append("sub $t2, $t2, $v0\n");
		
		/* Accès avec un indice supérieur à la longueur du tableau */
		aff.append("blez $t2, erreurAccesTableauInvalide\n");
		
		/* $v0 = indiceTableau, $t8 = adresse début tableau */
		/* $t1 va contenir le déplacement pour aller au bon indice */
		aff.append("li $t3, -4\n");
		aff.append("mult $v0, $t3\n");
		aff.append("mflo $t1\n");
		
		/* On retire -4 à $t1 (place de la longueur) */
		aff.append("add $t1, $t1, -4");
		
		aff.append("# Dépile la valeur à mettre dans la variable\n");
	    aff.append("add $sp, $sp, 4\n");
	    aff.append("lw $v0, 0($sp)\n");
	    
	    /* Ajout du déplacement de $t1 à $t8 */
		aff.append("add $t8, $t8, $t1\n");
		
	    /* Chargement de la valeur dans la case du tableau */
		aff.append("sw $v0, 0($t8)\n");		
		
		return aff.toString();
	}

	@Override
	public String toString() {
		return idf + "[" + caseTab + "] = " + exp.toString() + " ;";
	}

}
