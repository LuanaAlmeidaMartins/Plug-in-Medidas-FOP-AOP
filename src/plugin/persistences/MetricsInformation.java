package plugin.persistences;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

import plugin.metrics.Node;
import plugin.metrics.Propagation;

public class MetricsInformation {
	private String featureName;
	private int metricValue;
	private float metricValueF;
	private ArrayList<MetricsInformation> components = new ArrayList<>();
	private Node type;
	private Propagation propagation;

	public MetricsInformation(String featureName, ArrayList<MetricsInformation> components, Node type, Propagation prop) {
		this.featureName = featureName;
		this.components = components;
		this.propagation = prop;
		this.type = type;
		this.metricValueF = setMetricValue();
	}

	private float setMetricValue() {
		float sum = 0;
		for (int i = 0; i < components.size(); i++) {
			sum += components.get(i).getMetric();
		}
		if(propagation.equals(Propagation.AVERAGE)) {
			if(components.size()==0) {
				return 0;
			}
			return sum / components.size();
		}
		return sum;
	}

	public MetricsInformation(String featureName, int metricValue, Node type) {
		this.featureName = featureName;
		this.metricValue = metricValue;
		this.type = type;
	}

	public Node getType() {
		return type;
	}

	public ArrayList<MetricsInformation> getChildren() {
		return components;
	}

	public String getFeatureName() {
		return featureName;
	}

	public float getMetric() {
		if (type.equals(Node.LEAF)) {
			return (float) metricValue;
		}
		return metricValueF;
	}

	public String getMetricValue() {
		Number value;                               
		if (type.equals(Node.LEAF)) {
			return String.valueOf(metricValue);
		}
		else {
			if(metricValueF == Math.rint(metricValueF)) {
				value = (int) metricValueF; 
				return String.valueOf(value);
			} else {                
				float result = new BigDecimal(metricValueF).setScale(2, RoundingMode.HALF_UP).floatValue();    
				return String.valueOf(result);
			}   
		}
	}
}
