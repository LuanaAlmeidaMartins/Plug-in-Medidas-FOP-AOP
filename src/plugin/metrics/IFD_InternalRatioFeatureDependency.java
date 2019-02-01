package plugin.metrics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import plugin.persistences.Dependency;
import plugin.persistences.MetricsInformation;

public class IFD_InternalRatioFeatureDependency extends Metrics {

	public IFD_InternalRatioFeatureDependency(HashMap<String, ArrayList<Dependency>> code) {
		super(code);
		metricFeature = new ArrayList<MetricsInformation>();
		calculate();
		metricSystem.add(new MetricsInformation("Internal-ratio Feature Dependency (IFD)", metricFeature, Node.NON_LEAF,
				Propagation.NONE));
	}

	@Override
	public void calculate() {
		// one feature
		for (Entry<String, ArrayList<Dependency>> feature : code.entrySet()) {
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

					// if dependency is internal
					for (int j = 0; j < featureComponents.size(); j++) {
						if (feature.getValue().get(i).getDependencias().get(k).contains(featureComponents.get(j))) {
							internalDependencies++;
						}
					}
				}

			}
			System.out.println("\n\nFEATURE: " + feature.getKey());
			System.out.println("All elements: " + featureComponents.size());
			System.out.println("Internal dependencies: " + internalDependencies);
			System.out.println(
					"IFD: " + (float) internalDependencies / (featureComponents.size() * featureComponents.size()));
			// metricFeature.add(new MetricsInformation(feature.getKey(), metricComponent,
			// Node.NON_LEAF, Propagation.AVERAGE));
		}

	}
}