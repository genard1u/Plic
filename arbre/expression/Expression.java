package yal.arbre.expression;

import yal.arbre.ArbreAbstrait;

/**
 * @author Clément Bellanger, Pierre Génard, Valentin Thouvenin
 */
public abstract class Expression extends ArbreAbstrait {
    
    protected Expression(int n) {
        super(n) ;
    }

    public boolean estConstante() {
    	return false;
    }
    
    public abstract String getType();
    public abstract String operation();
    
}
