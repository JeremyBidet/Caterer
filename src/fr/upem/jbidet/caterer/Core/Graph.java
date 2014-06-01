package fr.upem.jbidet.caterer.Core;

import java.util.ArrayList;
import java.util.List;

/**
 * Repr�sente un graphe poss�dant des sommets ({@link Vertex}) et des arcs ({@link Arc}).<br>
 * Un graphe poss�de en plus un id et un nom pour l'identifier.<br>
 * @author Jeremy
 * @version 1.0
 */
public class Graph {

	private static int instance = 0;
	private final int id;
	private String name;
	private List<Vertex> vertex;
	private List<Arc> arcs;
	
	/**
	 * Cr�e un graphe avec un nom, une liste de sommets et une liste d'arcs.<br>
	 * Les sommets sont reli�s entre eux par ses arcs.<br>
	 * @param name nom du graphe
	 * @param vertex liste des sommets du graphe
	 * @param arcs liste des arcs du graphe
	 */
	public Graph(String name, List<Vertex> vertex, List<Arc> arcs) {
		this.id = instance++;
		this.name = name;
		this.vertex = vertex;
		this.arcs = arcs;
	}
	
	/**
	 * Cr�e un graphe avec un nom.<br>
	 * @param name le nom du graphe
	 */
	public Graph(String name) {
		this.id = instance++;
		this.name = name;
		this.vertex = new ArrayList<Vertex>();
		this.arcs = new ArrayList<Arc>();
		Vertex.resetInstance();
		Arc.resetInstance();
	}
	
	/**
	 * Retourne l'identifiant du graphe.<br>
	 * @return <b>id</b> l'identifiant du graphe
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Retourne le nom du graphe.<br>
	 * @return <b>name</b> le nom du graphe
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Retourne la liste des sommets du graphe.<br>
	 * @return <b>vertex</b> les sommets du graphe
	 */
	public List<Vertex> getVertex() {
		return vertex;
	}
	
	/**
	 * Retourne un sommet du graphe suivant un index.<br>
	 * @param index l'index du sommet � retourner
	 * @return <b>vertex</b> le sommet � l'index <b>index</b>
	 */
	public Vertex getVertex(int index) {
		return vertex.get(index);
	}
	
	/**
	 * Retourne le nombre de sommet dans le graphe.<br>
	 * @return <b>size</b> le nombre de sommets
	 */
	public int getVertexQuantity() {
		return vertex.size();
	}
	
	/**
	 * Retourne la liste des ars du graphe.<br>
	 * @return <b>arcs</b> la liste des arcs
	 */
	public List<Arc> getArcs() {
		return arcs;
	}
	
	/**
	 * Retourne un arc du graphe suivant un index.<br>
	 * @param index l'index de l'arc � retourner
	 * @return <b>arc</b> l'arc � l'index <b>index</b>
	 */
	public Arc getArc(int index) {
		return arcs.get(index);
	}
	
	/**
	 * Retourne le nombre d'arcs dans le graphe.<br>
	 * @return <b>size</b> le nombre d'arcs
	 */
	public int getArcsQuantity() {
		return arcs.size();
	}
	
	/**
	 * D�finie la liste des sommets du graphe.<br>
	 * @param vertex la liste des sommets
	 */
	public void setVertex(List<Vertex> vertex) {
		this.vertex = vertex;
	}
	
	/**
	 * D�finie la liste des arcs du graphe.<br>
	 * @param arc la liste des arcs
	 */
	public void setArcs(List<Arc> arc) {
		this.arcs = arc;
	}
	
	/**
	 * Ajoute un sommet au graphe.<br>
	 * @param v le sommet � ajouter
	 * @return <b>true</b> si l'ajout s'est bien pass�, sinon <b>false</b>
	 */
	public boolean addVertex(Vertex vertex) {
		return this.vertex.add(vertex);
	}
	
	/**
	 * Ajoute des sommets au graphe.<br>
	 * @param vs les sommets � ajouter
	 * @return <b>true</b> si l'ajout s'est bien pass�, sinon <b>false</b>
	 */
	public boolean addVertex(Vertex... vertex) {
		boolean b = true;
		for(Vertex v : vertex) {
			b &= this.vertex.add(v);
		}
		return b;
	}
	
	/**
	 * Ajoute un arc au graphe.<br>
	 * @param a l'arc � ajouter
	 * @return <b>true</b> si l'ajout s'est bien pass�, sinon <b>false</b>
	 */
	public boolean addArc(Arc arc) {
		return arcs.add(arc);
	}
	
	/**
	 * Ajoute des arcs au graphe.<br>
	 * @param as les arcs � ajouter
	 * @return <b>true</b> si l'ajout s'est bien pass�, sinon <b>false</b>
	 */
	public boolean addArcs(Arc... arcs) {
		boolean b = true;
		for(Arc a : arcs) {
			b &= this.arcs.add(a);
		}
		return b;
	}
	
	@Override
	public boolean equals(Object o) {
		return o instanceof Graph
				&& ((Graph)o).id == this.id
				&& ((Graph)o).name.equals(this.name)
				&& ((Graph)o).vertex.equals(this.vertex)
				&& ((Graph)o).arcs.equals(this.arcs);
	}
	
	@Override
	public String toString() {
		return "Graph : {\n"
				+ "\tid : " + id + "\n"
				+ "\tname : " + name + "\n"
				+ "\tvertex : {\n" + vertex.toString() + "\t}\n"
				//+ "\tarcs : {\n" + arcs.toString() + "\t}\n"
				+ "}\n";
	}
}
