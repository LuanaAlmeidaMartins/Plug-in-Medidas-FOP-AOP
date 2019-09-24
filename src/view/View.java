package view;

import org.eclipse.jface.action.Separator;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.part.ViewPart;

import controller.StartController;
import controller.ViewController;
import model.information.ViewInformation;

public class View extends ViewPart {

	public static final String ID = "tp1.views.SampleView";

	private TreeViewer viewer;

	public View() {
		
	}

	public void createPartControl(Composite parent) {

		// Custom Action for the View's Menu
		CustomAction lCustomAction = new CustomAction();
		lCustomAction.setText("Export to CSV");
		getViewSite().getActionBars().getMenuManager().add(lCustomAction);
		getViewSite().getActionBars().getMenuManager().add(new Separator()); // Add a horizontal separator
			
		String[] titles = { "Metric ", "Values to Feature Oriented", "Values to  Aspect Oriented",
		"Values to Aspectual Feature Modules" };
		int[] bounds = { 300, 200, 200, 200 };

		viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		viewer.setContentProvider(new ViewController());
		viewer.getTree().setHeaderVisible(true);
		//      viewer.getTree().setLinesVisible(true);

		TreeViewerColumn mainColumn = createTableViewerColumn(titles[0], bounds[0], 0);
		mainColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				ViewInformation v = (ViewInformation) element;
				return v.getFeatureName();
			}
		});

		TreeViewerColumn valueColumn = createTableViewerColumn(titles[1], bounds[1], 1);
		valueColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				ViewInformation v = (ViewInformation) element;
				return v.getMetricJakarta();
			}
		});

		TreeViewerColumn valueColumn2 = createTableViewerColumn(titles[2], bounds[2], 2);
		valueColumn2.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				ViewInformation v = (ViewInformation) element;
				return v.getMetricAspectJ();
			}
		});

		TreeViewerColumn valueColumn3 = createTableViewerColumn(titles[3], bounds[3], 3);
		valueColumn3.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				ViewInformation v = (ViewInformation) element;
				return v.getMetricAfm();
			}
		});

		GridLayoutFactory.fillDefaults().generateLayout(parent);
		viewer.setInput(StartController.recomendacoes);

		Tree tree = (Tree) viewer.getControl();

		tree.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TreeItem item = (TreeItem) e.item;
				if (item.getItemCount() > 0) {
					item.setExpanded(!item.getExpanded());
					// update the viewer
					viewer.refresh();
				}
			}
		});

		viewer.addDoubleClickListener(new IDoubleClickListener() {
			@Override
			public void doubleClick(DoubleClickEvent event) {
				TreeViewer viewer = (TreeViewer) event.getViewer();
				IStructuredSelection thisSelection = (IStructuredSelection) event.getSelection();
				Object selectedNode = thisSelection.getFirstElement();
				viewer.setExpandedState(selectedNode, !viewer.getExpandedState(selectedNode));
			}
		});

	}

	public void setFocus() {
		viewer.getControl().setFocus();
	}

	private TreeViewerColumn createTableViewerColumn(String title, int bound, final int colNumber) {
		final TreeViewerColumn viewerColumn = new TreeViewerColumn(viewer, SWT.CENTER | SWT.WRAP);
		final TreeColumn column = viewerColumn.getColumn();
		column.setText(title);
		column.setWidth(bound);
		column.setResizable(true);
		column.setMoveable(true);
		return viewerColumn;
	}
}