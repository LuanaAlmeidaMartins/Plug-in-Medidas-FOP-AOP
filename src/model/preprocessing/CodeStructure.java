package model.preprocessing;

import java.io.File;
import java.util.ArrayList;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;

import model.information.FileInformation;

/**
 * The measures are given at feature level, so class identifies the components
 * belonging to each feature.
 */
public class CodeStructure {

	private IProject project;

	public CodeStructure(IProject iProject) {
		this.project = iProject;
	}

	/**
	 * The components are separated into: .java files and .jak files. After, they
	 * are associated to a feature.
	 * 
	 * @return
	 */
	public ArrayList<FileInformation> jakCodeStructure() throws CoreException {
		ArrayList<FileInformation> code = new ArrayList<FileInformation>();
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
						// System.out.println(Arrays.toString(listOfFiles));
						for (File f : listOfFiles) {
							if (f.isDirectory()) {
								// System.out.println("nothing " + f.getName());
							} else {
								String arrayLine[] = new String[2];
								arrayLine = f.getName().split("\\.");
								if (arrayLine[1].equals("java") || arrayLine[1].equals("aj")) {
									javaFiles.add(arrayLine[0]);
								}
								if (arrayLine[1].equals("jak")) {
									jakFiles.add(arrayLine[0]);
								}
							}
						}
						code.add(new FileInformation(featureName, javaFiles, jakFiles));
					}
				}
				return true;
			}
		});
		return code;
	}

	public ArrayList<FileInformation> aspectCodeStructure() throws JavaModelException {
		ArrayList<FileInformation> code = new ArrayList<FileInformation>();
		IJavaProject javaProject = JavaCore.create(project);
		IPackageFragment[] packages = javaProject.getPackageFragments();
		for (IPackageFragment mypackage : packages) {
			if (mypackage.getKind() == IPackageFragmentRoot.K_SOURCE) {
				String featureName = mypackage.getElementName();
				ArrayList<String> javaFiles = new ArrayList<String>();
				for (ICompilationUnit unit : mypackage.getCompilationUnits()) {
					String arrayLine[] = new String[2];
					arrayLine = unit.getElementName().split("\\.");
					if (arrayLine[1].equals("java") || arrayLine[1].equals("aj")) {
						if (!javaFiles.contains(arrayLine[0])) {
							javaFiles.add(arrayLine[0]);
						}
					}
				}
				code.add(new FileInformation(featureName, javaFiles));
			}
		}
		for (int i = 0; i < code.size(); i++) {
			if (code.get(i).getFeatureName().isEmpty()) {
				code.remove(i);
			}
		}
		return code;
	}
}
