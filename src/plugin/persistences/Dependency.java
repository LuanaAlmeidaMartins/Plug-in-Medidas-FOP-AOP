package plugin.persistences;

import java.util.ArrayList;

import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;

public class Dependency {

	private String newClassName;
	private IType classe;
	private ArrayList<String> dependencias;

	public Dependency(IType classe, ArrayList<String> dependencias) {
		this.classe = classe;
		this.dependencias = dependencias;
	}

	public String getNewClassName() {
		return newClassName;
	}

	public void setNewClassName(String name) {
		this.newClassName = name;
	}

	public IType getClasse() {
		return classe;
	}

	public ArrayList<String> getDependencias() {
		return dependencias;
	}

	public String getClasseName() {
		return classe.getElementName();
	}

	public IMethod[] getMethods() {
		IMethod methods[] = null;
		try {
			methods = classe.getMethods();
		} catch (JavaModelException e) {
			e.printStackTrace();
		}
		return methods;
		// for (IMethod method : methods) {
		// System.out.println(method.getElementName());
		// }
	}
}
