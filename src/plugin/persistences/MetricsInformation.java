package plugin.persistences;

public class MetricsInformation {
	String featureName;
	float metricValue;
	int metricValueInt;
	
	public MetricsInformation(String featureName, float metricValue) {
		this.featureName = featureName;
		this.metricValue = metricValue;
	}
	
	public MetricsInformation(String featureName, int metricValue) {
		this.featureName = featureName;
		this.metricValueInt = metricValue;
	}

	public String getFeatureName() {
		return featureName;
	}

	public float getMetricValueFloat() {
		return metricValue;
	}
	
	public int getMetricValueInt() {
		return metricValueInt;
	}
}
