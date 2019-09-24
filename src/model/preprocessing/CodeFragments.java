package model.preprocessing;

import java.util.ArrayList;
import java.util.HashMap;

import model.information.Dependency;
import model.information.FileInformation;

public class CodeFragments {

	private ArrayList<FileInformation> code;
	private ArrayList<Dependency> classesDependencias;
	private ArrayList<String> classesNames = new ArrayList<>();
	private ArrayList<Dependency> classes;
	private HashMap<String, ArrayList<Dependency>> featuresFull = new HashMap<String, ArrayList<Dependency>>();
	private ArrayList<String> allFeatures = new ArrayList<>();
	private ArrayList<String> featurePrincipal = new ArrayList<>();

	public CodeFragments(ArrayList<FileInformation> code, ArrayList<Dependency> classesDependencias) {
		this.code = code;
		this.classesDependencias = classesDependencias;
	}

	public void jak() {
		classesFromSource();
		foundFeature();
		searchFilesIntoSource();
		print();
	}

	public void aspect() {
		classesFromSource();
		searchFilesIntoSource();
		print();
	}

	private void foundFeature() {
		for (int i = 0; i < code.size(); i++) {
			for (int j = 0; j < code.get(i).getJakFiles().size(); j++) {
				String firstName = code.get(i).getJakFiles().get(j) + "$$" + code.get(i).getFeatureName();
				if (!classesNames.contains(firstName)) {
					if (!allFeatures.contains(code.get(i).getFeatureName())) {
						allFeatures.add(code.get(i).getFeatureName());
					}
				}
				if (classesNames.contains(firstName + "1")) {
					for (int t = 0; t < allFeatures.size(); t++) {
						if (!featurePrincipal.contains(code.get(i).getFeatureName())) {
							featurePrincipal.add(code.get(i).getFeatureName());
						}
					}
				}
			}
		}

		for (int i = 0; i < allFeatures.size(); i++) {
			if (featurePrincipal.contains(allFeatures.get(i))) {
				allFeatures.remove(i);
			}
		}
	}

	// Printing code fragments
	private void print() {
		//		for (Entry<String, ArrayList<Dependency>> entry : featuresFull.entrySet()) {
		//			System.out.println("\n\nFEATURE: " + entry.getKey());
		//			for (int i = 0; i < entry.getValue().size(); i++) {
		//				System.out.println("CLASSE: " + entry.getValue().get(i).getClasseName());
		//				System.out.println("DP: " + Arrays.toString(entry.getValue().get(i).getDependencias().toArray()));
		//				// System.out.println("METHODS " +
		//				// Arrays.toString(entry.getValue().get(i).getMethodsCalled().toArray()));
		//			}
		//		}
	}

	// Searching the jak and java files from "features" package into "default"
	// package
	private void searchFilesIntoSource() {
		for (int i = 0; i < code.size(); i++) {

			classes = new ArrayList<>();

			// jak files
			for (int j = 0; j < code.get(i).getJakFiles().size(); j++) {
				String completeName = code.get(i).getJakFiles().get(j).concat("$$" + code.get(i).getFeatureName());
				String secondName = code.get(i).getJakFiles().get(j).concat("$$" + code.get(i).getFeatureName() + 1);
				if ((!classesNames.contains(completeName)) && (!classesNames.contains(secondName))) {
					for (int k = 0; k < classesDependencias.size(); k++) {
						if (code.get(i).getJakFiles().get(j).equals(classesDependencias.get(k).getClasseName())) {
							classesDependencias.get(k)
							.setClassName(code.get(i).getJakFiles().get(j).concat("$$" + allFeatures.get(0)));
							classes.add(classesDependencias.get(k));
						}
					}
				} else {
					for (int k = 0; k < classesDependencias.size(); k++) {
						if (completeName.equals(classesDependencias.get(k).getClasseName())) {
							classesDependencias.get(k).setClassName(completeName);
							classes.add(classesDependencias.get(k));
						}
						if (secondName.equals(classesDependencias.get(k).getClasseName())) {
							classesDependencias.get(k).setClassName(secondName);
							classes.add(classesDependencias.get(k));
						}
					}
				}
			}

			// java files
			for (int j = 0; j < code.get(i).getJavaFiles().size(); j++) {
				for (int k = 0; k < classesDependencias.size(); k++) {
					if (code.get(i).getJavaFiles().get(j).equals(classesDependencias.get(k).getClasseName())) {
						classes.add(classesDependencias.get(k));
						classesDependencias.get(k).setClassName(code.get(i).getJavaFiles().get(j));
					}
				}
			}

			featuresFull.put(code.get(i).getFeatureName(), classes);
		}
	}

	// Creating a list with all the classes found in "default" package from src
	private void classesFromSource() {
		for (int i = 0; i < classesDependencias.size(); i++) {
			this.classesNames.add(classesDependencias.get(i).getClasseName());
		}
	}

	// Returning the code fragments
	public HashMap<String, ArrayList<Dependency>> getCodeFragments() {
		return featuresFull;
	}

	public ArrayList<FileInformation> getCode() {
		return code;
	}

	public ArrayList<Dependency> getDep() {
		return classesDependencias;
	}
}
