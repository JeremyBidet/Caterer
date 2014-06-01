package fr.upem.jbidet.caterer.DAO;

import java.io.File;
import java.io.FileNotFoundException;

import javax.annotation.processing.FilerException;

import fr.upem.jbidet.caterer.Core.Graph;

/**
 * Implémente l'expression régulière et la méthode permettant de parser le fichier contenant le graphe.<br>
 * @author Jeremy
 * @version 1.0
 */
public interface FilePattern {

	public final String graphRegExp = ""
			+ "(?<graph>"
				+ "(\\s*(//.*)?)*" 								// commentaires avant <vertexQuantity>
				+ "(?<vertexQuantity>[+]?\\d+)"					// nombre de sommets
				+ "(\\s*(//.*)?)*" + "\\n"						// commentaires entre <vertexQuantity> et <vertexList>
				+ "(?<vertexList>"
					+ "([+-]?\\d+ )+"							// liste des poids des sommets (sauf le dernier)
					+ "([+-]?\\d+)"								// le dernier poids de sommet de la liste
				+ ")"
				+ "(?<arcList>"									// liste des arcs du graphe
					+ "("
						+ "(\\s*(//.*)?)*" + "\\n"				// commentaires entre <vertexList> et <arcList>, ou à la fin d'un <arc>, ou entre les <arc>
						+ "([+]?\\d+) ([+]?\\d+) ([+-]?\\d+)"	// un arc avec son vA, vB et son coût
					+ ")+"
				+ ")"
				+ "(\\s*(//.*)?)*"								// commentaires de fin de fichiers (après <arcList>)
			+ ")";
	
	public final String vertexRegExp = ""
			+ "((?<vertexWeight>[+-]?\\d+) ?)"; // liste des poids des sommets (sauf le dernier)
	
	public final String arcRegExp = ""
			+ "(?<arc>"
				+ "(\\s*(//.*)?)*" + "\\n"		// commentaires entre <vertexList> et <arcList>, ou à la fin d'un <arc>, ou entre les <arc>
				+ "(?<vertexA>[+]?\\d+)" + " "	// sommet A de l'arc
				+ "(?<vertexB>[+]?\\d+)" + " "	// sommet B de l'arc
				+ "(?<cost>[+-]?\\d+)" 			// coût de l'arc
			+ ")";
	
	/**
	 * Extrait les données d'un graphe depuis un fichier et renvoie une instance de ce graphe.<br>
	 * @param path le chemin venant vers le fichier qui contient le graphe à parser
	 * @return <b>graph</b> le graphe d'après le fichier
	 * @throws FilerException 
	 * @throws FileNotFoundException 
	 * @see Graph
	 */
	public Graph parseFile(File file);
	
}
