package model.metrics;


import java.util.ArrayList;
import java.util.HashMap;

import model.information.Dependency;
import model.information.MetricsInformation;

public abstract class Metrics {

	public HashMap<String, ArrayList<Dependency>> code;
	public ArrayList<MetricsInformation> metricComponent;
	public ArrayList<MetricsInformation> metricFeature;
	public ArrayList<MetricsInformation> metricSystem = new ArrayList<MetricsInformation>();

	public Metrics(HashMap<String, ArrayList<Dependency>> code) {
		this.code = code;
	}

	public abstract void calculate();

	public ArrayList<MetricsInformation> getMetrics() {
		return metricSystem;
	}

}
