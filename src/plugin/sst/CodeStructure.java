package plugin.sst;

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
	}

	public ArrayList<FileInformation> getCode() {
		return code;
	}

	/**
	 * The components are separated into: .java files and .jak files. After, they
	 * are associated to a feature.
	 */
	public void jakCodeStructure() throws CoreException {
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

	public void aspectCodeStructure() throws JavaModelException {
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
	}
}
