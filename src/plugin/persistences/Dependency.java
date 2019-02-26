package plugin.persistences;

import java.util.ArrayList;

public class Dependency {

	private String classe;
	private ArrayList<String> dependencias;
	private ArrayList<String> methodsCalled;
	private int lines = 0;
	private int attributes = 0;
	private int methods = 0;

	public Dependency(String classe, ArrayList<String> dependencias, ArrayList<String> methods) {
		this.classe = classe;
		this.dependencias = dependencias;
		this.methodsCalled = methods;
	} 

	public ArrayList<String> getDependencias() {
		return dependencias;
	}

	public String getClasseName() {
		return classe;
	}

	public ArrayList<String> getMethodsCalled() {
		return methodsCalled;
	}

	public void setNumberOfLines(int lines) {
		this.lines = lines;
	}

	public int getNumberOfLines() {
		return lines;
	}

	public int getAttributes() {
		return attributes;
	}

	public void setAttributes(int attributes) {
		this.attributes = attributes;
	}

	public int getMethods() {
		return methods;
	}

	public void setMethods(int methods) {
		this.methods = methods;
	}

	public void setClassName(String string) {
		this.classe = string;
	}

}
