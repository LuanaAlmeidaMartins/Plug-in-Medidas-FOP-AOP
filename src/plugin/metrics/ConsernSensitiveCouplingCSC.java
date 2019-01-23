package plugin.metrics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import plugin.persistences.Dependency;
import plugin.persistences.MetricsInformation;

public class ConsernSensitiveCouplingCSC {

	private HashMap<String, ArrayList<Dependency>> code;
	private ArrayList<MetricsInformation> metricCSC = new ArrayList<MetricsInformation>();

	public ConsernSensitiveCouplingCSC(HashMap<String, ArrayList<Dependency>> codeFragments) {
		this.code = codeFragments;
		calculate();
		print();
	}

	private void print() {
		System.out
				.println("******************************* Concern Sensitive Coupling *******************************");
		for (int i = 0; i < metricCSC.size(); i++) {
			System.out.println(metricCSC.get(i).getFeatureName() + "   " + metricCSC.get(i).getMetricValueFloat());
		}
	}

	private void calculate() {

		for (Entry<String, ArrayList<Dependency>> entry : code.entrySet()) {
			ArrayList<String> contarDP = new ArrayList<String>();
			if (entry.getValue().size() > 1) {
				ArrayList<String> dependencies = new ArrayList<>();

				for (int i = 0; i < entry.getValue().size(); i++) {
					dependencies.add(entry.getValue().get(i).getNewClassName());
				}
				for (int i = 0; i < entry.getValue().size(); i++) {
					for (int j = 0; j < dependencies.size(); j++) {
						if (entry.getValue().get(i).getNewClassName() != dependencies.get(j)) {
							if (entry.getValue().get(i).getDependencias().contains(dependencies.get(j))) {
								contarDP.add(dependencies.get(j));

							}
						}
					}
				}
			}
			metricCSC.add(new MetricsInformation(entry.getKey(), (float)contarDP.size()/entry.getValue().size()));
		}
	}
}
