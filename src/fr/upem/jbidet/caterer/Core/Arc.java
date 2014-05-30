package fr.upem.jbidet.caterer.Core;

/**
 * Représente un arc, reliant deux sommets, avec un coût.
 * <br>
 * @author Jeremy
 * @version 1.0
 */
public class Arc {
	
	private Vertex vA;
	private Vertex vB;
	private int cost;
	
	/**
	 * Crée un arc reliant deux sommets, avec un coût.<br>
	 * @param vA le sommet A
	 * @param vB le sommet B
	 * @param cost le coût associé à cet arc.
	 */
	public Arc(Vertex vA, Vertex vB, int cost) {
		this.vA = vA;
		this.vB = vB;
		this.cost = cost;
	}
	
	/**
	 * Crée un arc avec un coût.<br>
	 * @param cost le coût de l'arc
	 */
	public Arc(int cost) {
		this.cost = cost;
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
	
	public void setVertexA(Vertex vA) {
		this.vA = vA;
	}
	
	public void setVertexB(Vertex vB) {
		this.vB = vB;
	}
	
	public void setVertex(Vertex vA, Vertex vB) {
		setVertexA(vA);
		setVertexB(vB);
	}
	
	/**
	 * Vérifie que l'objet passé est bien une instance de {@link Arc} et que ses champs sont bien égaux à cette instance.
	 */
	@Override
	public boolean equals(Object o) {
		return o instanceof Arc
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

}
