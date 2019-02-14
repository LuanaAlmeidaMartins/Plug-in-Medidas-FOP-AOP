package plugin.views;

import org.eclipse.jface.viewers.ArrayContentProvider;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import plugin.metrics.Node;
import plugin.persistences.ViewInformation;

// Tutorial 
// http://www.vogella.com/tutorials/EclipseJFaceTree/article.html#prerequisites
public class TreeContentProvider implements ITreeContentProvider {

	@Override
	public boolean hasChildren(Object element) {
		ViewInformation mi = (ViewInformation) element;
		if (mi.getType().equals(Node.LEAF)) {
			return false;
		}
		return true;
	}

	@Override
	public Object getParent(Object element) {
		return null;
	}

	@Override
	public Object[] getElements(Object inputElement) {
		return ArrayContentProvider.getInstance().getElements(inputElement);
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		ViewInformation mi = (ViewInformation) parentElement;
		ViewInformation[] stockArr = new ViewInformation[mi.getChildren().size()];
		stockArr = mi.getChildren().toArray(stockArr);
		return stockArr;
	}

	public void inputChanged(Viewer v, Object oldInput, Object newInput) {
	}
}
