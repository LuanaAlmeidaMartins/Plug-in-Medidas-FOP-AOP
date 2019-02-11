package plugin.persistences;

import java.util.ArrayList;
import java.util.Arrays;

import plugin.metrics.Node;

public class MetricsView {

	private ArrayList<ViewInformation> view = new ArrayList<ViewInformation>();
	
	
	public MetricsView(ArrayList<MetricsInformation> jakarta, ArrayList<MetricsInformation> aspectj,
			ArrayList<MetricsInformation> afm) {
		organize(jakarta, aspectj);
	}


	private ArrayList<ViewInformation> orga(ArrayList<MetricsInformation> children, ArrayList<MetricsInformation> compare) {
		ArrayList<ViewInformation> nonLeaf = new ArrayList<>();
		ArrayList<String> nonString = new ArrayList<>();
		ArrayList<String> nameChildren = new ArrayList<String>();
		ArrayList<String> nameCompare = new ArrayList<>();
		
		for(int i = 0; i < children.size();i++) {
			nameChildren.add(children.get(i).getFeatureName());
		}
		
		for(int i = 0; i < compare.size();i++) {
			nameCompare.add(compare.get(i).getFeatureName());
		}
		
		
		
		for (int i = 0; i < children.size(); i++) {
			for(int j = 0; j < compare.size();j++) {
				if(children.get(i).getType().equals(Node.NON_LEAF) &&
						compare.get(j).getType().equals(Node.NON_LEAF)) {
					if(children.get(i).getFeatureName().equals(
							compare.get(j).getFeatureName())) {
						ViewInformation vi = new ViewInformation(children.get(i).getFeatureName(), 
								children.get(i).getType(), orga(children.get(i).getChildren(), compare.get(j).getChildren()));
						vi.setMetricJakarta(children.get(i).getMetricValue());
						vi.setMetricAspectJ(compare.get(j).getMetricValue());
						if(!nonLeaf.contains(vi)) {
							nonLeaf.add(vi);
						}
					}
				}
				if(children.get(i).getType().equals(Node.LEAF) &&
						compare.get(j).getType().equals(Node.LEAF)) { 
					if(children.get(i).getFeatureName().equals(
							compare.get(j).getFeatureName())) {
						ViewInformation vi = new ViewInformation(children.get(i).getFeatureName(), 
								children.get(i).getType());
						vi.setMetricJakarta(children.get(i).getMetricValue());
						vi.setMetricAspectJ(compare.get(j).getMetricValue());
						if(!nonString.contains(vi.getFeatureName())) {
							nonLeaf.add(vi);
							nonString.add(vi.getFeatureName());
						}
					}
					else {
						if(!nameChildren.contains(compare.get(j).getFeatureName())) {
							ViewInformation vi = new ViewInformation(compare.get(j).getFeatureName(), 
									compare.get(j).getType());
							vi.setMetricAspectJ(compare.get(j).getMetricValue());
							if(!nonString.contains(vi.getFeatureName())) {
								nonLeaf.add(vi);
								nonString.add(vi.getFeatureName());
							}
						}
						if(!nameCompare.contains(children.get(i).getFeatureName())) {
							ViewInformation vi = new ViewInformation(children.get(i).getFeatureName(), 
									children.get(i).getType());
							vi.setMetricJakarta(children.get(i).getMetricValue());
							if(!nonString.contains(vi.getFeatureName())) {
								nonLeaf.add(vi);
								nonString.add(vi.getFeatureName());
							}
						}
					}
				}
			}
		}

		return nonLeaf;
	}

	private void organize(ArrayList<MetricsInformation> language, ArrayList<MetricsInformation> compare) {
		for (int i = 0; i < language.size(); i++) {
			for (int j = 0; j < compare.size(); j++) {
				// metric name
				if(language.get(i).getType().equals(Node.NON_LEAF) &&
						compare.get(j).getType().equals(Node.NON_LEAF)){
					if(language.get(i).getFeatureName().equals(
							compare.get(j).getFeatureName())) {
						ViewInformation vi = new ViewInformation(language.get(i).getFeatureName(), 
								language.get(i).getType(), orga(language.get(i).getChildren(), compare.get(j).getChildren()));
						vi.setMetricJakarta(language.get(i).getMetricValue());
						vi.setMetricAspectJ(compare.get(j).getMetricValue());
						view.add(vi);
					}
				}
			}
		}
	}






	public ArrayList<ViewInformation> getView() {
		return view;
	}
}
