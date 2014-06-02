package fr.upem.jbidet.caterer.Core;

/**
 * Représente un arc artificiel crée pour la solution initiale.<br>
 * Cette classe hérite de tous les attributs et méthodes de la classe {@link Arc}.<br>
 * @author Jeremy
 * @author Melody
 * @version 1.0
 */
public class FakeArc extends Arc {
	
	public FakeArc(Vertex vA, Vertex vB, int cost, int flow) {
		super(vA, vB, cost, flow);
	}
	
	public FakeArc(Vertex vA, Vertex vB, int cost) {
		super(vA, vB, cost);
	}

	public FakeArc(int cost) {
		super(cost);
	}
	
	/**
	 * Vérifie que l'objet passé est bien une instance de {@link FakeArc} et que ses champs sont bien égaux à cette instance.
	 */
	@Override
	public boolean equals(Object o) {
		return o instanceof FakeArc && super.equals(o);
	}
	
	/**
	 * Affiche correctement l'instance de la classe {@link FakeArc}
	 */
	@Override
	public String toString() {
		return "Fake" + super.toString();
	}
	
	public Arc clone() {
		return new FakeArc(super.getVertexA(), super.getVertexB(), super.getCost());
	}

}
