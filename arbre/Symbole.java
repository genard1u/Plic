package yal.arbre;

public abstract class Symbole {

	private String type;
	private int deplacement;
	
	/* private int numRegion;
	private int numImbrication; */
	
	
	public Symbole(String type) {
		this.type = type;
		deplacement = TDS.getInstance().tailleZoneDesVariables();
	}
	
	public String getType() {
		return type;
	}
	
	public int getDeplacement() {
		return deplacement;
	}	
	
}
