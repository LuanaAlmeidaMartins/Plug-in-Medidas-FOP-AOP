package plugin.handlers;

import org.eclipse.jface.bindings.keys.KeyStroke;
import org.eclipse.jface.bindings.keys.ParseException;
import org.eclipse.jface.fieldassist.ContentProposalAdapter;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.jface.fieldassist.SimpleContentProposalProvider;
import org.eclipse.jface.fieldassist.TextContentAdapter;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class SelectionWindow extends WizardPage {

	// http://www.vogella.com/tutorials/EclipseWizards/article.html#wizards

	private Composite container;
	private ProjectIdentification projectID;
	private int enableCalculate = 0;

	public SelectionWindow(Shell shell, ProjectIdentification projectID2) {
		super("SPLMetrics");
		this.projectID = projectID2;
		setTitle("Projects Selection");
		setDescription("Select the SPL projects implemented in paradigms: (i) Feature Oriented, with Jakarta language;"
				+ " (ii) Aspect Oriented, \nwith AspectJ language, and;"
				+ "(iii) Aspectual Feature Mudules, with AspectJ and Jakarta language.");
	}

	@Override
	public void createControl(Composite parent) {
		// Create a Modal
		container = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 2;

		// Set buttons on modal window
		aheadSelection();
		aspectJSelection();
		afmSelection();

		// disable button finish
		setPageComplete(false);
		setControl(container);
	}

	private void aheadSelection() {
		// create Feature Oriented (Jakarta) container
		Label jakLabel = new Label(container, SWT.NONE);
		jakLabel.setText("Feature Oriented: ");

		Text jakText = new Text(container, SWT.BORDER);
		ControlDecoration deco = createDecoration(jakText);

		// set a project name
		jakText.addModifyListener(e -> {
			Text source = (Text) e.getSource();
			if (!source.getText().isEmpty()) {
				deco.hide();
				enableCalculate++;
				SampleHandler.jakLocation = source.getText();
				enableFinishButton();
			} else {
				deco.show();
			}
		});

		autoComplete(jakText, projectID.getListJakarta());
	}

	private void aspectJSelection() {
		// create Aspect Oriented (AspectJ) container
		Label ajLabel = new Label(container, SWT.NONE);
		ajLabel.setText("Aspect Oriented: ");

		Text ajText = new Text(container, SWT.BORDER);
		ControlDecoration deco = createDecoration(ajText);

		// set project name
		ajText.addModifyListener(e -> {
			Text source = (Text) e.getSource();
			if (!source.getText().isEmpty()) {
				deco.hide();
				enableCalculate++;
				SampleHandler.aspectJLocation = source.getText();
				enableFinishButton();
			} else {
				deco.show();
			}
		});

		autoComplete(ajText, projectID.getListAspectJ());
	}

	private void afmSelection() {
		// create afm container selection
		Label afmLabel = new Label(container, SWT.NONE);
		afmLabel.setText("Aspectual Features Modules: ");

		Text afmText = new Text(container, SWT.BORDER);
		// create the decoration for the text component

		ControlDecoration deco = createDecoration(afmText);

		// hide the decoration if the text widget has content
		afmText.addModifyListener(e -> {
			Text source = (Text) e.getSource();
			if (!source.getText().isEmpty()) {
				deco.hide();
				enableCalculate++;
				SampleHandler.afmLocation = source.getText();
				enableFinishButton();
			} else {
				deco.show();
			}
		});

		autoComplete(afmText, projectID.getListAFM());
	}

	private void enableFinishButton() {
		if (enableCalculate == 3) {
			setPageComplete(true);
		}
	}

	private void autoComplete(Text text, String[] projects) {
		// Define field assists for the text widget
		// use "." and " " activate the content proposals
		char[] autoActivationCharacters = new char[] { '.', ' ' };
		KeyStroke keyStroke;
		try {
			keyStroke = KeyStroke.getInstance("Ctrl+Space");
			new ContentProposalAdapter(text, new TextContentAdapter(), new SimpleContentProposalProvider(projects),
					keyStroke, autoActivationCharacters);
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
	}

	private ControlDecoration createDecoration(Text text) {
		// create the decoration for the text component
		GridData gd_text = new GridData(SWT.FILL, SWT.CENTER, true, false);
		gd_text.horizontalIndent = 8;
		text.setLayoutData(gd_text);

		ControlDecoration deco = new ControlDecoration(text, SWT.TOP | SWT.LEFT);
		Image image = FieldDecorationRegistry.getDefault().getFieldDecoration(FieldDecorationRegistry.DEC_INFORMATION)
				.getImage();
		deco.setDescriptionText("Use CTRL + SPACE to see possible values");
		deco.setImage(image);
		deco.setShowOnlyOnFocus(false);

		return deco;
	}

}
