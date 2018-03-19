package yal.analyse.tds.symbole;

import yal.analyse.tds.TDS;

public abstract class Symbole {

	private String type;
	private int deplacement;	
	private int numeroRegion;
	
	
	public Symbole(String type) {
		this.type = type;
		deplacement = - TDS.getInstance().tailleZoneDesVariables();
		numeroRegion = TDS.getInstance().numeroRegion();
	}
	
	public String getType() {
		return type;
	}
	
	public int getDeplacement() {
		return deplacement;
	}	
	
	public int numeroRegion() {
		return numeroRegion;
	}

	@Override
	public String toString() {
		return "Symbole [type=" + type + ", deplacement=" + deplacement
				+ ", numeroRegion=" + numeroRegion + "]";
	}
	
}
