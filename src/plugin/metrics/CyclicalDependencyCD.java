package plugin.metrics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import plugin.persistences.Dependency;
import plugin.persistences.MetricsInformation;

public class CyclicalDependencyCD {

	private HashMap<String, ArrayList<Dependency>> code;
	private ArrayList<MetricsInformation> metricCD = new ArrayList<MetricsInformation>();

	public CyclicalDependencyCD(HashMap<String, ArrayList<Dependency>> code) {
		this.code = code;
		calculate();
		print();
	}

	private void print() {
		System.out
				.println("******************************* Cyclical Dependency *******************************");
		for (int i = 0; i < metricCD.size(); i++) {
			System.out.println(metricCD.get(i).getFeatureName() + "   " + metricCD.get(i).getMetricValueInt());
		}
	}

	private void calculate() {
		for (Entry<String, ArrayList<Dependency>> entry : code.entrySet()) {
			int contarDP = 0;
			if (entry.getValue().size() > 1) {

				for (int i = 0; i < entry.getValue().size()-1; i++) {
					
					for(int j = 1; j<entry.getValue().size(); j++) {
						if(entry.getValue().get(i).getDependencias().contains(entry.getValue().get(j).getNewClassName())
								&& entry.getValue().get(j).getDependencias().contains(entry.getValue().get(i).getNewClassName())){
							contarDP++;
						}
					}
				}
			}
			metricCD.add(new MetricsInformation(entry.getKey(), contarDP));
		}

	}

}
