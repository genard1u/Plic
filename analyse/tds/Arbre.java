package yal.analyse.tds;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import yal.analyse.tds.entree.Entree;
import yal.analyse.tds.symbole.Symbole;
import yal.exceptions.AnalyseSemantiqueException;

public class Arbre {
	
    private int numeroRegion;
	
	private HashMap<Entree, Symbole> table;
	
	private Arbre parent;
	private HashMap<Integer, Arbre> fils;
	
    
	public Arbre(int region) {
		numeroRegion = region;
		parent = null;
		
		fils = new HashMap<Integer, Arbre>();
		table = new HashMap<Entree, Symbole>();
	}
	
	public Arbre(int region, Arbre p) {
		numeroRegion = region;
		parent = p;
		
		fils = new HashMap<Integer, Arbre>();
		table = new HashMap<Entree, Symbole>();
	}
	
	public void ajouter(Entree e, Symbole s, int noLigne) {
		assert e != null;
		
		if (table.containsKey(e)) {
			throw new AnalyseSemantiqueException(noLigne, "redéclaration de `" + e.getIdf() + "`");
		}
		
		table.put(e, s);
	}
	
	public void ajouterFils(Arbre f) {
		Arbre prev = fils.put(f.numeroRegion(), f);
		
		assert prev == null;
	}
	
	public Arbre recupererFils(int numeroRegion) {
		Arbre fils = this.fils.get(numeroRegion);
		
		assert fils != null;
		
		return fils;
	}
	
	public Symbole identifier(Entree e) {
		Symbole s = table.get(e);
		
		if (s == null) {
			if (parent != null) {
				s = parent.identifier(e);
			}
		}
		
		return s;
	}
	
	public Arbre getParent() {
		return parent;
	}
	
	public HashMap<Entree, Symbole> getTable() {
		return table;
	}
	
	public int numeroRegion() {
		return numeroRegion;
	}
	
	public int nbVariables() {
		int nombreVariables = 0;
		Set<Entree> cles = table.keySet();
		Iterator<Entree> parcours = cles.iterator();
		
		while (parcours.hasNext()){
			   Entree e = parcours.next(); 
			   Symbole s = table.get(e); 
			   
			   if (s.pourVariable()) {
				   nombreVariables ++;
			   }
	    }
		
		return nombreVariables;
	}
	
	public int nbParametres() {
		int nombreParametres = 0;
		Set<Entree> cles = table.keySet();
		Iterator<Entree> parcours = cles.iterator();
		
		while (parcours.hasNext()){
			   Entree e = parcours.next(); 
			   Symbole s = table.get(e); 
			   
			   if (s.pourParametre()) {
				   nombreParametres ++;
			   }
	    }
		
		return nombreParametres;
	}
	
	public int nbFils() {
		return fils.size();
	}
	
	public int tailleZoneDesVariables() {
		int tailleZone = 0;
		
		for (Entry<Entree, Symbole> map : table.entrySet()) {
		    Symbole s = map.getValue();
		    
		    if (s.pourVariable()) {
		    	tailleZone += s.getEspace();
		    }
		}
		
		return tailleZone;
    }
	
	public int tailleZoneDesParametres() {
        int tailleZone = 0;
		
		for (Entry<Entree, Symbole> map : table.entrySet()) {
		    Symbole s = map.getValue();
		    
		    if (s.pourParametre()) {
		    	tailleZone += s.getEspace();
		    }
		}
		
		return tailleZone;
	}
	
	@Override
	public String toString() {
		return table.toString();
	}
	
}
