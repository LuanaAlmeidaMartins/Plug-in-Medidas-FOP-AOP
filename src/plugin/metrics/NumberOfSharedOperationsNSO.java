package plugin.metrics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import plugin.persistences.Dependency;
import plugin.persistences.MetricsInformation;

public class NumberOfSharedOperationsNSO extends Metrics {

	public NumberOfSharedOperationsNSO(HashMap<String, ArrayList<Dependency>> code) {
		super(code);
		metricFeature = new ArrayList<MetricsInformation>();
		calculate();
		metricSystem.add(
				new MetricsInformation("Number of Shared Operations (NSO)", 
						metricFeature, Node.NON_LEAF, Propagation.AVERAGE));
	}


	public void calculate() {

		for (Entry<String, ArrayList<Dependency>> feat1 : code.entrySet()) {
			ArrayList<String> classesFromFeatures = new ArrayList<>();
			ArrayList<String> myClasses = new ArrayList<>();

			// Pegar nomes de classes da feature analisada
			for (int i = 0; i < feat1.getValue().size(); i++) {
				myClasses.add(feat1.getValue().get(i).getNewClassName());
			}

			// Pegar o nome das classes de outras features
			ArrayList<String> contarDP = new ArrayList<String>();
			for (Entry<String, ArrayList<Dependency>> feat2 : code.entrySet()) {
				if (feat1.getKey() != feat2.getKey()) {
					// coloca todos as classes de outros interesses numa lista
					for (int i = 0; i < feat2.getValue().size(); i++) {
						if (!classesFromFeatures.contains(feat2.getValue().get(i).getNewClassName())) {
							classesFromFeatures.add(feat2.getValue().get(i).getNewClassName());
						}
					}

				}
			}

			// Verificar se nas dependencias da feature analisada é encontrado o nome de
			// classes de outras features
			for (int i = 0; i < feat1.getValue().size(); i++) {
				for (int j = 0; j < classesFromFeatures.size(); j++) {
					if (!myClasses.contains(classesFromFeatures.get(j))) {
						if (feat1.getValue().get(i).getDependencias().contains(classesFromFeatures.get(j))) {
							if(!contarDP.contains(classesFromFeatures.get(j))){
								contarDP.add(classesFromFeatures.get(j));
							}
						}
					}
				}
			}
			metricFeature.add(new MetricsInformation(feat1.getKey(), contarDP.size(), Node.LEAF));
		}
	}
}
