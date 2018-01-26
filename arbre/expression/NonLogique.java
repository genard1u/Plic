package yal.arbre.expression;

import yal.exceptions.AnalyseSemantiqueException;

/**
 * @author Clément Bellanger, Pierre Génard, Valentin Thouvenin
 */
public class NonLogique extends Unaire {
    
    public NonLogique(Expression expr) {
        super(expr);
    }

    @Override
    public String operateur() {
        return " non " ;
    }

    @Override
	public int getType() {
		return BOOLEEN;
	}
    
    @Override
	public String operation() {
		return " Non Logique ";
	}
    
    @Override
    public void verifier() throws AnalyseSemantiqueException {
    	super.verifier();
    	
	    if (expression.getType() != BOOLEEN) {
	    	StringBuilder erreur = new StringBuilder(25);
	    	
	    	erreur.append("erreur de type : ");
	    	erreur.append(operateur());
	    	erreur.append(expression.getType());
	    	
	        throw new AnalyseSemantiqueException(getNoLigne(), erreur.toString());
	    }		
    }

	@Override
	public String toMIPS() {
		StringBuilder non = new StringBuilder(100);
		
		non.append(super.toMIPS());
		non.append("not $v0, $v0\n");
		
		return non.toString();
	}
	
}
