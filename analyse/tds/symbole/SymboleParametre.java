package yal.analyse.tds.symbole;

import yal.analyse.tds.TDS;

public class SymboleParametre extends Symbole {

	private static final int DEBUT_PAR = 20;
	
	
	public SymboleParametre(String type) {
		super(type);
		deplacement = DEBUT_PAR + TDS.getInstance().tailleZoneDesParametres();
		espace = 4;
	}

    public boolean pourParametre() {
    	return true;
    }
    
}

