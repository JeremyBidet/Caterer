package fr.upem.jbidet.caterer.DAO;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.processing.FilerException;

import fr.upem.jbidet.caterer.Core.Arc;
import fr.upem.jbidet.caterer.Core.Graph;
import fr.upem.jbidet.caterer.Core.Vertex;

/**
 * Représenter le parseur permettant d'extraire les données du fichier contenant le graphe.<br>
 * Implémente l'interface {@link FilePattern} qui définit les propriétés du parseur.<br>
 * @author Jeremy
 * @version 1.0
 */
public class GraphParser implements FilePattern {
		
	private int[] parseVertex(String vertexList, int vertexQuantity) throws FilerException {
		Pattern p = Pattern.compile(vertexRegExp);
		Matcher m = p.matcher(vertexList);
		int[] vertex = new int[vertexQuantity];
		int i = 0;
		while(m.find()) {
			vertex[i] = Integer.parseInt(m.group("vertexWeight"));
			i++;
		}
		if(i != vertexQuantity) {
			throw new FilerException("File is corrupted : vertex quantity is not equal to vertex weight quantity !");
		}
		return vertex;
	}
	
	private List<int[]> parseArc(String arcList) {
		Pattern p = Pattern.compile(arcRegExp);
		Matcher m = p.matcher(arcList);
		List<int[]> arc = new ArrayList<int[]>();;
		while(m.find()) {
			int[] tmp = new int[3];
			tmp[0] = Integer.parseInt(m.group("vertexA"));
			tmp[1] = Integer.parseInt(m.group("vertexB"));
			tmp[2] = Integer.parseInt(m.group("cost"));
			arc.add(tmp);
		}
		return arc;
	}
	
	@Override
	public Graph parseFile(File file) throws FilerException, FileNotFoundException {
		
		if( !file.exists() ) {
			throw new FileNotFoundException(file.getName());
		}
		if( !file.canRead() ) {
			throw new FilerException(file.getName() + " cannot be read !");
		}
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			StringBuilder sb = new StringBuilder();;
			for (String line; (line = br.readLine()) != null; ) {
				sb.append(line).append("\n");
			}
			br.close();
			String content = sb.toString();
			
			Pattern p = Pattern.compile(graphRegExp);
			Matcher m = p.matcher(content);
			
			if( !m.matches() ) {
				throw new FilerException(file.getName() + " is corrupted !");
			}
			
			Graph graph = new Graph(file.getName());
			/* parse le nombre de sommets */
			int vertexQuantity = Integer.parseInt(m.group("vertexQuantity"));
			/* instancie les sommets avec leur poids */
			int[] vertex = parseVertex(m.group("vertexList"), vertexQuantity);
			List<Vertex> vertexs = new ArrayList<Vertex>(vertexQuantity);
			for(int v : vertex) {
				vertexs.add(new Vertex(v));
			}
			/* instancie les arcs avec leurs sommets et coût */
			List<int[]> arc = parseArc(m.group("arcList"));
			List<Arc> arcs = new ArrayList<Arc>();
			for(int[] a : arc) {
				arcs.add(new Arc(vertexs.get(a[0]), vertexs.get(a[1]), a[2]));
			}
			/* définie les arcs des sommets */
			for(Vertex v : vertexs) {
				v.addArcs(subList(arcs, v));
			}
			graph.setVertex(vertexs);
			graph.setArcs(arcs);
			
			return graph;
			
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		} catch(IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private List<Arc> subList(List<Arc> arcs, Vertex vertex) {
		List<Arc> arc = new ArrayList<Arc>();
		for(Arc a : arcs) {
			if( a.getVertexA().equals(vertex) || a.getVertexB().equals(vertex) ) {
				arc.add(a);
			}
		}
		return arc;
	}

}
