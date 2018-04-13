package yal.arbre;

import java.util.ArrayList;
import java.util.Iterator;

import yal.arbre.instruction.Instruction;

/**
 * Un bloc est une suite d'instructions à laquelle 
 * il est possible d'attacher une liste de déclarations.
 * Dans notre classe, on ne lui attache aucune déclaration.
 * 
 * @author Clément Bellanger, Pierre Génard, Valentin Thouvenin
 */
public class BlocDInstructions extends ArbreAbstrait {
    
	/**
	 * Liste des instructions du bloc
	 */
	private ArrayList<Instruction> bloc;
    
    
    public BlocDInstructions(int n) {
        super(n);
        bloc = new ArrayList<Instruction>();
    }
    
    public void ajouter(Instruction a) {
        bloc.add(a);
    }
       
    public void ajouterAuDebut(ArrayList<Instruction> li) {
    	for (int i = li.size() - 1; i >= 0; i --) {
    		bloc.add(0, li.get(i));
    	}
    }
    
    public boolean estVide() {
    	return bloc.isEmpty();
    }
    
    public Instruction recuperer(int index) {
    	assert index >= 0;
    	assert index < nombreInstructions();
    	
    	return bloc.get(index);
    }
    
    public int nombreInstructions() {
    	return bloc.size();
    }
    
    public boolean estRetourne() {
		boolean verification = false;
		
		Iterator<Instruction> parcoursBloc = bloc.iterator();
		
		while (!verification && parcoursBloc.hasNext()) {
			Instruction inst = parcoursBloc.next();
			
			verification = inst.estRetourne();
		}
		
		return verification;
    }
    
    @Override
    public void verifier() {
    	for (Instruction instr : bloc) {
    		instr.verifier();
    	}
    }
    
    @Override
    public String toMIPS() {
        StringBuilder mips = new StringBuilder(100);
        
    	for (Instruction instr : bloc) {
			mips.append(instr.toMIPS());
			mips.append("\n");
		}
		
        return mips.toString();
    }

    @Override
    public String toString() {
    	StringBuilder yal = new StringBuilder(20);
    	
    	for (Instruction instr : bloc) {
			yal.append(instr.toString());
			yal.append("\n");
		}
		
        return yal.toString();
    }
    
}
