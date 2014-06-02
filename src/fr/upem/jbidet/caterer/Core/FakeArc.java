package fr.upem.jbidet.caterer.Core;

/**
 * Repr�sente un arc artificiel cr�e pour la solution initiale.<br>
 * Cette classe h�rite de tous les attributs et m�thodes de la classe {@link Arc}.<br>
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
	 * V�rifie que l'objet pass� est bien une instance de {@link FakeArc} et que ses champs sont bien �gaux � cette instance.
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
