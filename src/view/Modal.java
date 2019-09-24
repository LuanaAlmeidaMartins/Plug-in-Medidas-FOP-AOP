package view;

import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Shell;

import controller.ModalController;
import model.preprocessing.ExtensionFile;

public class Modal extends Wizard {

	protected ModalController selection;
	private Shell shell;
	private ExtensionFile projectID;

	public Modal(Shell shell, ExtensionFile projectID) {
		super();
		this.shell = shell;
		this.projectID = projectID;
		setNeedsProgressMonitor(true);
	}

	@Override
	public String getWindowTitle() {
		return "SPLiME - Software Product Line Maintainability Evaluation";
	}

	@Override
	public void addPages() {
		selection = new ModalController(shell, projectID);
		addPage(selection);
	}

	@Override
	public boolean performFinish() {
		return true;
	}
}
