package yal.analyse.tds.symbole;

import yal.analyse.tds.TDS;

public class SymboleFonction extends Symbole {

	private String etiquette;
	
	private String[] typesDesParametres;
	
	
	public SymboleFonction(String typeRetour) {
		super(typeRetour);
		
		typesDesParametres = null;
		etiquette = construireEtiquette();
	}
	
	public SymboleFonction(String typeRetour, String... parametres) {
		super(typeRetour);
		
		typesDesParametres = new String[parametres.length];
		
		for (int i = 0; i < parametres.length; i ++) {
			typesDesParametres[i] = parametres[i];
		}
	}
	
	private String construireEtiquette() {
		StringBuilder eti = new StringBuilder(10);
		
		eti.append("fonction");
		eti.append(TDS.getInstance().numeroRegion());
		
		return eti.toString();
	}
	
	public String etiquette() {
		return etiquette;
	}
	
	public String getTypeRetour() {
		return getType();
	}
	
}
