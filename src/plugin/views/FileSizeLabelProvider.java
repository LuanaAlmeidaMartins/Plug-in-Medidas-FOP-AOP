package plugin.views;

import javax.swing.JTree;

import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider.IStyledLabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.swt.graphics.Image;

public class FileSizeLabelProvider extends LabelProvider implements IStyledLabelProvider {

        @Override
        public StyledString getStyledText(Object element) {
            if (element instanceof JTree) {
            	JTree file = (JTree) element;
                if (file.getParent()==null) {
                    // a directory is just a container and has no size
                    return new StyledString("0");
                }
                return new StyledString(String.valueOf(file));
            }
            return null;
        }
   
}