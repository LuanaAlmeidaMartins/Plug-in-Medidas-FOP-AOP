package codeAnalysis;


import java.util.ArrayList;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jface.text.Document;

import information.Dependency;

public class VisitorJava extends ASTVisitor {

	private CompilationUnit compilationUnit;
	private ArrayList<String> dependencies, methods;
	private ArrayList<Dependency> dp;
	int i = 0;

	public VisitorJava(ICompilationUnit unit) throws JavaModelException {
		dp = new ArrayList<Dependency>();

		@SuppressWarnings("deprecation")
		ASTParser parser = ASTParser.newParser(AST.JLS8);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setSource(unit);
		parser.setCompilerOptions(JavaCore.getOptions());
		parser.setProject(unit.getJavaProject());
		parser.setResolveBindings(true);
		parser.setBindingsRecovery(true);

		this.compilationUnit = (CompilationUnit) parser.createAST(null);
		this.compilationUnit.accept(this);
	}

	@Override
	public boolean visit(TypeDeclaration node) {
		dependencies = new ArrayList<String>();
		methods = new ArrayList<String>();
		
		String nome = node.getName().toString();
		if(i==0) {
			if(node.getName().toString().contains("$$")) {
				nome = node.getName().toString()+"1";
			}
		}

		Document doc = new Document(node.toString());
		Dependency newDep = new Dependency(nome, dependencies, methods);
		newDep.setNumberOfLines(doc.getNumberOfLines());
		newDep.setAttributes(node.getFields().length);
		newDep.setMethods(node.getMethods().length);

		
		
		i++;

		dp.add(newDep);
		return true;
	}

	@Override
	public boolean visit(FieldDeclaration node) {
		if (!dependencies.contains(node.getType().resolveBinding().getName()) && !node.getType().isPrimitiveType()) {
			dependencies.add(node.getType().resolveBinding().getName());
		}
		return true;
	}

	@Override
	public boolean visit(MethodDeclaration node) {
		if (!methods.contains(node.resolveBinding().getName())) {
			methods.add(node.resolveBinding().getName());
		}
		for (Object o : node.parameters()) {
			if (o instanceof SingleVariableDeclaration) {
				SingleVariableDeclaration svd = (SingleVariableDeclaration) o;
				if (!dependencies.contains(svd.getType().resolveBinding().getName())
						&& !svd.getType().isPrimitiveType()) {
					dependencies.add(svd.getType().resolveBinding().getName());
				}
			}
		}
		return true;
	}

	@Override
	public boolean visit(MethodInvocation node) {
		if (!methods.contains(node.resolveMethodBinding().getName())) {
			methods.add(node.resolveMethodBinding().getName());
		}
		if (!dependencies.contains(node.resolveMethodBinding().getDeclaringClass().getName())) {
			dependencies.add(node.resolveMethodBinding().getDeclaringClass().getName());
		}
		return true;
	}

	@Override
	public boolean visit(ClassInstanceCreation node) {
		if (!node.getType().isPrimitiveType()) {
			if (!dependencies.contains(node.getType().resolveBinding().getName())) {
				dependencies.add(node.getType().resolveBinding().getName());
			}
			if (!methods.contains(node.getType().resolveBinding().getName())) {
				methods.add(node.getType().resolveBinding().getName());
			}
		}
		return true;
	}

	public ArrayList<Dependency> getDependency() {
		for(int i = 0; i < dp.size();i++) {
			for(int j = 0; j < dp.get(i).getDependencias().size();j++) {
				if(dp.get(i).getDependencias().get(j).contains("$$")) {
					dp.get(i).getDependencias().remove(j);
				}
			}
		}
		return dp;
	}

}
