package yal.arbre.instruction;

import yal.analyse.tds.TDS;
import yal.analyse.tds.entree.EntreeVariable;
import yal.analyse.tds.symbole.Symbole;
import yal.exceptions.AnalyseSemantiqueException;

public class Lire extends Instruction {

	private String idf;
	private String type;
	
	private int deplVar;
	private int numeroRegion;
	
	
	public Lire(String idf, int no) {
		super(no);
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
		
		if (!type.equals("entier")) {
			StringBuilder erreur = new StringBuilder(40);
			
			erreur.append("erreur de type :\t");
			erreur.append("lire ");
			erreur.append(idf);
			erreur.append("\n\t");
			erreur.append("`");
			erreur.append(idf);
			erreur.append("` doit être un entier");
			
			throw new AnalyseSemantiqueException(getNoLigne(),erreur.toString());
		}
		
		deplVar = s.getDeplacement();
		numeroRegion = s.numeroRegion();
	}

	@Override
	public String toMIPS() {
		StringBuilder lire = new StringBuilder(40);
		
		lire.append("# Lecture d'un entier\n");
		
		lire.append("# Appel système pour la lecture\n");
		lire.append("li $v0, 5\n");
		lire.append("syscall\n");
		
		lire.append("# Empile la valeur lue\n");
		lire.append("sw $v0, 0($sp)\n");
		lire.append("add $sp, $sp, -4\n");
		
		lire.append("# Transfert de la valeur lue vers la variable\n");
		
		/* Base courante */
		lire.append("# Récupère la base courante\n");
		lire.append("move $t2, $s7\n");
		
		/* Numéro de région où est déclarée la variable */
		lire.append("# Récupère le numéro de région où est déclarée la variable\n");
		lire.append("li $v1, ");
		lire.append(numeroRegion);
		lire.append("\n");
		
		/* Entrée TantQue */
		lire.append("tq_");
		lire.append(hashCode());
		lire.append(" :\n");
		
		/* On récupère le numéro de région de l'environnement courant */
		lire.append("lw $v0, 4($t2) \n");
		lire.append("sub $v0, $v0, $v1\n");
		
		/* Branchement vers la fin si les deux numéros sont égaux */
		lire.append("beqz $v0, fintq_");
		lire.append(hashCode());
		lire.append("\n");
		
		/* On repart au début et on essaye avec l'environnement précédent sinon */
		lire.append("lw $t2, 8($t2) \n");
		lire.append("j tq_");
		lire.append(hashCode());
		lire.append("\n");
		
		/* Sortie TantQue */
		lire.append("fintq_");
		lire.append(hashCode());
		lire.append(" :\n");
		
	    lire.append("# Dépile la valeur lue\n");
	    lire.append("add $sp, $sp, 4\n");
	    lire.append("lw $v0, 0($sp)\n");
	    
		lire.append("sw $v0, ");
		lire.append(deplVar);
		lire.append("($t2)");
		lire.append("\n");
		
		return lire.toString();
	}
	
	@Override
	public String toString() {
		return "lire " + idf + " ;";
	}
	
}
