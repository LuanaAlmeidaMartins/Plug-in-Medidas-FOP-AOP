package view;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class Perspective implements IPerspectiveFactory {

	public void createInitialLayout(IPageLayout layout) {

		layout.addStandaloneView(View.ID, true, IPageLayout.LEFT, 1.0f, layout.getEditorArea());
		layout.setEditorAreaVisible(false); // hide the editor in the perspective

	}

}