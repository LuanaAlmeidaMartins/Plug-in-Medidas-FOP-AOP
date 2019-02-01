package plugin.metrics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import plugin.persistences.Dependency;
import plugin.persistences.MetricsInformation;

public class EFD_ExternalRatioFeatureDependency extends Metrics {

	public EFD_ExternalRatioFeatureDependency(HashMap<String, ArrayList<Dependency>> code) {
		super(code);
		metricFeature = new ArrayList<MetricsInformation>();
		calculate();
		metricSystem.add(new MetricsInformation("External-ratio Feature Dependency (EFD)", metricFeature, Node.NON_LEAF,
				Propagation.NONE));
	}

	@Override
	public void calculate() {
		ArrayList<String> allComponents = getAllComponents();

		// one feature
		for (Entry<String, ArrayList<Dependency>> feature : code.entrySet()) {
			int allDependencies = 0;
			int internalDependencies = 0;

			// get components of the feature
			ArrayList<String> featureComponents = new ArrayList<>();
			for (int i = 0; i < feature.getValue().size(); i++) {
				if (!featureComponents.contains(feature.getValue().get(i).getNewClassName())) {
					featureComponents.add(feature.getValue().get(i).getNewClassName());
				}
			}

			// compare dependencies
			for (int i = 0; i < feature.getValue().size(); i++) {
				for (int k = 0; k < feature.getValue().get(i).getDependencias().size(); k++) {
					for (int j = 0; j < allComponents.size(); j++) {

						// if dependency is internal or external
						if (feature.getValue().get(i).getDependencias().get(k).contains(allComponents.get(j))) {
							allDependencies++;
						}
					}

					// if dependency is internal
					for (int j = 0; j < featureComponents.size(); j++) {
						if (feature.getValue().get(i).getDependencias().get(k).contains(featureComponents.get(j))) {
							internalDependencies++;
						}
					}
				}

			}
			System.out.println("\n\nFEATURE: " + feature.getKey());
			System.out.println("All dependencies: " + allDependencies);
			System.out.println("Internal dependencies: " + internalDependencies);
			System.out.println("EFD: " + (float) internalDependencies / allDependencies);
			// metricFeature.add(new MetricsInformation(feature.getKey(), metricComponent,
			// Node.NON_LEAF, Propagation.AVERAGE));
		}

	}

	private ArrayList<String> getAllComponents() {
		ArrayList<String> allComponents = new ArrayList<>();
		for (Entry<String, ArrayList<Dependency>> feature : code.entrySet()) {
			for (int i = 0; i < feature.getValue().size(); i++) {
				if (!allComponents.contains(feature.getValue().get(i).getNewClassName())) {
					allComponents.add(feature.getValue().get(i).getNewClassName());
				}
			}
		}
		return allComponents;
	}

}