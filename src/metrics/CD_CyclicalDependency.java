package metrics;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import information.Dependency;
import information.MetricsInformation;
import information.Node;
import information.Propagation;

public class CD_CyclicalDependency extends Metrics {

	public CD_CyclicalDependency(HashMap<String, ArrayList<Dependency>> code) {
		super(code);
		metricFeature = new ArrayList<MetricsInformation>();
		calculate();
		metricSystem.add(
				new MetricsInformation("Cyclical Dependencies (CD)", metricFeature, Node.NON_LEAF, Propagation.SUM));
	}

	public void calculate() {
		for (Entry<String, ArrayList<Dependency>> entry : code.entrySet()) {
			int contarDP = 0;
			if (entry.getValue().size() > 1) {

				for (int i = 0; i < entry.getValue().size() - 1; i++) {

					for (int j = 1; j < entry.getValue().size(); j++) {
						if (entry.getValue().get(i).getDependencias().contains(entry.getValue().get(j).getClasseName())
								&& entry.getValue().get(j).getDependencias()
										.contains(entry.getValue().get(i).getClasseName())) {
							contarDP++;
						}
					}
				}
			}
			metricFeature.add(new MetricsInformation(entry.getKey(), contarDP, Node.LEAF));
		}
	}
}
