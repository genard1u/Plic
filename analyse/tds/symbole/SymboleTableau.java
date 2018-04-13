package yal.analyse.tds.symbole;

import yal.analyse.tds.TDS;

public class SymboleTableau extends Symbole {

	public SymboleTableau() {
		super("tableau");
		deplacement = - TDS.getInstance().tailleZoneDesVariables();
		espace = 4;
	}
	
	public boolean pourVariable() {
		return true;
	}
	
}
