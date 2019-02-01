package plugin.metrics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import plugin.persistences.Dependency;
import plugin.persistences.MetricsInformation;

public class LCO_LackOfCohesionInMethods extends Metrics{

	public LCO_LackOfCohesionInMethods(HashMap<String, ArrayList<Dependency>> codeFragments) {
		super(codeFragments);
		metricFeature = new ArrayList<MetricsInformation>();
		calculate();
		metricSystem.add(
				new MetricsInformation("Lack of Cohesion in Methods (LCO)", metricFeature, Node.NON_LEAF, Propagation.AVERAGE));
	
		
	}

	@Override
	public void calculate() {
		for (Entry<String, ArrayList<Dependency>> feature : code.entrySet()) {
			metricComponent = new ArrayList<MetricsInformation>();
			for(int i = 0; i < feature.getValue().size();i++) {
				
			}
		}
		
	}

}
