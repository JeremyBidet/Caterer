package fr.upem.jbidet.caterer;

import java.io.File;

import fr.upem.jbidet.caterer.Core.Graph;
import fr.upem.jbidet.caterer.Core.Vertex;
import fr.upem.jbidet.caterer.DAO.GraphParser;

/**
 * @author Melody
 * @version 1.0
 */
public class Main {

	public static void main(String[] args) {
		
		GraphParser gp = new GraphParser();
		File file = new File("tests/testregex");
		Graph graph;
		if((graph = gp.parseFile(file)) == null) {
			System.out.println("! This file is corrupted !");
			return;
		}
		int sum = 0;
		for(Vertex v : graph.getVertex()) {
			sum += v.getVertexWeight();
		}
		if(sum != 0) {
			System.out.println("Erreur ! Offre et demande non �gale !");
			return;
		} else {
			System.out.println("Ok !");
		}
		
	}

}
