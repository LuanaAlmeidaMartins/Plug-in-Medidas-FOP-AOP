package plugin.metrics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import plugin.persistences.Dependency;
import plugin.persistences.MetricsInformation;

public class CSC_ConcernSensitiveCoupling extends Metrics {

	public CSC_ConcernSensitiveCoupling(HashMap<String, ArrayList<Dependency>> codeFragments) {
		super(codeFragments);
		metricFeature = new ArrayList<MetricsInformation>();
		calculate();
		metricSystem.add(
				new MetricsInformation("Concern Sensitive Coupling (CSC)", metricFeature, Node.NON_LEAF, Propagation.AVERAGE));
	}

	public void calculate() {
		int contarDP = 0;
		for (Entry<String, ArrayList<Dependency>> entry : code.entrySet()) {
			metricComponent = new ArrayList<MetricsInformation>();

			if (entry.getValue().size() > 1) {
				ArrayList<String> dependencies = new ArrayList<>();

				for (int i = 0; i < entry.getValue().size(); i++) {
					dependencies.add(entry.getValue().get(i).getNewClassName());
				}
				for (int i = 0; i < entry.getValue().size(); i++) {
					contarDP = 0;
					for (int j = 0; j < dependencies.size(); j++) {
						if (entry.getValue().get(i).getNewClassName() != dependencies.get(j)) {
							if (entry.getValue().get(i).getDependencias().contains(dependencies.get(j))) {
								contarDP++;
							}
						}
					}
					metricComponent.add(
							new MetricsInformation(entry.getValue().get(i).getNewClassName(), contarDP, Node.LEAF));
				}
			} else {
				for (int i = 0; i < entry.getValue().size(); i++) {
				metricComponent.add(
						new MetricsInformation(entry.getValue().get(i).getNewClassName(), 0, Node.LEAF));
				}
			}
			
			metricFeature.add(new MetricsInformation(entry.getKey(), metricComponent, Node.NON_LEAF, Propagation.AVERAGE));
		}
	}
}
