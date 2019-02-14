package plugin.persistences;

import java.util.ArrayList;
import plugin.metrics.Node;

public class MetricsView {

	private ArrayList<ViewInformation> view = new ArrayList<ViewInformation>();

	public MetricsView(ArrayList<MetricsInformation> jakarta, ArrayList<MetricsInformation> aspectj,
			ArrayList<MetricsInformation> afm) {

		organize(jakarta, aspectj, afm);

	}

	private ArrayList<ViewInformation> orga(ArrayList<MetricsInformation> jak, ArrayList<MetricsInformation> aj,
			ArrayList<MetricsInformation> afm) {
		ArrayList<ViewInformation> nonLeaf = new ArrayList<>();
		ArrayList<String> nonString = new ArrayList<>();

		ArrayList<String> nameJak = new ArrayList<String>();
		ArrayList<String> nameAJ = new ArrayList<String>();
		ArrayList<String> nameAfm = new ArrayList<String>();

		for (int i = 0; i < jak.size(); i++) {
			nameJak.add(jak.get(i).getFeatureName());
		}

		for (int i = 0; i < aj.size(); i++) {
			nameAJ.add(aj.get(i).getFeatureName());
		}

		for (int i = 0; i < afm.size(); i++) {
			nameAfm.add(afm.get(i).getFeatureName());
		}

		for (int i = 0; i < jak.size(); i++) {
			for (int j = 0; j < aj.size(); j++) {
				for (int k = 0; k < afm.size(); k++) {
					// Nonleaf files with the same names (e.g. names of packages)
					if (jak.get(i).getType().equals(Node.NON_LEAF) && aj.get(j).getType().equals(Node.NON_LEAF)
							&& afm.get(k).getType().equals(Node.NON_LEAF)) {

						if (jak.get(i).getFeatureName().equals(aj.get(j).getFeatureName())) {
							// aj == jak == afm
							if (aj.get(j).getFeatureName().equals(afm.get(k).getFeatureName())) {
								ViewInformation vi = new ViewInformation(jak.get(i).getFeatureName(),
										jak.get(i).getType(), orga(jak.get(i).getChildren(), aj.get(j).getChildren(),
												afm.get(k).getChildren()));
								vi.setMetricJakarta(jak.get(i).getMetricValue());
								vi.setMetricAspectJ(aj.get(j).getMetricValue());
								vi.setMetricAfm(afm.get(k).getMetricValue());
								if (!nonLeaf.contains(vi)) {
									nonLeaf.add(vi);
								}
							}
						}
					}
					// Leaf files with the same names
					if (jak.get(i).getType().equals(Node.LEAF) && aj.get(j).getType().equals(Node.LEAF)
							&& afm.get(k).getType().equals(Node.LEAF)) {

						// afm == aj == jak
						if (jak.get(i).getFeatureName().equals(aj.get(j).getFeatureName())
								&& aj.get(j).getFeatureName().equals(afm.get(k).getFeatureName())) {
							ViewInformation vi = new ViewInformation(jak.get(i).getFeatureName(), jak.get(i).getType());
							vi.setMetricJakarta(jak.get(i).getMetricValue());
							vi.setMetricAspectJ(aj.get(j).getMetricValue());
							vi.setMetricAfm(afm.get(k).getMetricValue());
							if (!nonString.contains(vi.getFeatureName())) {
								nonLeaf.add(vi);
								nonString.add(vi.getFeatureName());
							}
						} else {
							// aspectJ only
							if (!nameJak.contains(aj.get(j).getFeatureName())) {
								if (!nameAfm.contains(aj.get(j).getFeatureName())) {
									ViewInformation vi = new ViewInformation(aj.get(j).getFeatureName(),
											aj.get(j).getType());
									vi.setMetricAspectJ(aj.get(j).getMetricValue());
									if (!nonString.contains(vi.getFeatureName())) {
										nonLeaf.add(vi);
										nonString.add(vi.getFeatureName());
									}
								}
								// aspectJ and Afm
								else {
									ViewInformation vi = new ViewInformation(aj.get(j).getFeatureName(),
											aj.get(j).getType());
									vi.setMetricAspectJ(aj.get(j).getMetricValue());
									vi.setMetricAfm(afm.get(k).getMetricValue());
									if (!nonString.contains(vi.getFeatureName())) {
										nonLeaf.add(vi);
										nonString.add(vi.getFeatureName());
									}
								}

							}
							// Jakarta only
							if (!nameAJ.contains(jak.get(i).getFeatureName())) {
								if (!nameAfm.contains(jak.get(i).getFeatureName())) {
									ViewInformation vi = new ViewInformation(jak.get(i).getFeatureName(),
											jak.get(i).getType());
									vi.setMetricJakarta(jak.get(i).getMetricValue());
									if (!nonString.contains(vi.getFeatureName())) {
										nonLeaf.add(vi);
										nonString.add(vi.getFeatureName());
									}
								}
								// Jakarta and AFM
								else {
									ViewInformation vi = new ViewInformation(jak.get(i).getFeatureName(),
											jak.get(i).getType());
									vi.setMetricJakarta(jak.get(i).getMetricValue());
									vi.setMetricAfm(afm.get(k).getMetricValue());
									if (!nonString.contains(vi.getFeatureName())) {
										nonLeaf.add(vi);
										nonString.add(vi.getFeatureName());
									}
								}
							}

							// AFM only
							if (!nameAJ.contains(afm.get(k).getFeatureName())) {
								if (!nameJak.contains(afm.get(k).getFeatureName())) {
									ViewInformation vi = new ViewInformation(afm.get(k).getFeatureName(),
											afm.get(k).getType());
									vi.setMetricAfm(afm.get(k).getMetricValue());
									if (!nonString.contains(vi.getFeatureName())) {
										nonLeaf.add(vi);
										nonString.add(vi.getFeatureName());
									}
								}
								if (nameAJ.contains(jak.get(i).getFeatureName())) {
									ViewInformation vi = new ViewInformation(jak.get(i).getFeatureName(),
											jak.get(i).getType());
									vi.setMetricJakarta(jak.get(i).getMetricValue());
									vi.setMetricAspectJ(aj.get(j).getMetricValue());
									if (!nonString.contains(vi.getFeatureName())) {
										nonLeaf.add(vi);
										nonString.add(vi.getFeatureName());
									}
								}
							}
						}

					}
				}
			}
		}

		return nonLeaf;
	}

	private ArrayList<ViewInformation> organize(ArrayList<MetricsInformation> jak, ArrayList<MetricsInformation> aj,
			ArrayList<MetricsInformation> afm) {
		for (int i = 0; i < jak.size(); i++) {
			for (int j = 0; j < aj.size(); j++) {
				for (int k = 0; k < afm.size(); k++) {
					// metric name
					if (jak.get(i).getType().equals(Node.NON_LEAF) && aj.get(j).getType().equals(Node.NON_LEAF)
							&& afm.get(k).getType().equals(Node.NON_LEAF)) {
						if (jak.get(i).getFeatureName().equals(aj.get(j).getFeatureName())) {
							if (aj.get(j).getFeatureName().equals(afm.get(k).getFeatureName())) {
								ViewInformation vi = new ViewInformation(jak.get(i).getFeatureName(),
										jak.get(i).getType(), orga(jak.get(i).getChildren(), aj.get(j).getChildren(),
												afm.get(k).getChildren()));

								vi.setMetricJakarta(jak.get(i).getMetricValue());
								vi.setMetricAspectJ(aj.get(j).getMetricValue());
								vi.setMetricAfm(afm.get(k).getMetricValue());

								view.add(vi);
							}
						}
					}
				}
			}
		}
		return view;
	}

	public ArrayList<ViewInformation> getView() {
		return view;
	}
}
