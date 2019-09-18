package information;

import java.util.ArrayList;

public class ViewInformation {
	private String featureName;
	private String metricJakarta = "-";
	private String metricAspectJ = "-";
	private String metricAfm = "-";
	private ArrayList<ViewInformation> components = new ArrayList<>();
	private Node type;

	public ViewInformation(String featureName, Node type) {
		this.featureName = featureName;
		this.type = type;
	}

	public ViewInformation(String featureName, Node type, ArrayList<ViewInformation> components) {
		this.featureName = featureName;
		this.type = type;
		this.components = components;
	}

	public String getFeatureName() {
		return featureName;
	}

	public void setFeatureName(String featureName) {
		this.featureName = featureName;
	}

	public String getMetricJakarta() {
		return metricJakarta;
	}

	public void setMetricJakarta(String metricJakarta) {
		this.metricJakarta = metricJakarta;
	}

	public String getMetricAspectJ() {
		return metricAspectJ;
	}

	public void setMetricAspectJ(String metricAspectJ) {
		this.metricAspectJ = metricAspectJ;
	}

	public String getMetricAfm() {
		return metricAfm;
	}

	public void setMetricAfm(String metricAfm) {
		this.metricAfm = metricAfm;
	}

	public ArrayList<ViewInformation> getComponents() {
		return components;
	}

	public void setComponents(ArrayList<ViewInformation> components) {
		this.components = components;
	}

	public Node getType() {
		return type;
	}

	public ArrayList<ViewInformation> getChildren() {
		return components;
	}
}
