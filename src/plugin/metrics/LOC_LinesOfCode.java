package plugin.metrics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import plugin.persistences.Dependency;
import plugin.persistences.MetricsInformation;

public class LOC_LinesOfCode extends Metrics {

	public LOC_LinesOfCode(HashMap<String, ArrayList<Dependency>> code) {
		super(code);
		metricFeature = new ArrayList<MetricsInformation>();
		calculate();
		metricSystem.add(new MetricsInformation("Lines of Code (LOC)", metricFeature, Node.NON_LEAF, Propagation.SUM));
	}

	@Override
	public void calculate() {
		for (Entry<String, ArrayList<Dependency>> feature : code.entrySet()) {
			metricComponent = new ArrayList<MetricsInformation>();
			for (int i = 0; i < feature.getValue().size(); i++) {
				//	Document doc = new Document(feature.getValue().get(i).getClasse().getSource());
				metricComponent.add(
						new MetricsInformation(feature.getValue().get(i).getNewClassName(), 
								feature.getValue().get(i).getNumberOfLines(),
								Node.LEAF));
				
			}
			metricFeature
					.add(new MetricsInformation(feature.getKey(), metricComponent, Node.NON_LEAF, Propagation.SUM));
		}
	}
}
