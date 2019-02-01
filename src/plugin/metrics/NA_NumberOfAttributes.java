package plugin.metrics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import org.eclipse.jdt.core.JavaModelException;

import plugin.persistences.Dependency;
import plugin.persistences.MetricsInformation;

public class NA_NumberOfAttributes extends Metrics {

	public NA_NumberOfAttributes(HashMap<String, ArrayList<Dependency>> code) {
		super(code);
		metricFeature = new ArrayList<MetricsInformation>();
		calculate();
		metricSystem.add(
				new MetricsInformation("Number of Attributes (NA)", metricFeature, Node.NON_LEAF, Propagation.SUM));
	}

	@Override
	public void calculate() {
		for (Entry<String, ArrayList<Dependency>> feature : code.entrySet()) {
			metricComponent = new ArrayList<MetricsInformation>();
			for (int i = 0; i < feature.getValue().size(); i++) {
				try {
					metricComponent.add(
							new MetricsInformation(feature.getValue().get(i).getNewClassName(), 
							feature.getValue().get(i).getClasse().getFields().length, Node.LEAF));
				} catch (JavaModelException e) {
					e.printStackTrace();
				}
				
			}
			metricFeature
					.add(new MetricsInformation(feature.getKey(), metricComponent, Node.NON_LEAF, Propagation.SUM));
		}
	}
}
