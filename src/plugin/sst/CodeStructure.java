package plugin.sst;

import java.io.File;
import java.util.ArrayList;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.JavaModelException;

import plugin.persistences.FileInformation;

/**
 * The measures are given at feature level, so class identifies the components
 * belonging to each feature.
 */
public class CodeStructure {

	private ArrayList<FileInformation> code = new ArrayList<FileInformation>();
	private IProject project;

	public CodeStructure(IProject iProject) {
		this.project = iProject;
		try {
			codeStructure();
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	public ArrayList<FileInformation> getCode() {
		return code;
	}

	/**
	 * The components are separated into: .java files and .jak files. After, they
	 * are associated to a feature.
	 */
	private void codeStructure() throws CoreException {
		project.accept(new IResourceVisitor() {

			@Override
			public boolean visit(IResource resource) throws JavaModelException {
				if (resource.getLocation().toString().contains("features")
						&& (!resource.getLocation().toString().endsWith("features"))) {
					File file = new File(resource.getLocation().toString());
					if (file.isDirectory()) {
						String featureName = resource.getName();
						ArrayList<String> jakFiles = new ArrayList<String>();
						ArrayList<String> javaFiles = new ArrayList<String>();
						File[] listOfFiles = file.listFiles();
						for (File f : listOfFiles) {
							String arrayLine[] = new String[2];
							arrayLine = f.getName().split("\\.");
							if (arrayLine[1].equals("java")) {
								javaFiles.add(arrayLine[0]);
							}
							if (arrayLine[1].equals("jak")) {
								jakFiles.add(arrayLine[0]);
							}
						}
						code.add(new FileInformation(featureName, javaFiles, jakFiles));
					}
				}
				return true;
			}
		});
	}
}
