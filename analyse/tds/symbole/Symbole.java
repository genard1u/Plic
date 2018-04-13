package yal.analyse.tds.symbole;

import yal.analyse.tds.TDS;

public abstract class Symbole {

	protected String type;
	protected int deplacement;	
	protected int espace;
	protected int numeroRegion;
	protected int niveauImbrication;
	
	
	protected Symbole() {}
	
	protected Symbole(String type) {
		this.type = type;
		numeroRegion = TDS.getInstance().numeroRegion();
		niveauImbrication = TDS.getInstance().niveauImbrication();
	}
	
	public boolean pourVariable() {
		return false;
	}
	
	public boolean pourParametre() {
		return false;
	}
	
	public String getType() {
		return type;
	}
	
	public int getDeplacement() {
		return deplacement;
	}	
	
	public int getEspace() {
	    return espace;	
	}
	
	public int numeroRegion() {
		return numeroRegion;
	}

	public int niveauImbrication() {
		return niveauImbrication;
	}

	@Override
	public String toString() {
		return "Symbole [type=" + type + ", deplacement=" + deplacement + ", numeroRegion=" + numeroRegion
				+ ", niveauImbrication=" + niveauImbrication + "]";
	}
	
}
