package fr.upem.jbidet.caterer.Core;

import java.util.ArrayList;
import java.util.List;

/**
 * Représente un sommet dans un graphe. Possède un vecteur de poids et une liste d'arcs.<br>
 * @author Jeremy
 * @version 1.0
 * @see Arc
 */
public class Vertex {
	
	private static int instance = 0;
	private final int id; 
	private int vector_weight;
	private List<Arc> arcs;
	
	/**
	 * Crée un sommet avec un vecteur de poids et une liste d'arcs partant de ce dernier.<br>
	 * @param vector_weight le vecteur de poids
	 * @param arcs la liste d'arcs
	 */
	public Vertex(int vector_weight, List<Arc> arcs) {
		this.id = instance++;
		this.vector_weight = vector_weight;
		this.arcs = arcs;
	}
	
	/**
	 * Crée un sommet avec un vecteur de poids et une liste d'arcs (passés en chaine) partant de ce dernier.<br>
	 * @param vector_weight le vecteur de poids
	 * @param arcs la liste d'arcs
	 */
	public Vertex(int vector_weight, Arc... arcs) {
		this.id = instance++;
		this.vector_weight = vector_weight;
		this.arcs = new ArrayList<Arc>();
		for(Arc a : arcs) {
			this.arcs.add(a);
		}
	}
	
	/**
	 * Retourne l'identifiant du sommet.<br>
	 * @return <b>id</b> l'identifiant du sommet
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Retourne le vecteur de poids de ce sommet.<br>
	 * @return <b>vector_weight</b> le vecteur de poids
	 */
	public int getVectorWeight() {
		return vector_weight;
	}
	
	/**
	 * Retourne la liste d'arcs de ce sommet.<br>
	 * @return <b>arcs</b> la liste d'arcs
	 */
	public List<Arc> getArcs() {
		return arcs;
	}
	
	/**
	 * Retourne le <i>i-ème</i> arc partant de ce sommet.<br>
	 * @param index l'index de l'arc à retourner
	 * @return <b>arc</b> l'arc à l'index <b>index</b>
	 */
	public Arc getArc(int index) {
		return arcs.get(index);
	}
	
	/**
	 * Ajoute un arc au sommet.<br>
	 * @param a l'arc à ajouter
	 * @return <b>true</b> si l'ajout s'est bien passé, sinon <b>false</b>
	 */
	public boolean addArc(Arc a) {
		return arcs.add(a);
	}
	
	public boolean addArcs(Arc... as) {
		boolean b = true;
		for(Arc a : as) {
			b &= arcs.add(a);
		}
		return b;
	}
	
	/**
	 * Vérifie que l'objet passé est bien une instance de {@link Vertex} dont les champs sont égaux à cette instance.
	 */
	@Override
	public boolean equals(Object o) {
		return o instanceof Vertex
				&& ((Vertex)o).vector_weight == this.vector_weight
				&& ((Vertex)o).arcs.equals(this.arcs);
	}
	
	/**
	 * Affiche correctement l'intance de la classe {@link Vertex}.<br>
	 */
	@Override
	public String toString() {
		return "Vertex : {\n"
				+ "\tid : " + id + "\n"
				+ "\tvector_weight : " + vector_weight + "\n"
				+ "\tarcs : {\n" + arcs.toString() + "\t}\n"
				+"}\n";
	}

}
