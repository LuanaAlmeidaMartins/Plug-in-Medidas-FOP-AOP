package plugin.handlers;

import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Shell;

public class ModalWindow extends Wizard {

	protected SelectionWindow selection;
	private Shell shell;
	private ProjectIdentification projectID;

	public ModalWindow(Shell shell, ProjectIdentification projectID) {
		super();
		this.shell = shell;
		this.projectID = projectID;
		setNeedsProgressMonitor(true);
	}

	@Override
	public String getWindowTitle() {
		return "SPL Metrics";
	}

	@Override
	public void addPages() {
		selection = new SelectionWindow(shell, projectID);
		addPage(selection);
	}

	@Override
	public boolean performFinish() {
		return true;
	}
}
