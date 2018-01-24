package yal.arbre;

/**
 * @author Clément Bellanger, Pierre Génard, Valentin Thouvenin
 */

public abstract class ArbreAbstrait {

    // type des expressions  
    protected static enum TYPE {
        ENTIER,
        BOOLEEN
    }
  
    // numéro de ligne du début de l'instruction
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
