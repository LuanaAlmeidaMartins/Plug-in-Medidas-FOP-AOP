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

import plugin.ast.VisitorJava;
import plugin.ast.VisitorAj;
import plugin.metrics.CSC_ConcernSensitiveCoupling;
import plugin.metrics.CD_CyclicalDependency;
import plugin.metrics.DepIn_DependencyIn;
import plugin.metrics.DepOut_DependencyOut;
import plugin.metrics.EFD_ExternalRatioFeatureDependency;
import plugin.metrics.IFD_InternalRatioFeatureDependency;
import plugin.metrics.LCC_LackOfConcernBasedCohesion;
import plugin.metrics.LOC_LinesOfCode;
import plugin.metrics.Metrics;
import plugin.metrics.NA_NumberOfAttributes;
import plugin.metrics.NO_NumberOfOperations;
import plugin.metrics.NSO_NumberOfSharedOperations;
import plugin.metrics.SFC_StructuralFeatureCoupling;
import plugin.persistences.Dependency;
import plugin.persistences.MetricsInformation;
import plugin.persistences.MetricsView;
import plugin.persistences.ViewInformation;
import plugin.sst.CodeFragments;
import plugin.sst.CodeStructure;
import plugin.sst.XMLGenerator;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;

public class SampleHandler extends AbstractHandler {

	public static ExecutionEvent event;
	private ArrayList<Dependency> classesDependencias;

	public static ArrayList<ViewInformation> recomendacoes;
	public static String jakLocation = null, aspectJLocation = null, afmLocation = null;

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		recomendacoes = new ArrayList<>();

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
			classesDependencias = new ArrayList<Dependency>();
			IProject jakartaProject = projectIdentification.getProject(jakLocation);
			XMLGenerator xml = new XMLGenerator(jakartaProject);
			jakartaProject.refreshLocal(IResource.DEPTH_ZERO, null);
			getDependencies(jakartaProject);
			System.out.println(classesDependencias.size()+" ------ ???");
			CodeStructure code = new CodeStructure(jakartaProject);
			CodeFragments fragments = new CodeFragments(code.jakCodeStructure(), classesDependencias);
			fragments.jak();
	//		xml.undo();
			jakartaProject.refreshLocal(IResource.DEPTH_ZERO, null);
			
			// AspectJ project
			classesDependencias = new ArrayList<Dependency>();
			IProject aspectJProject = projectIdentification.getProject(aspectJLocation);
			getDependencies(aspectJProject);
			CodeStructure aspectCode = new CodeStructure(aspectJProject);
			CodeFragments fragmentsAj = new CodeFragments(aspectCode.aspectCodeStructure(), classesDependencias);
			fragmentsAj.aspect();

			// Aspectual Feature Modules project
			classesDependencias = new ArrayList<Dependency>();
			IProject afmProject = projectIdentification.getProject(afmLocation);
			XMLGenerator xmlAfm = new XMLGenerator(afmProject);
			afmProject.refreshLocal(IResource.DEPTH_ZERO, null);
			getDependencies(afmProject);
			CodeStructure afmCode = new CodeStructure(afmProject);
			CodeFragments fragmentsAfm = new CodeFragments(afmCode.jakCodeStructure(), classesDependencias);
			fragmentsAfm.jak();
			xmlAfm.undo();
			afmProject.refreshLocal(IResource.DEPTH_ZERO, null);
			
			MetricsView mv = new MetricsView(callMetrics(fragments), 
					callMetrics(fragments),
					callMetrics(fragments));
			recomendacoes = mv.getView();
			openView();

		} catch (CoreException e) {
			e.printStackTrace();
		}

		return null;
	}

	private ArrayList<MetricsInformation> callMetrics(CodeFragments fragments) {
		ArrayList<MetricsInformation> metricsList = new ArrayList<MetricsInformation>();
		Metrics metric;

		metric = new CD_CyclicalDependency(fragments.getCodeFragments());
		metricsList.addAll(metric.getMetrics());

		metric = new CSC_ConcernSensitiveCoupling(fragments.getCodeFragments());
		metricsList.addAll(metric.getMetrics());

		metric = new DepIn_DependencyIn(fragments.getCodeFragments());
		metricsList.addAll(metric.getMetrics());

		metric = new DepOut_DependencyOut(fragments.getCodeFragments());
		metricsList.addAll(metric.getMetrics());

		metric = new EFD_ExternalRatioFeatureDependency(fragments.getCodeFragments());
		metricsList.addAll(metric.getMetrics());

		metric = new IFD_InternalRatioFeatureDependency(fragments.getCodeFragments());
		metricsList.addAll(metric.getMetrics());

		metric = new LCC_LackOfConcernBasedCohesion(fragments.getCodeFragments());
		metricsList.addAll(metric.getMetrics());

		metric = new LOC_LinesOfCode(fragments.getCodeFragments());
		metricsList.addAll(metric.getMetrics());

		metric = new NA_NumberOfAttributes(fragments.getCodeFragments());
		metricsList.addAll(metric.getMetrics());

		try {
			metric = new NO_NumberOfOperations(fragments.getCodeFragments());
		} catch (JavaModelException e) {
			e.printStackTrace();
		}
		metricsList.addAll(metric.getMetrics());

		metric = new NSO_NumberOfSharedOperations(fragments.getCodeFragments());
		metricsList.addAll(metric.getMetrics());

		metric = new SFC_StructuralFeatureCoupling(fragments.getCodeFragments());
		metricsList.addAll(metric.getMetrics());

		return metricsList;
	}

	private void getDependencies(final IProject project) throws CoreException {
		project.accept(new IResourceVisitor() {

			@Override
			public boolean visit(IResource resource) throws JavaModelException {
				System.out.println(resource.getLocation().toString());
				if (resource instanceof IFile && resource.getLocation().toString().contains("src")
						&& resource.getName().endsWith(".java")) {
					System.out.println("here");
					ICompilationUnit unit = ((ICompilationUnit) JavaCore.create((IFile) resource));
					VisitorJava dp = new VisitorJava(unit);
					classesDependencias.addAll(dp.getDependency());
				}
				if (resource instanceof IFile && resource.getLocation().toString().contains("src")
						&& resource instanceof IFile && resource.getName().endsWith(".aj")) {
					ICompilationUnit unit = ((ICompilationUnit) JavaCore.create((IFile) resource));
					Document doc = new Document(unit.getSource());
					VisitorAj dp = new VisitorAj(doc.get());
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