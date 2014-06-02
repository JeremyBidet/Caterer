package fr.upem.jbidet.caterer.Core;

/**
 * Représente un arc, reliant deux sommets, avec un coût.<br>
 * @author Melody
 * @version 1.0
 */
public class Arc {
	
	private static int instance = 0;
	private final int id;
	private int flow;
	private Vertex vA;
	private Vertex vB;
	private int cost;
	
	/**
	 * Crée un arc reliant deux sommets, avec un coût et une valeur de flot.<br>
	 * @param vA le sommet A
	 * @param vB le sommet B
	 * @param cost le coût de l'arc
	 * @param flow le flot de l'arc en un instant
	 */
	public Arc(Vertex vA, Vertex vB, int cost, int flow) {
		this.id = instance++;
		this.vA = vA;
		this.vB = vB;
		this.cost = cost;
		this.flow = flow;
	}
	
	/**
	 * Crée un arc reliant deux sommets, avec un coût.<br>
	 * @param vA le sommet A
	 * @param vB le sommet B
	 * @param cost le coût associé à cet arc.
	 */
	public Arc(Vertex vA, Vertex vB, int cost) {
		this.id = instance++;
		this.vA = vA;
		this.vB = vB;
		this.cost = cost;
		this.flow = 0;
	}
	
	/**
	 * Crée un arc avec un coût.<br>
	 * @param cost le coût de l'arc
	 */
	public Arc(int cost) {
		this.id = instance++;
		this.cost = cost;
		this.flow = 0;
	}
	
	/**
	 * Retourne l'identifiant de l'arc.<br>
	 * @return <b>id</b> l'identifiant
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Retourne le coût associé à cet arc.<br>
	 * @return <b>cost</b> le coût de l'arc
	 */
	public int getCost() {
		return cost;
	}
	
	/**
	 * Retourne le sommet A.<br>
	 * @return <b>vA</b> le sommet A de cet arc
	 */
	public Vertex getVertexA() {
		return vA;
	}
	
	/**
	 * Retourne le sommet B.<br>
	 * @return <b>vB</b> le sommet B de cet arc
	 */
	public Vertex getVertexB() {
		return vB;
	}
	
	/**
	 * Retourne le flot de l'arc ; la quantité de "matières" transportée.<br>
	 * @return <b>flow</b> la quantité transportée
	 */
	public int getFlow() {
		return flow;
	}
	
	/**
	 * Définie le flot d'un arc.<br>
	 * @param flow le flot
	 */
	public void setFlow(int flow) {
		this.flow = flow;
	}
	
	/**
	 * Définie le coût de l'arc.<br>
	 * @param cost le coût de l'arc
	 */
	public void setCost(int cost) {
		this.cost = cost;
	}
	
	/**
	 * Définie le sommet A de l'arc.<br>
	 * @param vA le sommet A
	 */
	public void setVertexA(Vertex vA) {
		this.vA = vA;
	}
	
	/**
	 * Définie le sommet B de l'arc.<br>
	 * @param vB le sommet B
	 */
	public void setVertexB(Vertex vB) {
		this.vB = vB;
	}
	
	/**
	 * Remet à zéro le compteur d'instance.<br>
	 */
	protected static void resetInstance() {
		instance = 0;
	}
	
	/**
	 * Vérifie que l'objet passé est bien une instance de {@link Arc} et que ses champs sont bien égaux à cette instance.
	 */
	@Override
	public boolean equals(Object o) {
		return o instanceof Arc
				&& ((Arc)o).id == id
				&& ((Arc)o).cost == cost
				&& ((Arc)o).vA.equals(vA)
				&& ((Arc)o).vB.equals(vB);
	}
	
	/**
	 * Affiche correctement l'instance de la classe {@link Arc}
	 */
	@Override
	public String toString() {
		return "Arc : {\n"
				+ "\tcost : " + cost + "\n"
				+ "\tvertex A : " + vA.getId() + "\n"
				+ "\tvertex B : " + vB.getId() + "\n"
				+ "\t}\n";
	}
	
	public Arc clone() {
		return new Arc(vA, vB, cost);
	}

}
