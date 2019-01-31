package plugin.metrics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;

import plugin.persistences.Dependency;
import plugin.persistences.MetricsInformation;

public class ExternalRatioFeatureDependency_EFD extends Metrics{

	public ExternalRatioFeatureDependency_EFD(HashMap<String, ArrayList<Dependency>> code) {
		super(code);
		metricFeature = new ArrayList<MetricsInformation>();
		calculate();
		metricSystem.add(
				new MetricsInformation("External-ratio Feature Dependency (EFD)", 
						metricFeature, Node.NON_LEAF, Propagation.NONE));
	}

	@Override
	public void calculate() {
		
		ArrayList<String> allComponents = new ArrayList<>();
		for (Entry<String, ArrayList<Dependency>> feature : code.entrySet()) {
			metricComponent = new ArrayList<MetricsInformation>();
			for(int i =0; i < feature.getValue().size();i++) {
				if(!allComponents.contains(feature.getValue().get(i).getNewClassName())) {
					allComponents.add(feature.getValue().get(i).getNewClassName());
				}
			}
			metricFeature.add(new MetricsInformation(feature.getKey(), metricComponent, Node.NON_LEAF, Propagation.AVERAGE));
		}
		System.out.println("\n\n\nlista todos os componentes: "+Arrays.toString( allComponents.toArray()));
		// one feature
		for (Entry<String, ArrayList<Dependency>> feature : code.entrySet()) {
			ArrayList<String> allDependencies = new ArrayList<>();
			ArrayList<String> internalDependencies = new ArrayList<>();
			System.out.println("FEATURE: "+feature.getKey());
			ArrayList<String> featureComponents = new ArrayList<>();
			for(int i =0; i < feature.getValue().size();i++) {
				if(!featureComponents.contains(feature.getValue().get(i).getNewClassName())) {
					featureComponents.add(feature.getValue().get(i).getNewClassName());
				}
			}

			// compare
			for(int i =0; i < feature.getValue().size();i++) {
				// todas 
				for(int k = 0; k < feature.getValue().get(i).getDependencias().size();k++) {
				for(int j = 0; j < allComponents.size();j++) {
						if(feature.getValue().get(i).getDependencias().get(k).contains(allComponents.get(j))) {
							if(!allDependencies.contains(allComponents.get(j))) {
								allDependencies.add(allComponents.get(j));
							System.out.println("todas DP"+ feature.getValue().get(i).getDependencias().get(k)+
									"   "+allComponents.get(j));
							}
						}
					}
			
				// dentro feat
				for(int j = 0; j < featureComponents.size();j++) {
					if(feature.getValue().get(i).getDependencias().get(k).contains(featureComponents.get(j))) {
						
					}
				}
				}
				
				
			}
		}
		
	}

}
