package yal.arbre.instruction;

import yal.analyse.tds.TDS;
import yal.analyse.tds.entree.EntreeFonction;
import yal.analyse.tds.symbole.Symbole;
import yal.arbre.BlocDInstructions;
import yal.exceptions.AnalyseSemantiqueException;

public class Fonction extends Instruction {
	
	private String idf;
	private String typeRetour;
	// private String[] parametres;
	
	private BlocDInstructions instructions;

	
	public Fonction(BlocDInstructions li, String idf, int noLigne) {
		super(noLigne);
		instructions = li;
		this.idf = idf;
	}

	@Override
	public boolean estRetourne() {
		assert instructions.nombreInstructions() > 0;
		
		return instructions.estRetourne();
	}
	
	@Override
	public void verifier() {
		EntreeFonction e = new EntreeFonction(idf, 0);
		Symbole s = TDS.getInstance().identifier(e);
		
		if (s == null) {
			throw new AnalyseSemantiqueException(getNoLigne(), "aucune déclaration de `" + idf + "()`");
		}
		
		typeRetour = s.getType();
		
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
		//int hash = hashCode();
		
		fonction.append("# Fonction\n");
		fonction.append(idf+":\n");
		
		
		fonction.append("# Empilement de s7\n");
		fonction.append("sw $s7, 0($sp)\n");
		fonction.append("add $sp, $sp, -4\n");
		fonction.append("\n");
		
		fonction.append("# Empilement du numero de region\n");
		fonction.append("sw " + TDS.getInstance().numeroRegion() + ", 0($sp)\n");
		fonction.append("add $sp, $sp, -4\n");
		fonction.append("\n");
		
		 // 		$s7 <- $sp
		fonction.append("add $s7, $sp, 0 \n");
		
		//0 ACTUELLEMENT DONC PAS DE PROBLEME
		 // 		$sp <- $sp-place variable nbVariables()
		fonction.append("add $sp, $sp, -" + TDS.getInstance().nbVariables() + "\n");
		

		 fonction.append(instructions.toMIPS() + "\n");
		
		 //sp <- sp+nbVariable+n°region+chainagedynamique
		 fonction.append("add $sp, $sp, " + TDS.getInstance().nbVariables() + "\n");
		 fonction.append("add $sp, $sp, " + TDS.getInstance().numeroRegion() + "\n");
		 fonction.append("add $sp, $sp, " + TDS.getInstance().numeroParent() + "\n");
		 
		 fonction.append("# Dépilement de la base\n");
		 fonction.append("add $sp, $sp, 4\n");
		 //fonction.append("lw $t8, 0($sp)\n");
		 fonction.append("\n");
		 
		 fonction.append("lw $ra, 0($sp)\n");
		 fonction.append("jr $ra+\n");
		
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
