package plugin.metrics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import plugin.persistences.Dependency;
import plugin.persistences.MetricsInformation;

public class DepOut_DependencyOut extends Metrics {

	public DepOut_DependencyOut(HashMap<String, ArrayList<Dependency>> code) {
		super(code);
		metricFeature = new ArrayList<MetricsInformation>();
		calculate();
		metricSystem
				.add(new MetricsInformation("Dependency In (DepIn)", metricFeature, Node.NON_LEAF, Propagation.NONE));
	}

	@Override
	public void calculate() {
		// one feature
		for (Entry<String, ArrayList<Dependency>> feature : code.entrySet()) {
			ArrayList<String> useFeatures = new ArrayList<>();
			
			// check if it uses componentes from other features
			for (Entry<String, ArrayList<Dependency>> feature2 : code.entrySet()) {
				if (!feature.getKey().equals(feature2.getKey())) {
					for (int i = 0; i < feature.getValue().size(); i++) {
						for (int j = 0; j < feature.getValue().get(i).getDependencias().size(); j++) {

							for (int k = 0; k < feature2.getValue().size(); k++) {
								if (feature.getValue().get(i).getDependencias().get(j)
										.equals(feature2.getValue().get(k).getNewClassName())) {
									if (!useFeatures.contains(feature2.getKey())) {
										useFeatures.add(feature2.getKey());
									}
								}
							}

						}
					}
				}
			}
			System.out.println("\n\nFEATURE: " + feature.getKey());
			System.out.println("DepOut: " + useFeatures.size());
		}
	}
}
