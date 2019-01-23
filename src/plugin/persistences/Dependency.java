package plugin.persistences;

import java.util.ArrayList;
import org.eclipse.jdt.core.IType;

public class Dependency {
	
	private String newClassName;
	private IType classe;
	private ArrayList<String> dependencias;
	
	public Dependency(IType classe, ArrayList<String> dependencias){
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
	
	public String getClasseName(){
		return classe.getElementName();
	}
}
