package plugin.handlers;

import java.io.File;
import java.util.ArrayList;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.JavaModelException;

public class ProjectIdentification {

	private IProject[] projects;
	private ArrayList<IProject> listJakarta = new ArrayList<IProject>();
	private ArrayList<IProject> listAspectJ = new ArrayList<IProject>();
	private ArrayList<IProject> listAFM = new ArrayList<IProject>();

	public ProjectIdentification() {
		this.projects = getProjectsFromWorkspace();
		try {
			createList();
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}
	
	private IProject[] getProjectsFromWorkspace() {
		// Get the root of the workspace
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot root = workspace.getRoot();

		// Get all projects in the workspace
		return root.getProjects();
	}

	// Separe the projects into list to present in SelectionWindow
	private void createList() throws CoreException {

		for (IProject project : projects) {
			project.accept(new IResourceVisitor() {
				@Override
				public boolean visit(IResource resource) throws JavaModelException {
					// If the project is Feature Oriented, contains .jak file
					if (resource.getLocation().toString().contains("features")
							&& (!resource.getLocation().toString().endsWith("features"))) {
						File file = new File(resource.getLocation().toString());
						if (file.isDirectory()) {
							File[] listOfFiles = file.listFiles();
							for (File f : listOfFiles) {
								if (f.getName().contains("jak")) {
									if (!listJakarta.contains(project)) {
										listJakarta.add(project);
									}
								}
							}
						}
					}
					// If the project is Aspect Oriented, contains .aj file
					if (resource instanceof IFile && resource.getName().endsWith(".aj")) {
						if (!listAspectJ.contains(project)) {
							listAspectJ.add(project);
						}
					}
					return true;
				}
			});
		}

		// If the project is Aspectual Features Modules, contains .jak and .aj file
		// Check if the project is on the listJakarta and on the listAspectJ
		for (int i = 0; i < listJakarta.size(); i++) {
			for (int j = 0; j < listAspectJ.size(); j++) {
				if (listAspectJ.get(j).equals(listJakarta.get(i))) {
					listAFM.add(listJakarta.get(i));
					listJakarta.remove(i);
					listAspectJ.remove(j);
				}
			}
		}
	}

	private String[] convert(ArrayList<IProject> list) {
		String[] names = new String[list.size()];
		for(int i = 0; i < list.size();i++) {
			names[i] = list.get(i).getName();
		}
		return names;
	}
	
	public String[] getListAFM() {
		return convert(listAFM);
	}

	public String[] getListAspectJ() {
		return convert(listAspectJ);
	}

	public String[] getListJakarta() {
		return convert(listJakarta);
	}
	
	public IProject[] getAllProjects() {
		return projects;
	}

	public IProject getProject(String nameProject) {
		for(IProject project : projects) {
			if(project.getName().equals(nameProject)) {
				return project;
			}
		}
		return null;
	}
}
