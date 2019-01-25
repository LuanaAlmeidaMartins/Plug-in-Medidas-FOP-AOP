package plugin.metrics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import org.eclipse.jdt.core.JavaModelException;

import plugin.persistences.Dependency;
import plugin.persistences.MetricsInformation;

public class NumberOfOperationsNOO extends Metrics {
	
	public NumberOfOperationsNOO(HashMap<String, ArrayList<Dependency>> code) throws JavaModelException {
		super(code);
		metricFeature = new ArrayList<MetricsInformation>();
		calculate();
		metricSystem.add(
				new MetricsInformation("Number of Operations (NO)", metricFeature, Node.NON_LEAF, Propagation.SUM));
	}



	public void calculate() {
		for (Entry<String, ArrayList<Dependency>> entry : code.entrySet()) {
			metricComponent = new ArrayList<MetricsInformation>();
			int sum = 0;
			for (int i = 0; i < entry.getValue().size(); i++) {
				try {
					sum += entry.getValue().get(i).getNumberOfOperations();
				} catch (JavaModelException e) {
					e.printStackTrace();
				}
				metricComponent.add(
						new MetricsInformation(entry.getValue().get(i).getNewClassName(), sum, Node.LEAF));
			}
			metricFeature.add(new MetricsInformation(entry.getKey(), metricComponent, Node.NON_LEAF, Propagation.SUM));
		}
	}
}
