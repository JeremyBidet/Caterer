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
				sb.append(line);
			}
			br.close();
			
			String content = sb.toString();
			
			Pattern p = Pattern.compile(graphRegExp);
			Matcher m = p.matcher(content);
			
			if( !m.matches() ) {
				throw new FilerException(file.getName() + " is corrupted !");
			}
			
			List<Vertex> vertex = new ArrayList<Vertex>();
			List<Arc> arcs = new ArrayList<Arc>();
			
			return new Graph(file.getName(), vertex, arcs);
			
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		} catch(IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
