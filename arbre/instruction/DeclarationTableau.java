package yal.arbre.instruction;

import yal.analyse.tds.TDS;
import yal.analyse.tds.entree.EntreeVariable;
import yal.analyse.tds.symbole.Symbole;
import yal.arbre.expression.Expression;
import yal.exceptions.AnalyseSemantiqueException;

/**
 * Dans la pile, le tableau est représenté par la longueur
 * suivie des valeurs de toutes ses cases.
 * 
 * @author Pierre Génard
 */
public class DeclarationTableau extends Instruction {

	private String idf;
	private Expression longueur;
	
	private int deplVar;
	private int numeroRegion;
	
	
	public DeclarationTableau(String identifiant, Expression exp) {
		super(exp.getNoLigne());
		idf = identifiant;
		longueur = exp;
	}

	public boolean estDeclarationTableau() {
		return true;
	}
	
	@Override
	public void verifier() {
		numeroRegion = TDS.getInstance().numeroRegion();
		
		assert numeroRegion >= 0;
		
		longueur.verifier();		
		
		if (numeroRegion == 0) {
			if (!longueur.estConstante()) {
				StringBuilder erreur = new StringBuilder(40);
				
				erreur.append("erreur déclaration de tableau :\t");
				erreur.append("`");
				erreur.append(longueur);
				erreur.append("`");
				erreur.append(" n'est pas une constante");
				erreur.append("\n\t");
				erreur.append("dans le bloc principal, la longueur du tableau doit être une constante");
				
				throw new AnalyseSemantiqueException(getNoLigne(),erreur.toString());
			}
		}
		
		if (!(longueur.getType().equals("entier"))) {
			StringBuilder erreur = new StringBuilder(40);
			
			erreur.append("erreur de type :\t");
			erreur.append("sur la longueur de ");
			erreur.append("`");
			erreur.append(idf);
			erreur.append("`");
			erreur.append("\n\t");
			erreur.append(longueur);
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
			
			throw new AnalyseSemantiqueException(getNoLigne(), erreur.toString());
		}
		
		deplVar = s.getDeplacement();
		numeroRegion = TDS.getInstance().numeroRegion();
	}

	@Override
	public String toMIPS() {
		StringBuilder init = new StringBuilder(60);
		
		init.append("# Déclaration du tableau " + idf + "\n");
		
		init.append("# Stocke l'adresse de début du tableau\n");
		init.append("sw $sp, ");
		init.append(deplVar);
		init.append("($s7)\n");	
		
		init.append("# Place la valeur de la longueur dans $v0\n");
		init.append(longueur.toMIPS());
		
		init.append("# Vérifie que la longueur est strictement supérieure à 0\n");
        init.append("blez $v0, erreurLongueurInvalide\n");
        
		init.append("# Stocke la longueur\n");
		init.append("sw $v0, 0($sp)\n");
		
		init.append("# Initialisation du tableau\n");
		
		init.append("tq_");
		init.append(hashCode());
		init.append(" :\n");	
		
		init.append("beqz $v0, fintq_");
		init.append(hashCode());
		init.append("\n");
		
		init.append("addi $v0, $v0, -1\n");
		init.append("addi $sp, $sp, -4\n");		
		init.append("sw $zero, 0($sp)\n");
		
		init.append("j tq_");
		init.append(hashCode());
		init.append("\n");

		init.append("fintq_");
		init.append(hashCode());
		init.append(" :\n");
		
		// Je crois
		init.append("addi $sp, $sp, -4\n");	
		
		return init.toString();
	}

}
