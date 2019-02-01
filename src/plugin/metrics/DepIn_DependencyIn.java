package plugin.metrics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import plugin.persistences.Dependency;
import plugin.persistences.MetricsInformation;

public class DepIn_DependencyIn extends Metrics {

	public DepIn_DependencyIn(HashMap<String, ArrayList<Dependency>> code) {
		super(code);
		metricFeature = new ArrayList<MetricsInformation>();
		calculate();
		metricSystem.add(new MetricsInformation("Dependency In (DepIn)", metricFeature, Node.NON_LEAF,
				Propagation.NONE));
	}

	@Override
	public void calculate() {
		// one feature
		for (Entry<String, ArrayList<Dependency>> feature : code.entrySet()) {
			ArrayList<String> usedByFeatures = new ArrayList<>();
			// get components of the feature
			ArrayList<String> featureComponents = new ArrayList<>();
			for (int i = 0; i < feature.getValue().size(); i++) {
				if (!featureComponents.contains(feature.getValue().get(i).getNewClassName())) {
					featureComponents.add(feature.getValue().get(i).getNewClassName());
				}
			}
			
			// check if the components is used by other features
			for (Entry<String, ArrayList<Dependency>> feature2 : code.entrySet()) {
				if(!feature.getKey().equals(feature2.getKey())){
					for(int i = 0; i < feature2.getValue().size();i++) {
						for(int j = 0; j < feature2.getValue().get(i).getDependencias().size();j++) {
							for (int k = 0; k < featureComponents.size(); k++) {
								if (feature2.getValue().get(i).getDependencias().get(j).contains(featureComponents.get(k))) {
									if(!usedByFeatures.contains(feature2.getKey())) {
										usedByFeatures.add(feature2.getKey());
									}
								}
							}
						}
					}
				}
			}
			System.out.println("\n\nFEATURE: " + feature.getKey());
			System.out.println("DepIn: " + usedByFeatures.size());
		}
	}
}

