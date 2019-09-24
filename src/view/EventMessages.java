package view;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;

import javafx.scene.control.ProgressBar;
import model.preprocessing.ExtensionFile;

public class EventMessages {

	Shell shell;
	ExecutionEvent event;
	ProgressBar progressBar;

	public EventMessages(ExecutionEvent event) {
		this.event = event;
		this.shell = HandlerUtil.getActiveWorkbenchWindow(event).getShell();
	}

	public void noProjects(ExtensionFile projectIdentification) {

		// If there is no project in workspace
		if (projectIdentification.getAllProjects().length == 0) {
			MessageDialog.openInformation(HandlerUtil.getActiveShell(event), "Information",
					"Please add projects to the workspace!");
		}

		// Create a new modal window to select the SPL projects
		else {
			WizardDialog wizardDialog = new WizardDialog(HandlerUtil.getActiveShell(event).getShell(),
					new Modal(shell, projectIdentification));
			wizardDialog.open();
		}
	}
}
