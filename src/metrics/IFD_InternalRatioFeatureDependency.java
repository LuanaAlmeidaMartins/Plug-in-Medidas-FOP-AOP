package metrics;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import information.Dependency;
import information.MetricsInformation;
import information.Node;
import information.Propagation;

public class IFD_InternalRatioFeatureDependency extends Metrics {

	public IFD_InternalRatioFeatureDependency(HashMap<String, ArrayList<Dependency>> code) {
		super(code);
		metricFeature = new ArrayList<MetricsInformation>();
		calculate();
		metricSystem.add(new MetricsInformation("Internal-ratio Feature Dependency (IFD)", metricFeature, Node.NON_LEAF,
				Propagation.AVERAGE));
	}

	@Override
	public void calculate() {
		// one feature
		for (Entry<String, ArrayList<Dependency>> feature : code.entrySet()) {
			int internalDependencies = 0;

			// get components of the feature
			ArrayList<String> featureComponents = new ArrayList<>();
			for (int i = 0; i < feature.getValue().size(); i++) {
				if (!featureComponents.contains(feature.getValue().get(i).getClasseName())) {
					featureComponents.add(feature.getValue().get(i).getClasseName());
				}
			}

			// compare dependencies
			for (int i = 0; i < feature.getValue().size(); i++) {
				for (int k = 0; k < feature.getValue().get(i).getDependencias().size(); k++) {

					// if dependency is internal
					for (int j = 0; j < featureComponents.size(); j++) {
						if (feature.getValue().get(i).getDependencias().get(k).equals(featureComponents.get(j))) {
							
							internalDependencies++;
						}
					}
				}

			}
			
//			System.out.println("contu "+feature.getKey() + "  "+ internalDependencies + "  "+ featureComponents);
			metricFeature.add(new MetricsInformation(feature.getKey(), internalDependencies, featureComponents.size() * featureComponents.size()
						, Node.LEAF));
		}

	}
}
