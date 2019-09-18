package view;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

public class CustomAction extends Action implements IWorkbenchAction {

	public static final String ID2 = "com.timmolter.helloWorld.CustomAction";

	public CustomAction() {
		setId(ID2);
	}

	public void run() {

		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
		String dialogBoxTitle = "Information";
		String message = "CSV exported successfully!";
		MessageDialog.openInformation(shell, dialogBoxTitle, message);
	}

	public void dispose() {
	}

}