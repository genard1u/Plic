package yal.arbre.instruction;

import yal.analyse.tds.TDS;
import yal.arbre.ArbreAbstrait;
import yal.arbre.BlocDInstructions;

public class Fonction extends ArbreAbstrait {
	
	private String idf;
	private String typeRetour;
	private String[] parametres;
	
	private BlocDInstructions li;
	
	
	public Fonction(int noLigne, BlocDInstructions li) {
		super(noLigne);
		this.li = li;
	}

	@Override
	public void verifier() {
		li.verifier();
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
		fonction.append("sw "+TDS.getInstance().numeroRegion()+", 0($sp)\n");
		fonction.append("add $sp, $sp, -4\n");
		fonction.append("\n");
		


		 // 		$s7 <- $sp
		
		//0 ACTUELLEMENT DONC PAS DE PROBLEME
		 // 		$sp <- $sp-place variable
		
		

		 fonction.append(li.toMIPS()+"\n");
		 fonction.append("# Dépilement de la base\n");
		 //sp <- sp+nbVariable+n°region+chainagedynamique
		 fonction.append("add $sp, $sp, "+TDS.getInstance().nbVariables()+"\n");
		 fonction.append("add $sp, $sp, "+TDS.getInstance().numeroRegion()+"\n");
		 fonction.append("add $sp, $sp, "+TDS.getInstance().numeroParent()+"\n");
		 
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
		
		int dernier = parametres.length - 1;
		
		for (int i = 0; i < dernier; i ++) {
		    fonction.append(parametres[i]);
		    fonction.append(", ");
		}
		
		if (parametres.length > 0) {
			fonction.append(parametres[dernier]);
		}
		
		fonction.append("");
		fonction.append(") {");
		fonction.append(li.toString());
		fonction.append("}");
		fonction.append("\n");
		
		return fonction.toString();
	}
	
}
