package yal.arbre.instruction;

import yal.analyse.tds.TDS;
import yal.analyse.tds.entree.EntreeFonction;
import yal.analyse.tds.symbole.SymboleFonction;
import yal.arbre.BlocDInstructions;
import yal.exceptions.AnalyseSemantiqueException;

public class Fonction extends Instruction {
	
	private String idf;
	private String typeRetour;
	private String etiquette;
	// private String[] parametres;
	
	private BlocDInstructions instructions;

	
	public Fonction(BlocDInstructions li, String idf, int noLigne) {
		super(noLigne);
		this.idf = idf;
		instructions = li;
	}

	@Override
	public boolean estRetourne() {
		assert instructions.nombreInstructions() > 0;
		
		return instructions.estRetourne();
	}
	
	@Override
	public void verifier() {
		EntreeFonction e = new EntreeFonction(idf, 0);
		SymboleFonction s = (SymboleFonction) TDS.getInstance().identifier(e);
		
		if (s == null) {
			throw new AnalyseSemantiqueException(getNoLigne(), "aucune déclaration de `" + idf + "()`");
		}
		
		typeRetour = s.getType();
		etiquette = s.etiquette();
		
		TDS.getInstance().entreeBloc();		
		
		instructions.verifier();
		
		if (!estRetourne()) {
			throw new AnalyseSemantiqueException(getNoLigne(), "retourne peut ne pas être atteint dans `" + idf + "()`");
		}
		
		TDS.getInstance().sortieBloc();
	}
	
	@Override
	public String toMIPS() {
		StringBuilder fonction = new StringBuilder();
		
		fonction.append("# Fonction\n");
		fonction.append(etiquette + ":\n");
		
		fonction.append("# Empilement de l'adresse retour\n");
		fonction.append("sw $s7, 0($sp)\n");
		fonction.append("add $sp, $sp, -4\n");
		fonction.append("\n");

		fonction.append("# Empilement du chainage dynamique\n");
		fonction.append("sw "+TDS.getInstance().numeroParent()+", 0($sp)\n");
		fonction.append("add $sp, $sp, -4\n");
		fonction.append("\n");
		
		fonction.append("# Empilement du numero de region\n");
		fonction.append("sw " + TDS.getInstance().numeroRegion() + ", 0($sp)\n");
		fonction.append("add $sp, $sp, -4\n");
		fonction.append("\n");
				
		fonction.append("# Déplacement de la base\n");
		fonction.append("add $s7, $sp, 0 \n");
		
		fonction.append("# Allocation de la place des variables\n");
		fonction.append("add $sp, $sp, -"+TDS.getInstance().nbVariables()+"\n");
		
		fonction.append("# Instruction de la fonction\n");
		fonction.append(instructions.toMIPS()+"\n");
		
		return fonction.toString();
	}

	@Override
	public String toString() {
		StringBuilder fonction = new StringBuilder(40);
		
		fonction.append(typeRetour);
		fonction.append(" ");
		fonction.append(idf);
		fonction.append("(");
		
		/* int dernier = parametres.length - 1;
		
		for (int i = 0; i < dernier; i ++) {
		    fonction.append(parametres[i]);
		    fonction.append(", ");
		}
		
		if (parametres.length > 0) {
			fonction.append(parametres[dernier]);
		} */
		
		fonction.append(") {");
		fonction.append(instructions.toString());
		fonction.append("}");
		fonction.append("\n");
		
		return fonction.toString();
	}
	
}
