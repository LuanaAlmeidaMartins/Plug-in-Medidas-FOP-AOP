package plugin.persistences;

import java.util.ArrayList;
import org.eclipse.jdt.core.IType;

public class Dependency {

	private String newClassName;
	private IType classe;
	private ArrayList<String> dependencias;
	private ArrayList<String> methods;

	public Dependency(IType classe, ArrayList<String> dependencias, ArrayList<String> methods) {
		this.classe = classe;
		this.dependencias = dependencias;
		this.methods = methods;
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
	
	public ArrayList<String> getMethods() {
		return methods;
	}
}
