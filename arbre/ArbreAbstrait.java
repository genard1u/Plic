package yal.arbre;

/**
 * @author Clément Bellanger, Pierre Génard, Valentin Thouvenin
 */
public abstract class ArbreAbstrait {
	
    protected int noLigne ;

  
    protected ArbreAbstrait(int no) {
        noLigne = no ;
    }
    
    public int getNoLigne() {
        return noLigne ;
    }

    public abstract void verifier();
    public abstract String toMIPS();

}
