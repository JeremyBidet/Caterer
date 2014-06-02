package fr.upem.jbidet.caterer.Solver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fr.upem.jbidet.caterer.Core.Arc;
import fr.upem.jbidet.caterer.Core.FakeArc;
import fr.upem.jbidet.caterer.Core.Graph;
import fr.upem.jbidet.caterer.Core.Vertex;

public class Simplex {
	
	private static Graph solution;
	
	static class Struct {
		int arcno;
		Vertex v;
		Struct(int arcno, Vertex v) { this.arcno = arcno; this.v = v; }
	}
	
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
				List<Arc> toRemove = new ArrayList<Arc>(v.getArcs().size());
				for(Arc a : v.getArcs()) {
					if(a.getVertexB().equals(w)) {
						add &= false;
						a.setFlow(-v.getVertexWeight());
					} else {
						toRemove.add(a); // ajoute à la liste de suppression
					}
				}
				for(Arc a : toRemove) {
					v.removeArc(a); // supprime de la liste du sommet, pas du graphe
				}
				if(add) {
					Arc a = new FakeArc(v, w, 1, -v.getVertexWeight());
					v.addArc(a);
					w.addArc(a);
					solution.addArc(a);
				}
			}
			if(v.getVertexWeight() >= 0) {
				boolean add = true;
				List<Arc> toRemove = new ArrayList<Arc>(v.getArcs().size());
				for(Arc a : v.getArcs()) {
					if(a.getVertexA().equals(w)) {
						add &= false;
						a.setFlow(v.getVertexWeight());
					} else {
						toRemove.add(a); // ajoute à la liste de suppression
					}
				}
				for(Arc a : toRemove) {
					v.removeArc(a); // supprime de la liste du sommet, pas du graphe
				}
				if(add) {
					Arc a = new FakeArc(w, v, 1, v.getVertexWeight());
					v.addArc(a);
					w.addArc(a);
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
	 * Met à jour les coût de chaque sommet à partir d'un sommet source arbitraire dans la solution
	 */
	private static void updateCost() {
		for(Vertex v : solution.getVertex()) {
			if(v.getVertexWeight() < 0) {
				updateCostRecursive(v, null);
				return;
			}
		}
	}
	private static void updateCostRecursive(Vertex v, Vertex l) {
		for(Arc a : v.getArcs()) {
			if(!a.getVertexA().equals(l) && !a.getVertexB().equals(l)) {
				if(a.getVertexA().equals(v)) {
					a.getVertexB().setCost(v.getCost() + a.getCost());
					updateCostRecursive(a.getVertexB(), a.getVertexA());
				} else {
					a.getVertexA().setCost(v.getCost() - a.getCost());
					updateCostRecursive(a.getVertexA(), a.getVertexB());
				}
			}
		}
	}
	
	/**
	 * Déploie l'algorithme du simplex (réseau de transport) sur le graph solution.<br>
	 * @param graph le graphe d'origine
	 */
	private static boolean simplex() {
		Set<Arc> tmp_f_arc = new HashSet<Arc>();
		List<Arc> e_arc = new ArrayList<Arc>();
		for(Vertex v : solution.getVertex()) {
			tmp_f_arc.addAll(v.getArcs());
		}
		List<Arc> f_arc = new ArrayList<Arc>(tmp_f_arc);
		for(Arc a : solution.getArcs()) {
			if(!f_arc.contains(a)) {
				e_arc.add(a);
			}
		}
		
		/* on cherche l'arc le plus interessant */
		Arc e = new Arc(Integer.MAX_VALUE);
		for(Arc a : e_arc) {
			for(Arc aa : a.getVertexB().getArcs()) {
				if	(	a.getVertexA().getCost() + a.getCost() < e.getCost() // si l'arc e (=a) testé est mieux que le précédent e
					&& (a.getVertexA().getCost() + a.getCost() < aa.getCost() // ou si l'arc e (=a) testé est mieux que l'un des arcs du sommet de destination
							&& aa.getVertexB().equals(a.getVertexB()) // et si cet arc est entrant dans le sommet de destination (sens contraire)
						)
					) {
					e = a;
				}
			}
		}
		if(e.getCost() == Integer.MAX_VALUE) { /* pas d'arc e plus interessant, solution optimale trouvée */
			return true;
		}
		e.getVertexA().addArc(e);
		e.getVertexB().addArc(e);
		/* on cherche l'arc f de sens contraire le moins interessant (coût le plus élevé) */
		findF(e);
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
		// crée la liste des arcs du cycle, en supprimant de la copie tous les arcs qui ne sont pas dans le cycle
		List<Vertex> tmp_cycle = new ArrayList<Vertex>(solution.getVertex());
		List<Arc> cycle = new ArrayList<Arc>(tmp_cycle.size());
		
		Set<Arc> visited = new HashSet<Arc>();
		Map<Integer, Struct> struct = new HashMap<Integer, Struct>();
		for(Vertex v : solution.getVertex()) {
			struct.put(v.getId(), new Struct(v.getArcs().size(), v));
		}
		findCycle(e.getVertexB(), e.getVertexA(), tmp_cycle, visited, struct);
		sortCycle(e, e.getVertexB(), cycle, tmp_cycle);
		
		// puis cherche dans ce cycle l'arc en sens inverse ayant le plus fort coût.
		Arc f = e;
		int max = Integer.MIN_VALUE;
		Vertex vA = e.getVertexB();
		for(Arc a : cycle) {
			if(a.getVertexB().equals(vA) && a.getCost() > max) {
				max = a.getCost();
				f = a; // plus grand coût dans le sens contraire du cycle
			}
			vA = a.getVertexA();
		}
		
		int f_flow = f.getFlow();
		f.getVertexA().getArcs().remove(f);
		f.getVertexB().getArcs().remove(f);
		solution.getArcs().remove(f);
		updateFlow(cycle, e.getVertexB(), f_flow);
		cycle.remove(f); // doit être fait après pour mettre à jour les flots correctement
	}
	
	private static void findCycle(Vertex b, Vertex a, List<Vertex> cycle, Set<Arc> visited, Map<Integer, Struct> struct) {
		for(Arc arc : b.getArcs()) {
			if(arc.getVertexA().equals(a) || arc.getVertexB().equals(a)) {
				continue; // arc d'où l'on vient
			}
			if(visited.contains(arc)) {
				continue; // arc déjà visité avant
			}
			visited.add(arc);
			if(arc.getVertexA().equals(b)) { // l'arc sort de b
				if(struct.get(arc.getVertexB().getId()).arcno == 1) { // si le sommet suivant est une feuille
					cycle.remove(arc.getVertexB());	// on le supprime de la liste
					struct.get(b.getId()).arcno--; // et on décrémente le nombre d'arc du sommet actuel
				} else {
					findCycle(arc.getVertexB(), b, cycle, visited, struct); // on va au sommet suivant
					if(struct.get(arc.getVertexB().getId()).arcno == 1) { // après avoir réduit le sommet suivant on test s'il
						cycle.remove(arc.getVertexB()); // n'est pas devenue une feuille à son tour, on le supprime dans ce cas
						struct.get(b.getId()).arcno--; // et on décrémente le nombre d'arc du sommet actuel
					}
				}
			} else { // l'arc entre dans b
				if(struct.get(arc.getVertexA().getId()).arcno == 1) { // si le sommet suivant est une feuille
					cycle.remove(arc.getVertexA());	// on le supprime de la liste
					struct.get(b.getId()).arcno--; // et on décrémente le nombre d'arc du sommet actuel
				} else {
					findCycle(arc.getVertexA(), b, cycle, visited, struct);
					if(struct.get(arc.getVertexA().getId()).arcno == 1) { // après avoir réduit le sommet suivant on test s'il
						cycle.remove(arc.getVertexA()); // n'est pas devenue une feuille à son tour, on le supprime dans ce cas
						struct.get(b.getId()).arcno--; // et on décrémente le nombre d'arc du sommet actuel
					}
				}
			}
		}
	}
	
	private static void sortCycle(Arc arc, Vertex e, List<Arc> cycle, List<Vertex> tmp_cycle) {
		if(cycle.size() == tmp_cycle.size()) {
			return; // il y autant d'arcs dans le cycle que de sommets
		}
		for(Arc a : e.getArcs()) {
			if(a.getVertexA().equals(e)) {
				if(tmp_cycle.contains(a.getVertexB())) {
					if(!cycle.contains(a)) {
						cycle.add(a);
						sortCycle(a, a.getVertexB(), cycle, tmp_cycle);
					}
				}
			} else {
				if(tmp_cycle.contains(a.getVertexA())) {
					if(!cycle.contains(a)) {
						cycle.add(a);
						sortCycle(a, a.getVertexA(), cycle, tmp_cycle);
					}
				}
			}
		}
	}
	
	private static void updateFlow(List<Arc> cycle, Vertex v, int flow) {
		Vertex vA = v;
		for(Arc a : cycle) {
			if(a.getVertexB().equals(vA)) {
				a.setFlow(a.getFlow() - flow);
				vA = a.getVertexA();
			} else {
				a.setFlow(a.getFlow() + flow);
				vA = a.getVertexB();
			}
		}
	}
	
}
