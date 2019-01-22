package plugin.sst;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.JavaModelException;

/**
 * The XML configuration file is generated with all the features of the SPL
 */
public class XMLGenerator {

	private IProject project;
	private ArrayList<String> features = new ArrayList<String>();

	public XMLGenerator(IProject iProject) {
		this.project = iProject;
		try {
			readModelFile();
			backupConfigurationFiles();
			createConfigurationFile();
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Backup of XML configuration files of SPL products
	 */
	private void backupConfigurationFiles() throws CoreException {
		project.accept(new IResourceVisitor() {

			@Override
			public boolean visit(IResource resource) throws JavaModelException {
				if (resource.getLocation().toString().endsWith("configs")) {
					File fileConfig = new File(resource.getLocation().toString());
					if (fileConfig.isDirectory()) {
						File[] listOfFiles = fileConfig.listFiles();
						for (File file : listOfFiles) {
							if (file.getAbsolutePath().endsWith("xml")) {
								try {
									File newDiretory = new File(resource.getLocation().toString() + "/backup");
									if (!(newDiretory.exists())) {
										newDiretory.mkdir();
									}
									String currentPath = file.getPath().replace("/" + file.getName(), "");
									file.renameTo(new File(currentPath + "/backup", file.getName()));
									file.delete();
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}
					}
				}
				return true;
			}

		});
	}

	/**
	 * Creating a new configuration file with all SPL features.
	 */
	private void createConfigurationFile() throws CoreException {
		project.accept(new IResourceVisitor() {

			@Override
			public boolean visit(IResource resource) throws JavaModelException {
				if (resource.getLocation().toString().endsWith("configs")) {
					File file = new File(resource.getLocation().toString());
					if (file.isDirectory()) {
						file = new File(resource.getLocation().toString() + "/configuration.xml");
						FileWriter fw;
						try {
							fw = new FileWriter(file);
							fw.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n"
									+ "<configuration>\n");
							for (int i = 0; i < features.size(); i++) {
								fw.write("    <feature automatic=\"selected\" name=\"" + features.get(i) + "\"/>\n");
							}
							fw.write("</configuration>");
							fw.close();
						} catch (IOException e) {
							e.printStackTrace();
						}

					}
				}
				try {
					resource.refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());
				} catch (CoreException e) {
					e.printStackTrace();
				}
				return true;
			}

		});
	}

	/**
	 * Extracting the name of each SPL feature.
	 */
	private void readModelFile() throws CoreException {
		project.accept(new IResourceVisitor() {

			@Override
			public boolean visit(IResource resource) throws JavaModelException {
				if (resource instanceof IFile && resource.getName().equals("model.xml")) {
					try {
						FileReader file = new FileReader(resource.getLocation().toString());
						BufferedReader reading = new BufferedReader(file);

						String line = reading.readLine();
						while (line != null) {
							String arrayLine[] = new String[2];
							if (line.contains("name")) {
								arrayLine = line.split("name=");
								String featureName = arrayLine[1];
								featureName = featureName.replaceAll("[^\\w]", "");
								features.add(featureName);
							}
							line = reading.readLine();
						}
						file.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				return true;
			}

		});
	}

}
