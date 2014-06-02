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
	private int vertex_weight;
	private int cost;
	private List<Arc> arcs;
	
	/**
	 * Crée un sommet avec un vecteur de poids et une liste d'arcs partant de ce dernier.<br>
	 * @param vertex_weight le vecteur de poids
	 * @param arcs la liste d'arcs
	 */
	public Vertex(int vertex_weight, List<Arc> arcs) {
		this.id = instance++;
		this.vertex_weight = vertex_weight;
		this.arcs = arcs;
	}
	
	/**
	 * Crée un sommet avec un vecteur de poids et une liste d'arcs (passés en chaine) partant de ce dernier.<br>
	 * @param vertex_weight le vecteur de poids
	 * @param arcs la liste d'arcs
	 */
	public Vertex(int vertex_weight, Arc... arcs) {
		this.id = instance++;
		this.vertex_weight = vertex_weight;
		this.arcs = new ArrayList<Arc>();
		for(Arc a : arcs) {
			this.arcs.add(a);
		}
	}
	
	public Vertex(int vertex_weight) {
		this.id = instance++;
		this.vertex_weight = vertex_weight;
		this.arcs = new ArrayList<Arc>();
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
	public int getVertexWeight() {
		return vertex_weight;
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
	
	public int getCost() {
		return cost;
	}
	
	public void setCost(int cost) {
		this.cost = cost;
	}
	
	/**
	 * Définie la liste des arcs de ce sommet.<br>
	 * @param arcs la liste des arcs
	 */
	public void setArcs(List<Arc> arcs) {
		this.arcs = arcs;
	}
	
	/**
	 * Ajoute un arc à la liste des arcs du sommet.<br>
	 * @param a l'arc à ajouter
	 * @return <b>true</b> si l'ajout s'est bien passé, sinon <b>false</b>
	 */
	public boolean addArc(Arc a) {
		return arcs.add(a);
	}
	
	/**
	 * Ajoute des arcs au sommet.<br>
	 * @param as les arcs à ajouter
	 * @return <b>true</b> si l'ajout s'est bien passé, sinon <b>false</b>
	 */
	public boolean addArcs(Arc... as) {
		boolean b = true;
		for(Arc a : as) {
			b &= arcs.add(a);
		}
		return b;
	}
	
	/**
	 * Ajoute une liste d'arcs au sommet.<br>
	 * @param as la liste d'arcs à ajouter
	 * @return <b>true</b> si l'ajout s'est bien passé, sinon <b>false</b>
	 */
	public boolean addArcs(List<Arc> as) {
		return arcs.addAll(as);
	}
	
	public boolean removeArc(Arc a) {
			return arcs.remove(a);
	}
	
	/**
	 * Remet à zéro le compteur d'instance.<br>
	 */
	protected static void resetInstance() {
		instance = 0;
	}
	
	/**
	 * Vérifie que l'objet passé est bien une instance de {@link Vertex} dont les champs sont égaux à cette instance.
	 */
	@Override
	public boolean equals(Object o) {
		return o instanceof Vertex
				&& ((Vertex)o).id == id
				&& ((Vertex)o).vertex_weight == this.vertex_weight
				&& ((Vertex)o).arcs.equals(this.arcs);
	}
	
	/**
	 * Affiche correctement l'intance de la classe {@link Vertex}.<br>
	 */
	@Override
	public String toString() {
		return "Vertex : {\n"
				+ "\tid : " + id + "\n"
				+ "\tvertex_weight : " + vertex_weight + "\n"
				+ "\tarcs : {\n" + arcs.toString() + "\t}\n"
				+"}\n";
	}
	
	public Vertex clone() {
		return new Vertex(vertex_weight, arcs);
	}

}
