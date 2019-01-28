package plugin.metrics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import plugin.persistences.Dependency;
import plugin.persistences.MetricsInformation;

public class LackOfConcernBasedCohesionLCC extends Metrics {
	private ArrayList<String> components = new ArrayList<String>();
	private HashMap<String, Integer> aux = new HashMap<String, Integer>();

	public LackOfConcernBasedCohesionLCC(HashMap<String, ArrayList<Dependency>> codeFragments) {
		super(codeFragments);
		metricFeature = new ArrayList<MetricsInformation>();
		calculate();

		match();
		metricSystem.add(new MetricsInformation("Lack of Concern-based Cohesion (LCC)", metricFeature, Node.NON_LEAF,
				Propagation.AVERAGE));
	}

	private void match() {
		for (Entry<String, ArrayList<Dependency>> feature : code.entrySet()) {
			metricComponent = new ArrayList<MetricsInformation>();
			System.out.println("FEATURE: " + feature.getKey());
			for (int i = 0; i < feature.getValue().size(); i++) {
				for (Entry<String, Integer> components : aux.entrySet()) {
					if (feature.getValue().get(i).getNewClassName().equals(components.getKey())) {
						metricComponent
								.add(new MetricsInformation(components.getKey(), components.getValue(), Node.LEAF));
					}
				}
			}
			metricFeature
					.add(new MetricsInformation(feature.getKey(), metricComponent, Node.NON_LEAF, Propagation.AVERAGE));
		}

	}

	public void calculate() {

		for (Entry<String, ArrayList<Dependency>> entry : code.entrySet()) {
			for (int i = 0; i < entry.getValue().size(); i++) {
				if (!components.contains(entry.getValue().get(i).getNewClassName())) {
					components.add(entry.getValue().get(i).getNewClassName());
				}
			}
		}

		// verifica em cada feature se o componente é usado
		for (int i = 0; i < components.size(); i++) {
			ArrayList<String> featuresUse = new ArrayList<String>();
			for (Entry<String, ArrayList<Dependency>> entry : code.entrySet()) {
				for (int j = 0; j < entry.getValue().size(); j++) {
					if (entry.getValue().get(j).getDependencias().contains(components.get(i))) {
						if (!featuresUse.contains(entry.getKey())) {
							featuresUse.add(entry.getKey());
						}
					}
				}
			}
			aux.put(components.get(i), featuresUse.size());
		}
	}
}
