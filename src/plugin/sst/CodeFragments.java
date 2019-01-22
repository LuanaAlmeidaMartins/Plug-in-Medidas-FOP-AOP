package plugin.sst;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;

import plugin.persistences.Dependency;
import plugin.persistences.FileInformation;

public class CodeFragments {

	private ArrayList<FileInformation> code;
	private ArrayList<Dependency> classesDependencias;
	private ArrayList<String> classesNames = new ArrayList<>();
	private ArrayList<Dependency> classes;
	private HashMap<String, ArrayList<Dependency>> featuresFull = new HashMap<String, ArrayList<Dependency>>();

	public CodeFragments(ArrayList<FileInformation> code, ArrayList<Dependency> classesDependencias) {
		this.code = code;
		this.classesDependencias = classesDependencias;
		classesFromSource();
		searchFilesIntoSource();
		print();

	}

	// Printing code fragments
	private void print() {
		for (Entry<String, ArrayList<Dependency>> entry : featuresFull.entrySet()) {
			System.out.println("\n\nFEATURE: " + entry.getKey());
			for (int i = 0; i < entry.getValue().size(); i++) {
				System.out.println("CLASSE: " + entry.getValue().get(i).getClasseName());
				System.out.println("DP: " + Arrays.toString(entry.getValue().get(i).getDependencias().toArray()));
			}
		}
	}

	// Searching the jak and java files from "features" package into "default" package
	private void searchFilesIntoSource() {
		for (int i = 0; i < code.size(); i++) {
			classes = new ArrayList<>();

			// jak files
			for (int j = 0; j < code.get(i).getJakFiles().size(); j++) {
				String completeName = code.get(i).getJakFiles().get(j).concat("$$" + code.get(i).getFeatureName());
				if (!classesNames.contains(completeName)) {
					for (int k = 0; k < classesDependencias.size(); k++) {
						if (code.get(i).getJakFiles().get(j)
								.equals(classesDependencias.get(k).getClasse().getElementName())) {
							classesDependencias.get(k).setNewClassName(completeName);
							classes.add(classesDependencias.get(k));
						}
					}
				} else {
					for (int k = 0; k < classesDependencias.size(); k++) {
						if (completeName.equals(classesDependencias.get(k).getClasse().getElementName())) {
							classes.add(classesDependencias.get(k));
						}
					}
				}
			}
			
			// java files
			for (int j = 0; j < code.get(i).getJavaFiles().size(); j++) {
				for (int k = 0; k < classesDependencias.size(); k++) {
					if (code.get(i).getJavaFiles().get(j)
							.equals(classesDependencias.get(k).getClasse().getElementName())) {
						classes.add(classesDependencias.get(k));
					}
				}
			}
			
			featuresFull.put(code.get(i).getFeatureName(), classes);
		}
	}

	// Creating a list with all the classes found in "default" package from src
	private void classesFromSource() {
		for (int i = 0; i < classesDependencias.size(); i++) {
			this.classesNames.add(classesDependencias.get(i).getClasse().getElementName());
		}
	}

	// Returning the code fragments
	public HashMap<String, ArrayList<Dependency>> getCodeFragments() {
		return featuresFull;
	}
}
