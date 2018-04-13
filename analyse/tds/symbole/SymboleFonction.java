package yal.analyse.tds.symbole;

import java.util.ArrayList;

public class SymboleFonction extends Symbole {

	private String etiquette;
	
	private ArrayList<String> typesDesParametres;
	
	
	public SymboleFonction(String typeRetour) {
		super(typeRetour);
		deplacement = 0;
		espace = 0;
		typesDesParametres = new ArrayList<String>();
		etiquette = construireEtiquette();
	}
	
	public SymboleFonction(String typeRetour, ArrayList<String> types) {
		super(typeRetour);
		deplacement = 0;
		espace = 0;
		typesDesParametres = types;
		etiquette = construireEtiquette();
	}
	
	private String construireEtiquette() {
		StringBuilder eti = new StringBuilder(20);
		
		eti.append("fonction");
		eti.append(hashCode());
		
		return eti.toString();
	}
	
	public String etiquette() {
		return etiquette;
	}
	
	public String getTypeRetour() {
		return getType();
	}
	
	public ArrayList<String> types() {
		return typesDesParametres;
	}
	
}
