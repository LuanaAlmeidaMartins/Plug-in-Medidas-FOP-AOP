package plugin.handlers;
import java.util.ArrayList;
import java.util.Arrays;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.internal.resources.Project;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.*;
import org.eclipse.jdt.internal.core.JavaProject;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

import plugin.ast.DependencyVisitor;
import plugin.metrics.ConcernSensitiveCouplingCSC;
import plugin.metrics.CyclicalDependencyCD;
import plugin.metrics.LackOfConcernBasedCohesionLCC;
import plugin.metrics.LinesOfCodeLOC;
import plugin.metrics.Metrics;
import plugin.metrics.NumberOfAttributesNA;
import plugin.metrics.NumberOfOperationsNOO;
import plugin.metrics.NumberOfSharedOperationsNSO;
import plugin.persistences.Dependency;
import plugin.persistences.MetricsInformation;
import plugin.persistences.StatusConversa;
import plugin.sst.CodeFragments;
import plugin.sst.CodeStructure;
import plugin.sst.XMLGenerator;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.swt.widgets.Display;

@SuppressWarnings("restriction")
public class SampleHandler extends AbstractHandler {

	public static ExecutionEvent event;
	public static IJavaProject javaProject;
	private ArrayList<Dependency> classesDependencias;

	public static ArrayList<MetricsInformation> recomendacoes;
	
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException   {
		classesDependencias = new ArrayList<Dependency>();
		recomendacoes = new ArrayList<MetricsInformation>();
		
		try {
			SampleHandler.event = event;

			hideView();

			IProject iProject = getProjectFromWorkspace(event);
			if (iProject == null) {
				return null;
			}
			
			new XMLGenerator(iProject);
			iProject.refreshLocal(IResource.DEPTH_ZERO, null);
			getDependencies(iProject);
			CodeStructure code = new CodeStructure(iProject);
			CodeFragments fragments = new CodeFragments(code.getCode(), classesDependencias);
			
			// Metrics
			ConcernSensitiveCouplingCSC csc = new ConcernSensitiveCouplingCSC(fragments.getCodeFragments());
			CyclicalDependencyCD cd = new CyclicalDependencyCD(fragments.getCodeFragments());
			LinesOfCodeLOC loc = new LinesOfCodeLOC(fragments.getCodeFragments());
			NumberOfAttributesNA na = new NumberOfAttributesNA(fragments.getCodeFragments());
			NumberOfOperationsNOO no = new NumberOfOperationsNOO(fragments.getCodeFragments());
			NumberOfSharedOperationsNSO nso = new NumberOfSharedOperationsNSO(fragments.getCodeFragments());
			LackOfConcernBasedCohesionLCC lcc = new LackOfConcernBasedCohesionLCC(fragments.getCodeFragments());
			
			recomendacoes.addAll(csc.getMetrics());
			recomendacoes.addAll(cd.getMetrics());
			recomendacoes.addAll(loc.getMetrics());
			recomendacoes.addAll(na.getMetrics());
			recomendacoes.addAll(no.getMetrics());
			recomendacoes.addAll(nso.getMetrics());
			recomendacoes.addAll(lcc.getMetrics());
			
		openView();

		} catch (JavaModelException e) {
			e.printStackTrace();
		} catch (CoreException e) {
			e.printStackTrace();
		}
		return null;
	}


	private void getDependencies(final IProject project) throws CoreException {
		project.accept(new IResourceVisitor() {

			@Override
			public boolean visit(IResource resource) throws JavaModelException {
				if (resource instanceof IFile && 
						resource.getLocation().toString().contains("src") &&
						resource.getName().endsWith(".java")) {
					ICompilationUnit unit = ((ICompilationUnit) JavaCore.create((IFile) resource));
					DependencyVisitor dp = new DependencyVisitor(unit);
					classesDependencias.addAll(dp.getDependency());
				}
				return true;
			}
		});
	}

	private IProject getProjectFromWorkspace(ExecutionEvent event) {

		TreeSelection selection = (TreeSelection) HandlerUtil.getCurrentSelection(event);

		if (selection == null || selection.getFirstElement() == null) {
			MessageDialog.openInformation(HandlerUtil.getActiveShell(event), "Information", "Please select a project");
			return null;
		}

		JavaProject jp;
		Project p;

		try {
			jp = (JavaProject) selection.getFirstElement();
			return jp.getProject();
		} catch (ClassCastException e) {
			p = (Project) selection.getFirstElement();
			return p.getProject();
		}
	}

	private void hideView() {
		IWorkbenchPage wp = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		IViewPart myView = wp.findView("tp1.views.SampleView");
		wp.hideView(myView);
	}

	private void openView() {
		try {
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView("tp1.views.SampleView");
		} catch (PartInitException e) {
			e.printStackTrace();
		}
	}
	

}