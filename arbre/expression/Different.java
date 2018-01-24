package yal.arbre.expression;

import yal.exceptions.AnalyseSemantiqueException;

/**
 * @author Clément Bellanger, Pierre Génard, Valentin Thouvenin
 */
public class Different extends Comparaison {

    public Different(Expression gauche, Expression droite) {
        super(gauche, droite);
    }

    @Override
    public String operateur() {
        return " != ";
    }

    @Override
	public void verifier() throws AnalyseSemantiqueException {
		if (gauche.getType() != droite.getType()) {
            StringBuilder erreur = new StringBuilder(25);
	    	
	    	erreur.append("erreur de type : ");
	    	erreur.append(gauche.getType());
	    	erreur.append(operateur());
	    	erreur.append(droite.getType());
	    	
			throw new AnalyseSemantiqueException(getNoLigne(), erreur.toString());
		}
	}

	@Override
	public String toMIPS() {
		StringBuilder sb = new StringBuilder(200);
		
		sb.append("# Different\n");
		
		sb.append("# Calcul de la partie gauche\n");
		sb.append(gauche.toMIPS());
		sb.append("# Empilement de la partie gauche\n");
		sb.append("sw $v0, 0($sp)\n");
		sb.append("add $sp, $sp, -4\n");
		sb.append("# Calcul de la partie droite\n");
		sb.append(droite.toMIPS());
		sb.append("# Dépilement de la partie gauche\n");
		sb.append("add $sp, $sp, 4\n");
		sb.append("lw $t8,($sp)\n");
		
		sb.append("# Comparaison entre $v0 et $t8\n");
		sb.append("bne $v0,$t8, alors\n");
		sb.append("# Si c'est egal, on met 0 dans $v0\n");
		sb.append("li $v0, 0\n");
		sb.append("j fin\n");
		sb.append("# Si c'est different, on met 1 dans $v0\n");
		sb.append("alors\n");
		sb.append("li $v0, 1\n");
		sb.append("fin\n");
		
		return sb.toString();
	}
  
}
