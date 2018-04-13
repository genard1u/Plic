package yal.arbre.instruction;

import yal.arbre.BlocDInstructions;
import yal.arbre.expression.Expression;
import yal.exceptions.AnalyseSemantiqueException;

public class Condition extends Instruction {

	private Expression exp;
    private BlocDInstructions alors;
    private BlocDInstructions sinon;
    
    private boolean estSinon;
    
    
    public Condition(Expression expr) {
    	super(expr.getNoLigne());    
    	
    	exp = expr;   	
    	
    	alors = new BlocDInstructions(noLigne + 1);
    	sinon = new BlocDInstructions(noLigne + 1);
    	
    	estSinon = false;
    }
    
    /**
     * Instancie une condition avec un seul bloc comprenant des instructions
     * @param expr
     * @param li
     * @param vide
     */
	public Condition(Expression expr, BlocDInstructions li, boolean vide) {
		super(expr.getNoLigne());
		
		assert li != null;
		
		exp = expr;
		
		if (!vide) {
			alors = li;
			sinon = new BlocDInstructions(noLigne + 1);
			estSinon = false;
		}
		else {
			alors = new BlocDInstructions(noLigne + 1);
			sinon = li;
			estSinon = true;
		}
	}

	public Condition(Expression expr, BlocDInstructions alors, BlocDInstructions sinon) {
		super(expr.getNoLigne());
		
		exp = expr;
		
		this.alors = alors;
		this.sinon = sinon;
		
		estSinon = true;
	}
	
	public boolean estSinon() {
		return estSinon;
	}
	
	@Override
	public boolean estRetourne() {
		boolean sinonEstRetourne = false;
		
		if (estSinon) {
			sinonEstRetourne = sinon.estRetourne();
		}
		
		return alors.estRetourne() && sinonEstRetourne;
	}
	
	@Override
	public void verifier() {
		exp.verifier();
		
		if (!exp.getType().equals("booleen")) {
			StringBuilder erreur = new StringBuilder(40);
			
			erreur.append("erreur de type :\t");
			erreur.append(exp);
			erreur.append("\n\t");
			erreur.append("une expression évaluée pour une condition doit être booléenne");
			
			throw new AnalyseSemantiqueException(getNoLigne(), erreur.toString());
		}

		alors.verifier();
		sinon.verifier();
	}

	@Override
	public String toMIPS() {
        StringBuilder condition = new StringBuilder(150);
		int hash = hashCode();
		
		condition.append("# Condition\n");
		
		condition.append("si_");
		condition.append(hash);
		condition.append(" :\n");
		condition.append(exp.toMIPS());
		
		condition.append("beqz $v0, sinon_");
		condition.append(hash);
		condition.append("\n");
		
		condition.append("alors_");
		condition.append(hash);
		condition.append(" :\n");
		condition.append(alors.toMIPS());
		condition.append("j fin_");
		condition.append(hash);
		condition.append("\n");
		
		condition.append("sinon_");
		condition.append(hash);
		condition.append(" :\n");
		condition.append(sinon.toMIPS());
		
		condition.append("fin_");
		condition.append(hash);
		condition.append(" :\n");
		
		return condition.toString();
	}

}
