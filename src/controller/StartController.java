package controller;

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

import model.codeAnalysis.VisitorAj;
import model.codeAnalysis.VisitorJava;
import model.information.Dependency;
import model.information.MetricsInformation;
import model.information.MetricsView;
import model.information.Tecnologia;
import model.information.ViewInformation;
import model.metrics.DepIn_DependencyIn;
import model.metrics.DepOut_DependencyOut;
import model.metrics.EFD_ExternalRatioFeatureDependency;
import model.metrics.IFD_InternalRatioFeatureDependency;
import model.metrics.LCC_LackOfConcernBasedCohesion;
import model.metrics.LOC_LinesOfCode;
import model.metrics.Metrics;
import model.metrics.NA_NumberOfAttributes;
import model.metrics.NO_NumberOfOperations;
import model.metrics.SFC_StructuralFeatureCoupling;
import model.preprocessing.CodeFragments;
import model.preprocessing.CodeStructure;
import model.preprocessing.ExtensionFile;
import model.preprocessing.XMLGenerator;
import view.EventMessages;
import org.eclipse.jface.text.Document;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

public class StartController extends AbstractHandler {

	public static ExecutionEvent event;
	private ArrayList<Dependency> classesDependencias;

	public static ArrayList<ViewInformation> recomendacoes;
	public static String jakLocation = null, aspectJLocation = null, afmLocation = null;
	ArrayList<MetricsInformation> afmMI = new ArrayList<>();
	ArrayList<MetricsInformation> ajMI = new ArrayList<>();
	ArrayList<MetricsInformation> jakMI = new ArrayList<>();

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		recomendacoes = new ArrayList<>();

		StartController.event = event;

		hideView();

		ExtensionFile projectIdentification = new ExtensionFile();
		EventMessages treat = new EventMessages(event);
		treat.noProjects(projectIdentification);
		
		
		if((afmLocation!=null) && (aspectJLocation!=null) && (jakLocation!=null)) {
			try {
				jakVerify(projectIdentification);
				aspectVerify(projectIdentification);
				afmVerify(projectIdentification);

				MetricsView mv = new MetricsView(jakMI, ajMI, afmMI);

				recomendacoes = mv.getView();
				openView();
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
		
		if(afmLocation==null) {
			if(aspectJLocation==null) {
				try {
					jakVerify(projectIdentification);
					MetricsView mv = new MetricsView(jakMI, Tecnologia.JAK);
					
					recomendacoes = mv.getView();
					openView();
				} catch (CoreException e) {
					e.printStackTrace();
				}
			}
			else {
				// DO IT
//				try {
//					jakVerify(projectIdentification);
//					MetricsView mv = new MetricsView(jakMI, ajMI);
//		
//					recomendacoes = mv.getView();
//					openView();
//				} catch (CoreException e) {
//					e.printStackTrace();
//				}
			}
		}
		
		if(jakLocation==null) {
			if(aspectJLocation==null) {
				try {
					afmVerify(projectIdentification);
					MetricsView mv = new MetricsView(afmMI, Tecnologia.AFM);
					
					recomendacoes = mv.getView();
					openView();
				} catch (CoreException e) {
					e.printStackTrace();
				}
			}
			else {
				// DO IT
//				try {
//					jakVerify(projectIdentification);
//					MetricsView mv = new MetricsView(afmMI, ajMI);
//		
//					recomendacoes = mv.getView();
//					openView();
//				} catch (CoreException e) {
//					e.printStackTrace();
//				}
			}
		}
		
		if(jakLocation==null) {
			if(afmLocation==null) {
				try {
					aspectVerify(projectIdentification);
					MetricsView mv = new MetricsView(ajMI, Tecnologia.AJ);
					
					recomendacoes = mv.getView();
					openView();
				} catch (CoreException e) {
					e.printStackTrace();
				}
			}
		}
		
		return null;
	}

	private void jakVerify(ExtensionFile projectIdentification) throws CoreException {
			classesDependencias = new ArrayList<Dependency>();
			IProject jakartaProject = projectIdentification.getProject(jakLocation);
			XMLGenerator xml = new XMLGenerator(jakartaProject);
			jakartaProject.refreshLocal(IResource.DEPTH_ZERO, null);
			getDependencies(jakartaProject);
			CodeStructure code = new CodeStructure(jakartaProject);
			CodeFragments fragments = new CodeFragments(code.jakCodeStructure(), classesDependencias);
			fragments.jak();
			//xml.undo();
			jakartaProject.refreshLocal(IResource.DEPTH_ZERO, null);
			jakMI.addAll(callMetrics(fragments));
	}

	private void afmVerify(ExtensionFile projectIdentification) throws CoreException {
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
			afmMI.addAll(callMetrics(fragmentsAfm));
	}

	private void aspectVerify(ExtensionFile projectIdentification) throws CoreException {
		// AspectJ project
			classesDependencias = new ArrayList<Dependency>();
			IProject aspectJProject = projectIdentification.getProject(aspectJLocation);
			getDependencies(aspectJProject);
			CodeStructure aspectCode = new CodeStructure(aspectJProject);
			CodeFragments fragmentsAj = new CodeFragments(aspectCode.aspectCodeStructure(), classesDependencias);
			fragmentsAj.aspect();
			ajMI.addAll(callMetrics(fragmentsAj));
	}

	private ArrayList<MetricsInformation> callMetrics(CodeFragments fragments) {
		ArrayList<MetricsInformation> metricsList = new ArrayList<MetricsInformation>();
		Metrics metric;

		// metric = new CD_CyclicalDependency(fragments.getCodeFragments());
		// metricsList.addAll(metric.getMetrics());
		//
		// metric = new CSC_ConcernSensitiveCoupling(fragments.getCodeFragments());
		// metricsList.addAll(metric.getMetrics());

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
		//
		// metric = new NSO_NumberOfSharedOperations(fragments.getCodeFragments());
		// metricsList.addAll(metric.getMetrics());

		metric = new SFC_StructuralFeatureCoupling(fragments.getCodeFragments());
		metricsList.addAll(metric.getMetrics());

		return metricsList;
	}

	private void getDependencies(final IProject project) throws CoreException {
		project.accept(new IResourceVisitor() {

			@Override
			public boolean visit(IResource resource) throws JavaModelException {
				if (resource instanceof IFile && resource.getLocation().toString().contains("src")
						&& resource.getName().endsWith(".java")) {
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

	public void hideView() {
		IWorkbenchPage wp = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		IViewPart myView = wp.findView("tp1.views.SampleView");
		wp.hideView(myView);
	}

	public void openView() {
		try {
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView("tp1.views.SampleView");
		} catch (PartInitException e) {
			e.printStackTrace();
		}
		recomendacoes = null;
		jakLocation = null;
		aspectJLocation = null;
		afmLocation = null;
		afmMI = new ArrayList<>();
		jakMI = new ArrayList<>();
		ajMI = new ArrayList<>();
		
	}

}