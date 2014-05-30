package fr.upem.jbidet.caterer;

import fr.upem.jbidet.caterer.Core.Arc;
import fr.upem.jbidet.caterer.Core.Graph;
import fr.upem.jbidet.caterer.Core.Vertex;

/**
 * @author Melody
 * @version 1.0
 */
public class Main {

	public static void main(String[] args) {
		
		System.out.println("Hello World");
		
		/** INIT GRAPH **/
		/* crée le graphe avec son nom */
		Graph graph = new Graph("Autoroute");
		/* crée les arcs avec leut coût */
		Arc a1 = new Arc(5);
		Arc a2 = new Arc(6);
		Arc a3 = new Arc(3);
		Arc a4 = new Arc(3);
		Arc a5 = new Arc(5);
		Arc a6 = new Arc(4);
		/* crée les sommets avec leur vecteur de poids et arcs respectifs */
		Vertex v1 = new Vertex(-550, a1, a2, a3);
		Vertex v2 = new Vertex(-350, a4, a5, a6);
		Vertex v3 = new Vertex(400, a1, a4);
		Vertex v4 = new Vertex(300, a2, a5);
		Vertex v5 = new Vertex(200, a3, a6);
		/* définit les sommets des arcs */
		a1.setVertex(v1, v3);
		a2.setVertex(v1, v4);
		a3.setVertex(v1, v5);
		a4.setVertex(v2, v3);
		a5.setVertex(v2, v4);
		a6.setVertex(v2, v5);
		/* ajoute au graphe les sommets et arcs */
		graph.addVertex(v1, v2, v3, v4, v5);
		graph.addArcs(a1, a2, a3, a4, a5, a6);
		/** END **/
		
		System.out.println(graph.toString());
		
	}

}
