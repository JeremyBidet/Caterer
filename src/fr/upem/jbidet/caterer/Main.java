package fr.upem.jbidet.caterer;

import java.io.File;
import java.io.FileNotFoundException;

import javax.annotation.processing.FilerException;

import fr.upem.jbidet.caterer.Core.Graph;
import fr.upem.jbidet.caterer.DAO.GraphParser;

/**
 * @author Melody
 * @version 1.0
 */
public class Main {

	public static void main(String[] args) {
		
		GraphParser gp = new GraphParser();
		File file = new File("tests/testregexs");
		try {
			Graph graph2 = gp.parseFile(file);
			System.out.println(graph2);
		} catch (FilerException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}

}
