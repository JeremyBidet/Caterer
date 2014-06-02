package fr.upem.jbidet.caterer;

import java.io.File;

import fr.upem.jbidet.caterer.Core.Graph;
import fr.upem.jbidet.caterer.Core.Vertex;
import fr.upem.jbidet.caterer.DAO.GraphParser;
import fr.upem.jbidet.caterer.Solver.Simplex;

/**
 * @author Melody
 * @author Jeremy
 * @version 1.0
 */
public class Main {

	public static void main(String[] args) {
		
		/** CREATION ARBRE INITIAL **/
		GraphParser gp = new GraphParser();
		File file = new File("tests/testregex");
		Graph graph;
		if((graph = gp.parseFile(file)) == null) {
			System.out.println("! This file is corrupted !");
			return;
		}
		/* v�rifie que le nombre de sommets d�clar�s correspond au nombre de sommets ins�r�s dans le graphe */
		int sum = 0;
		for(Vertex v : graph.getVertex()) {
			sum += v.getVertexWeight();
		}
		if(sum != 0) {
			System.out.println("Erreur ! Offre et demande non �gale !");
			return;
		}
		/** FIN CREATION **/
		
		/** RESOLUTION PROBLEME **/
		Graph solution = Simplex.solve(graph);
		System.out.println("Solution :\n" + solution);
		/** FIN RESOLUTION **/
		
	}

}
