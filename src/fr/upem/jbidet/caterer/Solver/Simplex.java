package fr.upem.jbidet.caterer.Solver;

import java.util.ArrayList;
import java.util.List;

import fr.upem.jbidet.caterer.Core.Arc;
import fr.upem.jbidet.caterer.Core.FakeArc;
import fr.upem.jbidet.caterer.Core.Graph;
import fr.upem.jbidet.caterer.Core.Vertex;

public class Simplex {

	private static Graph solution;
	
	public static enum Response {
		OK,
		PERHAPS,
		NO
	};
	
	private Simplex() {
	}
	
	/**
	 * Lance l'algorithme du simplex (r�seau de transpiort) 
	 *  pour tenter de trouver une solution optimale au probl�me (graphe).<br>
	 * L'algorithme consiste � :
	 * <ol>
	 *  <li>D�terminer une solution initiale en modifiant le graphe initial
	 *   et en appliquant l'agorithme du simplex une premi�re fois.</li>
	 *  <li>V�rifier si cette solution existe. En testant la pr�sence d'arcs artificiels apr�s algorithme.</li>
	 *  <li>Restaurer la solution initiale trouv�e pour faire correspondre les co�ts des arcs
	 *   avec les co�ts du graphe initial.</li>
	 *  <li>Appliquer l'algorithme du simplex une deuxi�me fois pour trouver la solution optimale.</li>
	 * </ol>
	 * @param graph le graphe d'origine
	 * @return <b>solution</b> la solution si elle existe
	 */
	public static Graph solve(Graph graph) {
		solution = new Graph();
		getInitialSolution(graph);
		try {
			checkedInitialSolution(graph);
		} catch (GraphException e) {
			e.printStackTrace();
		}
		getOptimalSolution(graph);
		return solution;
	}
	
	/**
	 * D�termine une solution initiale au graphe.<br>
	 * @param graph le grpahe d'origine
	 */
	private static void getInitialSolution(Graph graph) {
		/* prend un sommet arbitraire */
		Vertex w = graph.getVertex(graph.getVertexQuantity()/2);
		/* copie le graphe initial pour trouver une solution initiale */
		solution = graph.clone();
		w = solution.getVertex(w.getId());
		/* les arcs r�els ont un co�t de z�ro */
		for(Arc a : solution.getArcs()) {
			a.setCost(0);
		}
		/* on ajoute des arcs artificiels pour chaque arc qui ne remplit pas la condition : 
		 * toutes les sources vont vers w et w va vers tous les puits */
		for(Vertex v : solution.getVertex()) {
			if(v.equals(w)) {
				continue;
			}
			if(v.getVertexWeight() < 0) {
				boolean add = true;
				for(Arc a : v.getArcs()) {
					if(a.getVertexB().equals(w)) {
						add &= false;
						a.setFlow(-v.getVertexWeight());
					} else {
						v.removeArc(a); // supprime de la liste du sommet, pas du graphe
					}
				}
				if(add) {
					Arc a = new FakeArc(v, w, 1, -v.getVertexWeight());
					v.addArc(a);
					solution.addArc(a);
				}
			}
			if(v.getVertexWeight() >= 0) {
				boolean add = true;
				for(Arc a : v.getArcs()) {
					if(a.getVertexA().equals(w)) {
						add &= false;
						a.setFlow(v.getVertexWeight());
					} else {
						v.removeArc(a); // supprime de la liste du sommet, pas du graphe
					}
				}
				if(add) {
					Arc a = new FakeArc(w, v, 1, v.getVertexWeight());
					v.addArc(a);
					solution.addArc(a);
				}
			}
		}
		updateCost();
		simplex();
	}
	
	/**
	 * V�rifie l'existance d'une solution initiale pour ce graphe.<br>
	 * @param graph le graphe d'origine
	 * @throws GraphException le graphe n'accepte, ou peut ne pas accepter de solutions
	 */
	private static void checkedInitialSolution(Graph graph) throws GraphException {
		boolean tmp = true;
		for(Arc a : graph.getArcs()) {
			if(a instanceof FakeArc && a.getCost() > 0) {
				throw new GraphException("This graph DOES NOT ACCEPT any solutions !");
			}
			if(a instanceof FakeArc && a.getCost() == 0) {
				tmp &= false;
			}
		}
		if(tmp) {
			return;
		} else {
			throw new GraphException("This graph MAY ACCEPT a solution !");
		}
	}
	
	/**
	 * Retourne la solution optimale, si elle existe.<br>
	 * @param graph le graphe d'origine
	 */
	private static void getOptimalSolution(Graph graph) {
		restore(graph);
		simplex();
	}
	
	/**
	 * Met � jour les co�t de chaque sommet dans la solution
	 */
	private static void updateCost() {
		for(Vertex v : solution.getVertex()) {
			if(v.getVertexWeight() < 0) {
				updateCostRecursive(v);
				return;
			}
		}
	}
	private static void updateCostRecursive(Vertex v) {
		for(Arc a : v.getArcs()) {
			if(a.getVertexB().getCost() == 0) {
				if(a.getVertexA().equals(v)) {
					a.getVertexB().setCost(v.getCost() + a.getCost());
				} else {
					a.getVertexB().setCost(v.getCost() - a.getCost());
				}
				updateCostRecursive(a.getVertexB());
			}
		}
	}
	
	/**
	 * D�ploie l'algorithme du simplex (r�seau de transport) sur le graph solution.<br>
	 * @param graph le graphe d'origine
	 */
	private static boolean simplex() {
		List<Arc> f_arc = new ArrayList<Arc>();
		List<Arc> e_arc = new ArrayList<Arc>();
		for(Vertex v : solution.getVertex()) {
			f_arc.addAll(v.getArcs());
		}
		for(Arc a : solution.getArcs()) {
			if(!f_arc.contains(a)) {
				e_arc.add(a);
			}
		}
		
		/* on cherche l'arc e plus interessant */
		Arc e = new Arc(-1);
		for(Arc a : e_arc) {
			for(Arc aa : a.getVertexB().getArcs()) {
				if	(	a.getVertexA().getCost() + a.getCost() < e.getCost() 
					|| (a.getVertexA().getCost() + a.getCost() < aa.getCost() 
							&& aa.getVertexB().equals(a.getVertexB())
						)
					) {
					e = a;
					a.getVertexA().addArc(a);
					a.getVertexB().addArc(a);
				}
			}
		}
		if(e.getCost() == -1) { /* pas d'arc e plus interessant, solution optimale trouv�e */
			return true;
		}
		/* on cherche l'arc f de sens contraire le moins interessant (co�t le plus �lev�) */
		findF(e); //TODO: findF is to do
		updateCost();
		
		return false | simplex();
	}
	
	/**
	 * Si une solution arbre est trouv�e, alors on r�tablit les co�ts de chaque arc du graphe � leur origine.<br>
	 * @param graph le graphe d'origine
	 */
	private static void restore(Graph graph) {
		for(Arc a : solution.getArcs()) {
			a.setCost(graph.getArc(a.getId()).getCost());
		}
	}
	
	private static void findF(Arc e) {
		// TODO: cr�e la liste des arcs du cycle,
		List<Arc> cycle = new ArrayList<Arc>();
		// ... //
		
		// puis cherche dans ce cycle l'arc en sens inverse ayant le plus fort co�t.
		Arc f = e;
		int max = Integer.MIN_VALUE;
		Vertex vA = e.getVertexB();
		for(Arc a : cycle) {
			if(a.getVertexB().equals(vA) && a.getCost() > max) {
				f = a; // plus grand co�t dans le sens contraire du cycle
			}
		}
		int f_flow = f.getFlow();
		f.getVertexA().getArcs().remove(f);
		f.getVertexB().getArcs().remove(f);
		cycle.remove(f);
		updateFlow(cycle, e, f_flow);
	}
	

	private static void updateFlow(List<Arc> cycle, Arc e, int flow) {
		Vertex vA = e.getVertexB();
		for(Arc a : cycle) {
			if(a.getVertexB().equals(vA)) {
				a.setFlow(a.getFlow() - flow);
			} else {
				a.setFlow(a.getFlow() + flow);
			}
		}
	}
	
}
