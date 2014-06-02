package fr.upem.jbidet.caterer.Solver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	 * Lance l'algorithme du simplex (réseau de transpiort) 
	 *  pour tenter de trouver une solution optimale au problème (graphe).<br>
	 * L'algorithme consiste à :
	 * <ol>
	 *  <li>Déterminer une solution initiale en modifiant le graphe initial
	 *   et en appliquant l'agorithme du simplex une première fois.</li>
	 *  <li>Vérifier si cette solution existe. En testant la présence d'arcs artificiels après algorithme.</li>
	 *  <li>Restaurer la solution initiale trouvée pour faire correspondre les coûts des arcs
	 *   avec les coûts du graphe initial.</li>
	 *  <li>Appliquer l'algorithme du simplex une deuxième fois pour trouver la solution optimale.</li>
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
	 * Détermine une solution initiale au graphe.<br>
	 * @param graph le grpahe d'origine
	 */
	private static void getInitialSolution(Graph graph) {
		/* prend un sommet arbitraire */
		Vertex w = graph.getVertex(graph.getVertexQuantity()/2);
		/* copie le graphe initial pour trouver une solution initiale */
		solution = graph.clone();
		w = solution.getVertex(w.getId());
		/* les arcs réels ont un coût de zéro */
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
	 * Vérifie l'existance d'une solution initiale pour ce graphe.<br>
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
	 * Met à jour les coût de chaque sommet dans la solution
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
	 * Déploie l'algorithme du simplex (réseau de transport) sur le graph solution.<br>
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
		if(e.getCost() == -1) { /* pas d'arc e plus interessant, solution optimale trouvée */
			return true;
		}
		/* on cherche l'arc f de sens contraire le moins interessant (coût le plus élevé) */
		findF(e); //TODO: findF is to do
		updateCost();
		
		return false | simplex();
	}
	
	/**
	 * Si une solution arbre est trouvée, alors on rétablit les coûts de chaque arc du graphe à leur origine.<br>
	 * @param graph le graphe d'origine
	 */
	private static void restore(Graph graph) {
		for(Arc a : solution.getArcs()) {
			a.setCost(graph.getArc(a.getId()).getCost());
		}
	}
	
	private static void findF(Arc e) {
		// TODO: crée la liste des arcs du cycle, en supprimant de la copie tous les arcs qui ne sont pas dans le cycle
		List<Vertex> copy = new ArrayList<Vertex>(solution.getVertex());
		List<Arc> cycle = new ArrayList<Arc>();
		Map<Integer, Vertex> map = new HashMap<Integer, Vertex>();
		copy = removeLeaf(e.getVertexB(), e.getVertexA(), copy, map); // supprime les feuilles et colore le graphe
		copy = extractCycle(e.getVertexB(), e.getVertexA(), copy, map); // supprime les sommets qui ne sont pas dans le cycle
		cycle = fillCycle(e, e.getVertexA(), cycle, copy); // remplit le cycle
		
		// puis cherche dans ce cycle l'arc en sens inverse ayant le plus fort coût.
		Arc f = e;
		int max = Integer.MIN_VALUE;
		Vertex vA = e.getVertexB();
		for(Arc a : cycle) {
			if(a.getVertexB().equals(vA) && a.getCost() > max) {
				f = a; // plus grand coût dans le sens contraire du cycle
			}
		}
		int f_flow = f.getFlow();
		f.getVertexA().getArcs().remove(f);
		f.getVertexB().getArcs().remove(f);
		cycle.remove(f);
		updateFlow(cycle, e, f_flow);
	}
	
	private static List<Vertex> removeLeaf(Vertex v, Vertex l, List<Vertex> copy, Map<Integer, Vertex> map) {
		if(v.getArcs().size() == 1) { // feuille du graphe
			copy.remove(v); // le sommet ne fait pas partie du cycle
			map.put(v.hashCode(), v); // on ajoute le sommet à la liste des sommets déjà visités
		}
		/* cherche et supprime les feuilles */
		for(Arc a : v.getArcs()) {
			if(a.getVertexA().equals(l) || a.getVertexB().equals(l)) {
				continue;
			}
			if(a.getVertexA().equals(v)) {
				if(!map.containsKey(v.hashCode())) {
					copy = removeLeaf(a.getVertexB(), v, copy, map);
				}
			} else {
				if(!map.containsKey(v.hashCode())) {
					copy = removeLeaf(a.getVertexA(), v, copy, map);
				}
			}
			if(!map.containsKey(v.hashCode())) {
				map.put(v.hashCode(), v);
			}
		}
		return copy;
	}
	
	private static List<Vertex> extractCycle(Vertex v, Vertex l, List<Vertex> copy, Map<Integer, Vertex> map) {
		/* cherche si le sommet possède un arc vers un sommet déjà visité non supprimé */
		for(Arc a : v.getArcs()) {
			boolean pass = false;
			if(a.getVertexA().equals(l) || a.getVertexB().equals(l)) {
				continue;
			}
			// si le sommet opposé de l'arc existe toujours et est visité alors v fait partie du cycle
			if(a.getVertexA().equals(v)) {
				if(map.containsKey(a.getVertexA().hashCode()) && copy.contains(a.getVertexA())) {
					copy = extractCycle(a.getVertexB(), v, copy, map);
					pass = true; // sommet du cycle confirmé, plus besoin de chercher dans les autres arcs de ce sommet
				}
			} else if(a.getVertexB().equals(v)) {
				if(map.containsKey(a.getVertexB().hashCode()) && copy.contains(a.getVertexB())) {
					copy = extractCycle(a.getVertexA(), v, copy, map);
					pass = true; // sommet du cycle confirmé, plus besoin de chercher dans les autres arcs de ce sommet
				}
			} else {
				copy.remove(v);
			}
			if(pass) {
				break;
			}
		}
		return copy;
	}
	
	private static List<Arc> fillCycle(Arc arc, Vertex e, List<Arc> cycle, List<Vertex> copy) {
		if(e.equals(arc)) {
			return cycle;
		}
		for(Vertex v : copy) {
			if(    (arc.getVertexB().equals(v) && arc.getVertexA().equals(e))
				|| (arc.getVertexA().equals(v) && arc.getVertexB().equals(e))) {
				for(Arc a : v.getArcs()) {
					if(a.getVertexA().equals(v)) {
						for(Vertex t : copy) {
							if(a.getVertexB().equals(t)) {
								cycle.add(a);
								cycle = fillCycle(a, a.getVertexA(), cycle, copy);
							}
						}
					} else {
						for(Vertex t : copy) {
							if(a.getVertexA().equals(t)) {
								cycle.add(a);
								cycle = fillCycle(a, a.getVertexB(), cycle, copy);
							}
						}
					}
				}
			}
		}
		return cycle; // should never called
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
