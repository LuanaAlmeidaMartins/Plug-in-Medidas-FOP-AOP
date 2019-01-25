package plugin.metrics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import plugin.persistences.Dependency;
import plugin.persistences.MetricsInformation;

public class LackOfConcernBasedCohesionLCC {
	
	private HashMap<String, ArrayList<Dependency>> code;
	private ArrayList<String> components = new ArrayList<String>();
	private ArrayList<MetricsInformation> metricLCC = new ArrayList<MetricsInformation>();
	
	public LackOfConcernBasedCohesionLCC(HashMap<String, ArrayList<Dependency>> code) {
		this.code = code;
		calculate();
		print();
	}

	private void print() {
		System.out
		.println("******************************* Lack Of Concern Based Cohesion *******************************");
		for (int i = 0; i < metricLCC.size(); i++) {
			System.out.println(metricLCC.get(i).getFeatureName() + "   " + metricLCC.get(i).getMetricValue());
		}
		
	}

	private void calculate() {
		
		// Cria lista com todos os componentes
		for (Entry<String, ArrayList<Dependency>> entry : code.entrySet()) {
			for(int i = 0; i < entry.getValue().size(); i++) {
				if(!components.contains(entry.getValue().get(i).getNewClassName())) {
					components.add(entry.getValue().get(i).getNewClassName());
				}
			}
		}
		
		// verifica em cada feature se o componente é usado
		for(int i = 0; i < components.size();i++) {
			ArrayList<String> featuresUse = new ArrayList<String>();
			for (Entry<String, ArrayList<Dependency>> entry : code.entrySet()) {
				for(int j = 0; j < entry.getValue().size(); j++) {
					if(entry.getValue().get(j).getDependencias().contains(components.get(i))) {			
						if(!featuresUse.contains(entry.getKey())) {
							featuresUse.add(entry.getKey());
						}
					}
				}
				
			}
		//	metricLCC.add(new MetricsInformation(components.get(i), featuresUse.size()));
		}
	}
	
	
	
}
