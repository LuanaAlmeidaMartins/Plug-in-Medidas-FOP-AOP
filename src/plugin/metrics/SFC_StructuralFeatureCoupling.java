package plugin.metrics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import plugin.persistences.Dependency;
import plugin.persistences.MetricsInformation;

public class SFC_StructuralFeatureCoupling extends Metrics {

	public SFC_StructuralFeatureCoupling(HashMap<String, ArrayList<Dependency>> code) {
		super(code);
		metricFeature = new ArrayList<MetricsInformation>();
		calculate();
		metricSystem.add(new MetricsInformation("Structural Feature Coupling (SFC)", metricFeature, Node.NON_LEAF,
				Propagation.NONE));
	}

	@Override
	public void calculate() {

		// one feature
		for (Entry<String, ArrayList<Dependency>> feature : code.entrySet()) {

			// get methods of the feature
			ArrayList<String> union = new ArrayList<>();
			ArrayList<String> featureMethods = new ArrayList<>();
			for (int i = 0; i < feature.getValue().size(); i++) {
				for (int j = 0; j < feature.getValue().get(i).getMethodsCalled().size(); j++) {
					if (!featureMethods.contains(feature.getValue().get(i).getMethodsCalled().get(j))) {
						featureMethods.add(feature.getValue().get(i).getMethodsCalled().get(j));
						union.add(feature.getValue().get(i).getMethodsCalled().get(j));
					}
				}
			}
			// union methods
			ArrayList<String> intersection = new ArrayList<String>();
			for (Entry<String, ArrayList<Dependency>> feature2 : code.entrySet()) {
				if (!feature2.getKey().equals(feature.getKey())) {
					for (int i = 0; i < feature2.getValue().size(); i++) {
						for (int j = 0; j < feature2.getValue().get(i).getMethodsCalled().size(); j++) {
							if (!union.contains(feature2.getValue().get(i).getMethodsCalled().get(j))) {
								union.add(feature2.getValue().get(i).getMethodsCalled().get(j));
							}
							if (featureMethods.contains(feature2.getValue().get(i).getMethodsCalled().get(j))) {
								if (!intersection.contains(feature2.getValue().get(i).getMethodsCalled().get(j))) {
									intersection.add(feature2.getValue().get(i).getMethodsCalled().get(j));
								}
							}
						}
					}
				}

			}
			metricFeature.add(new MetricsInformation(feature.getKey(), intersection.size(), union.size(), Node.LEAF));
		}

	}
}
