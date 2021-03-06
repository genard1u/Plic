package yal.arbre.instruction;

import yal.arbre.ArbreAbstrait;

/**
 * @author Clément Bellanger, Pierre Génard, Valentin Thouvenin
 */
public abstract class Instruction extends ArbreAbstrait {

	public Instruction(int no) {
		super(no);
	}	
	
	public boolean estRetourne() {
		return false;
	}
	
	public boolean estDeclarationTableau() {
		return false;
	}
	
	public abstract void verifier();
    public abstract String toMIPS();

}
