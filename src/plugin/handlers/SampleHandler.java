package plugin.handlers;

import java.util.ArrayList;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.*;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

import plugin.ast.DependencyVisitor;
import plugin.metrics.ConcernSensitiveCouplingCSC;
import plugin.metrics.CyclicalDependencyCD;
import plugin.metrics.ExternalRatioFeatureDependency_EFD;
import plugin.metrics.LackOfConcernBasedCohesionLCC;
import plugin.metrics.LinesOfCodeLOC;
import plugin.metrics.Metrics;
import plugin.metrics.NumberOfAttributesNA;
import plugin.metrics.NumberOfOperationsNOO;
import plugin.metrics.NumberOfSharedOperationsNSO;
import plugin.persistences.Dependency;
import plugin.persistences.MetricsInformation;
import plugin.sst.CodeFragments;
import plugin.sst.CodeStructure;
import plugin.sst.XMLGenerator;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;

public class SampleHandler extends AbstractHandler {

	public static ExecutionEvent event;
	public static IJavaProject javaProject;
	private ArrayList<Dependency> classesDependencias;

	public static ArrayList<MetricsInformation> recomendacoes;
	public static String jakLocation = null, aspectJLocation = null, afmLocation = null;

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		classesDependencias = new ArrayList<Dependency>();
		recomendacoes = new ArrayList<MetricsInformation>();

		SampleHandler.event = event;
		Shell shell = HandlerUtil.getActiveWorkbenchWindow(event).getShell();

		hideView();

		ProjectIdentification projectIdentification = new ProjectIdentification();

		// If there is no project in workspace
		if (projectIdentification.getAllProjects().length == 0) {
			MessageDialog.openInformation(HandlerUtil.getActiveShell(event), "Information",
					"Please add projects to the workspace!");
		}

		// Create a new modal window to select the SPL projects
		else {
			WizardDialog wizardDialog = new WizardDialog(HandlerUtil.getActiveShell(event).getShell(),
					new ModalWindow(shell, projectIdentification));
			wizardDialog.open();
		}

		try {
			// Jakarta project
			IProject jakartaProject = projectIdentification.getProject(jakLocation);
			new XMLGenerator(jakartaProject);
			jakartaProject.refreshLocal(IResource.DEPTH_ZERO, null);
			getDependencies(jakartaProject);
			CodeStructure code = new CodeStructure(jakartaProject);
			CodeFragments fragments = new CodeFragments(code.getCode(), classesDependencias);
			callMetrics(fragments);

			openView();

		} catch (CoreException e) {
			e.printStackTrace();
		}

		return null;
	}

	private void callMetrics(CodeFragments fragments) {
		Metrics metric;
		
		metric = new ConcernSensitiveCouplingCSC(fragments.getCodeFragments());
		recomendacoes.addAll(metric.getMetrics());
		
		metric = new CyclicalDependencyCD(fragments.getCodeFragments());
		recomendacoes.addAll(metric.getMetrics());
		
		metric = new LinesOfCodeLOC(fragments.getCodeFragments());
		recomendacoes.addAll(metric.getMetrics());
		
		metric= new NumberOfAttributesNA(fragments.getCodeFragments());
		recomendacoes.addAll(metric.getMetrics());
		
		try {
			metric= new NumberOfOperationsNOO(fragments.getCodeFragments());
		} catch (JavaModelException e) {
			e.printStackTrace();
		}
		recomendacoes.addAll(metric.getMetrics());
		
		metric= new NumberOfSharedOperationsNSO(fragments.getCodeFragments());
		recomendacoes.addAll(metric.getMetrics());
		
		metric= new LackOfConcernBasedCohesionLCC(fragments.getCodeFragments());
		recomendacoes.addAll(metric.getMetrics());
		
		metric = new ExternalRatioFeatureDependency_EFD(fragments.getCodeFragments());
	}

	private void getDependencies(final IProject project) throws CoreException {
		project.accept(new IResourceVisitor() {

			@Override
			public boolean visit(IResource resource) throws JavaModelException {
				if (resource instanceof IFile && resource.getLocation().toString().contains("src")
						&& resource.getName().endsWith(".java")) {
					ICompilationUnit unit = ((ICompilationUnit) JavaCore.create((IFile) resource));
					DependencyVisitor dp = new DependencyVisitor(unit);
					classesDependencias.addAll(dp.getDependency());
				}
				return true;
			}
		});
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