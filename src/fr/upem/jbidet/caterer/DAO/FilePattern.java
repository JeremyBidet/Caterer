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
					+ "(\\s*(//.*)?)*" // commentaires avant <vertexquantity>
					+ "((?<vertex>\\d+)" 						+ "[ \\t]*(//.*)?" + "\\n)" // commentaire fin <vertexquantity>
					+ "(\\s*(//.*)?)*" // commentaires entre <vertexquantity> et <vertexlist>
					+ "((?<vertexlist>(-?\\d+ ?)+)"				+ "[ \\t]*(//.*)?" + ")" // commentaire fin <vertexlist>
					+ "(\\s*(//.*)?)*" // commentaires entre <vertexlist> et <arclist>
					+ "(?<arclist>"
							+ "(\\s*(//.*)?)*" // commentaires entre les arcs
							+ "(\\n(\\d+) (\\d+) (-?\\d+)"		+ "[ \\t]*(//.*)?" + ")+)" // commentaire fin <arc>
					+ "(\\s*(//.*)?)*" // commentaires apres <arclist>
			+ ")";
	
	public final String vertexRegExp = ""
			+ "(?<weight>-?\\d+" + " ?)+";
	
	public final String arcRegExp = ""
			+ "(\\s*(//.*)?)*" // commentaires entre les <arc>
			+ "(?<arc>\\n"
					+ "(?<vertexA>\\d+)" + " "
					+ "(?<vertexB>\\d+)" + " "
					+ "(?<cost>-?\\d+)"		+ "[ \\t]*(?<comment3>//.*)?" // commentaire fin <arc>
			+ ")+";
	
	/**
	 * Extrait les données d'un graphe depuis un fichier et renvoie une instance de ce graphe.<br>
	 * @param path le chemin venant vers le fichier qui contient le graphe à parser
	 * @return <b>graph</b> le graphe d'après le fichier
	 * @throws FilerException 
	 * @throws FileNotFoundException 
	 * @see Graph
	 */
	public Graph parseFile(File file) throws FilerException, FileNotFoundException;
	
}
