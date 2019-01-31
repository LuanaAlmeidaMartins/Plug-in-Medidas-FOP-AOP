package plugin.ast;

import java.util.ArrayList;


import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import plugin.persistences.Dependency;
import plugin.persistences.DependencyMethod;

public class DependencyVisitor extends ASTVisitor {

	private CompilationUnit fullClass;
	private IType clazz;
	private ArrayList<String> dependencias;
	private 
	ArrayList<Dependency> dp;
	ArrayList<DependencyMethod> dpMethod;

	public DependencyVisitor(ICompilationUnit unit) throws JavaModelException {
		dp = new ArrayList<Dependency>();
		dp = new ArrayList<>();
		@SuppressWarnings("deprecation")
		ASTParser parser = ASTParser.newParser(AST.JLS8);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setSource(unit);
		parser.setCompilerOptions(JavaCore.getOptions());
		parser.setProject(unit.getJavaProject());
		parser.setResolveBindings(true);
		parser.setBindingsRecovery(true);

		this.fullClass = (CompilationUnit) parser.createAST(null);
		this.fullClass.accept(this);
	}
	
	
	@Override
	public boolean visit(TypeDeclaration node) {
		dependencias = new ArrayList<String>();
		clazz = (IType) node.resolveBinding().getJavaElement();
		dp.add(new Dependency(clazz, dependencias));
		// System.out.println("Class name: "+ node.resolveBinding().getName());
		return true;
	}

	@Override
	public boolean visit(FieldDeclaration node) {
		if (!dependencias.contains(node.getType().resolveBinding().getName()) && !node.getType().isPrimitiveType()) {
				dependencias.add(node.getType().resolveBinding().getName());
		} 
		return true;
	}
	
	@Override
	public boolean visit(MethodDeclaration node) {
		// System.out.println("Method name: "+node.getName());
		for (Object o : node.parameters()) {
			if (o instanceof SingleVariableDeclaration) {
				SingleVariableDeclaration svd = (SingleVariableDeclaration) o;
				if (!dependencias.contains(svd.getType().resolveBinding().getName()) && !svd.getType().isPrimitiveType()) {
				//	System.out.println("MethodDeclaration "+svd.getType().resolveBinding().getName());
					dependencias.add(svd.getType().resolveBinding().getName());
				}
			}
		}
		return true;
	}
	
	@Override
	public boolean visit(Assignment node) {
//		System.out.println("assigment : "+node.resolveTypeBinding().getJavaElement().getElementName());
	//	System.out.println("-- : "+node.getLeftHandSide());
		return true;
	}

	@Override
	public boolean visit(MethodInvocation node) {
		//System.out.println("Method invo: "+node.resolveMethodBinding().getName());
		if(!dependencias.contains(node.resolveMethodBinding().getDeclaringClass().getName())) {
			dependencias.add(node.resolveMethodBinding().getDeclaringClass().getName());
		}
		return true;
	}

	@Override
	public boolean visit(ClassInstanceCreation node) {
		if (!dependencias.contains(node.getType().resolveBinding().getName()) && !node.getType().isPrimitiveType()) {
		//	System.out.println("ClassInstanceCreation "+node.getType().resolveBinding().getName());
			dependencias.add(node.getType().resolveBinding().getName());
		// System.out.println("Class used name: "+node.getType().resolveBinding().getName());
		}
		return true;
	}

	public IType getClazz() {
		return clazz;
	}
		
	public ArrayList<Dependency> getDependency() {
		return dp;
	}

}
