package metrics;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import information.Dependency;
import information.MetricsInformation;
import information.Node;
import information.Propagation;

public class LCC_LackOfConcernBasedCohesion extends Metrics {

	public LCC_LackOfConcernBasedCohesion(HashMap<String, ArrayList<Dependency>> codeFragments) {
		super(codeFragments);
		metricFeature = new ArrayList<MetricsInformation>();
		calculate();

		metricSystem.add(new MetricsInformation("Lack of Concern-based Cohesion (LCC)", metricFeature, Node.NON_LEAF,
				Propagation.AVERAGE));
	}

	public void calculate() {

		// one feature
		for (Entry<String, ArrayList<Dependency>> feature : code.entrySet()) {
			ArrayList<String> useFeatures = new ArrayList<>();
			useFeatures.add(feature.getKey());
			// check if it uses components from other features
			for (Entry<String, ArrayList<Dependency>> feature2 : code.entrySet()) {
				if (!feature.getKey().equals(feature2.getKey())) {
					for (int i = 0; i < feature.getValue().size(); i++) {
						for (int j = 0; j < feature.getValue().get(i).getDependencias().size(); j++) {

							for (int k = 0; k < feature2.getValue().size(); k++) {
								if (feature.getValue().get(i).getDependencias().get(j)
										.equals(feature2.getValue().get(k).getClasseName())) {
									if (!useFeatures.contains(feature2.getKey())) {
										useFeatures.add(feature2.getKey());
									}
								}
							}

						}
					}
				}
			}
			metricFeature.add(new MetricsInformation(feature.getKey(), useFeatures.size(), Node.LEAF));
		}
	}
}
