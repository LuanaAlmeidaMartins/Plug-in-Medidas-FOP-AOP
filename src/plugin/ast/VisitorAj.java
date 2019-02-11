package plugin.ast;

import java.util.ArrayList;
import java.util.HashMap;

import org.aspectj.org.eclipse.jdt.core.dom.AST;
import org.aspectj.org.eclipse.jdt.core.dom.ASTParser;
import org.aspectj.org.eclipse.jdt.core.dom.AfterAdviceDeclaration;
import org.aspectj.org.eclipse.jdt.core.dom.AfterReturningAdviceDeclaration;
import org.aspectj.org.eclipse.jdt.core.dom.AfterThrowingAdviceDeclaration;
import org.aspectj.org.eclipse.jdt.core.dom.AjASTVisitor;
import org.aspectj.org.eclipse.jdt.core.dom.AroundAdviceDeclaration;
import org.aspectj.org.eclipse.jdt.core.dom.BeforeAdviceDeclaration;
import org.aspectj.org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.aspectj.org.eclipse.jdt.core.dom.CompilationUnit;
import org.aspectj.org.eclipse.jdt.core.dom.FieldDeclaration;
import org.aspectj.org.eclipse.jdt.core.dom.ImportDeclaration;
import org.aspectj.org.eclipse.jdt.core.dom.MethodDeclaration;
import org.aspectj.org.eclipse.jdt.core.dom.MethodInvocation;
import org.aspectj.org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.aspectj.org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jface.text.Document;

import plugin.persistences.Dependency;

public class VisitorAj extends AjASTVisitor {

	CompilationUnit compilationUnit;
	private ArrayList<String> dependencies, methods;
	private ArrayList<Dependency> dp;
	private Dependency newDep;
	private ArrayList<String> imported = new ArrayList<String>();

	@SuppressWarnings("rawtypes")
	public VisitorAj(String source) {
		dp = new ArrayList<Dependency>();

		@SuppressWarnings("deprecation")
		ASTParser parser = ASTParser.newParser(AST.JLS2);
		parser.setCompilerOptions(new HashMap());
		parser.setSource(source.toCharArray());
		parser.setResolveBindings(true);
		parser.setStatementsRecovery(true);
		parser.setBindingsRecovery(true);

		this.compilationUnit = (CompilationUnit) parser.createAST(null);
		this.compilationUnit.accept(this);
	}

	
	@Override
	public boolean visit(TypeDeclaration node) {
		dependencies = new ArrayList<String>();
		methods = new ArrayList<String>();
		
		if(imported.size()>0) {
			dependencies.addAll(imported);
		}
		Document doc = new Document(node.toString());
		newDep = new Dependency(node.getName().toString(), dependencies, methods);
		newDep.setNumberOfLines(doc.getNumberOfLines());
		newDep.setAttributes(node.getFields().length);
		newDep.setMethods(node.getMethods().length);

		dp.add(newDep);

		return true;
	}

	@Override
	public boolean visit(FieldDeclaration node) {
		if (!dependencies.contains(node.getType().toString()) && !node.getType().isPrimitiveType()) {
			dependencies.add(node.getType().toString());
		}
		return true;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean visit(ClassInstanceCreation node) {
		String a = node.getName() + "";

		if (!methods.contains(a)) {
			methods.add(a);
		}
		if (!dependencies.contains(a)) {
			dependencies.add(a);
		}
		
		return true;
	}

	@Override
	public boolean visit(MethodDeclaration node) {
		if (!methods.contains(node.getName().toString())) {
			methods.add(node.getName().toString());
		}
		for (Object o : node.parameters()) {
			if (o instanceof SingleVariableDeclaration) {
				SingleVariableDeclaration svd = (SingleVariableDeclaration) o;

				if (!dependencies.contains(svd.getType().toString()) && !svd.getType().isPrimitiveType()) {
					dependencies.add(svd.getType().toString());
				}
			}
		}
		return true;
	}

	@Override
	public boolean visit(AfterAdviceDeclaration node) {
		if (!methods.contains(String.valueOf(node.hashCode()))) {
			methods.add(String.valueOf(node.hashCode()));
		}
		for (Object o : node.parameters()) {
			if (o instanceof SingleVariableDeclaration) {
				SingleVariableDeclaration svd = (SingleVariableDeclaration) o;
				if (!dependencies.contains(svd.getType().toString()) && !svd.getType().isPrimitiveType()) {
					dependencies.add(svd.getType().toString());
				}
			}
		}
		newDep.setMethods(newDep.getMethods()+1);
		return true;
	}

	@Override
	public boolean visit(BeforeAdviceDeclaration node) {
		if (!methods.contains(String.valueOf(node.hashCode()))) {
			methods.add(String.valueOf(node.hashCode()));
		}
		for (Object o : node.parameters()) {
			if (o instanceof SingleVariableDeclaration) {
				SingleVariableDeclaration svd = (SingleVariableDeclaration) o;
				if (!dependencies.contains(svd.getType().toString()) && !svd.getType().isPrimitiveType()) {
					dependencies.add(svd.getType().toString());
				}
			}
		}
		newDep.setMethods(newDep.getMethods()+1);
		return true;
	}

	@Override
	public boolean visit(AfterReturningAdviceDeclaration node) {
		if (!methods.contains(String.valueOf(node.hashCode()))) {
			methods.add(String.valueOf(node.hashCode()));
		}
		for (Object o : node.parameters()) {
			if (o instanceof SingleVariableDeclaration) {
				SingleVariableDeclaration svd = (SingleVariableDeclaration) o;
				if (!dependencies.contains(svd.getType().toString()) && !svd.getType().isPrimitiveType()) {
					dependencies.add(svd.getType().toString());
				}
			}
		}
		newDep.setMethods(newDep.getMethods()+1);
		return true;
	}

	@Override
	public boolean visit(AfterThrowingAdviceDeclaration node) {
		if (!methods.contains(String.valueOf(node.hashCode()))) {
			methods.add(String.valueOf(node.hashCode()));
		}
		for (Object o : node.parameters()) {
			if (o instanceof SingleVariableDeclaration) {
				SingleVariableDeclaration svd = (SingleVariableDeclaration) o;
				if (!dependencies.contains(svd.getType().toString()) && !svd.getType().isPrimitiveType()) {
					dependencies.add(svd.getType().toString());
				}
			}
		}
		newDep.setMethods(newDep.getMethods()+1);
		return true;
	}

	@Override
	public boolean visit(AroundAdviceDeclaration node) {
		if (!methods.contains(String.valueOf(node.hashCode()))) {
			methods.add(String.valueOf(node.hashCode()));
		}
		for (Object o : node.parameters()) {
			if (o instanceof SingleVariableDeclaration) {
				SingleVariableDeclaration svd = (SingleVariableDeclaration) o;
				if (!dependencies.contains(svd.getType().toString()) && !svd.getType().isPrimitiveType()) {
					dependencies.add(svd.getType().toString());
				}
			}
		}
		newDep.setMethods(newDep.getMethods()+1);
		return true;
	}

	@Override
	public boolean visit(MethodInvocation node) {
		if (!methods.contains(node.getName().toString())) {
			methods.add(node.getName().toString());
		}
		if (!dependencies.contains(node.getName().toString())) {
			dependencies.add(node.getName().toString());
		}
		return true;
	}

	// Pegar within e tudo mais - desnecessario
	// @Override
	// public boolean visit(DefaultPointcut node) {
	// System.out.println("details "+node.getDetail());
	// return true;
	// }

	@Override
	public boolean visit(ImportDeclaration node) {
		imported = new ArrayList<String>();
		String testString = node.getName().toString();
		String[] parts = testString.split("\\.");
		String lastWord = parts[parts.length - 1];
		if (!imported.contains(lastWord)) {
			imported.add(lastWord);
		}
		return true;
	}
	
	public ArrayList<Dependency> getDependency() {
		return dp;
	}
}