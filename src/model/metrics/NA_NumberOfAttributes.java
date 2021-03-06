package model.metrics;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import model.information.Dependency;
import model.information.MetricsInformation;
import model.information.Node;
import model.information.Propagation;

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
				metricComponent.add(new MetricsInformation(feature.getValue().get(i).getClasseName(),
						feature.getValue().get(i).getAttributes(), Node.LEAF));

			}
			metricFeature
					.add(new MetricsInformation(feature.getKey(), metricComponent, Node.NON_LEAF, Propagation.SUM));
		}
	}
}
