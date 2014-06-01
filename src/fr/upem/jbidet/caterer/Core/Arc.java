package fr.upem.jbidet.caterer.Core;

/**
 * Repr�sente un arc, reliant deux sommets, avec un co�t.
 * <br>
 * @author Jeremy
 * @version 1.0
 */
public class Arc {
	
	private static int instance = 0;
	private int id;
	private Vertex vA;
	private Vertex vB;
	private int cost;
	
	/**
	 * Cr�e un arc reliant deux sommets, avec un co�t.<br>
	 * @param vA le sommet A
	 * @param vB le sommet B
	 * @param cost le co�t associ� � cet arc.
	 */
	public Arc(Vertex vA, Vertex vB, int cost) {
		this.id = instance++;
		this.vA = vA;
		this.vB = vB;
		this.cost = cost;
	}
	
	/**
	 * Cr�e un arc avec un co�t.<br>
	 * @param cost le co�t de l'arc
	 */
	public Arc(int cost) {
		this.id = instance++;
		this.cost = cost;
	}
	
	/**
	 * Retourne l'identifiant de l'arc.<br>
	 * @return <b>id</b> l'identifiant
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Retourne le co�t associ� � cet arc.<br>
	 * @return <b>cost</b> le co�t de l'arc
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
	 * Remet � z�ro le compteur d'instance.<br>
	 */
	protected static void resetInstance() {
		instance = 0;
	}
	
	/**
	 * V�rifie que l'objet pass� est bien une instance de {@link Arc} et que ses champs sont bien �gaux � cette instance.
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

}
