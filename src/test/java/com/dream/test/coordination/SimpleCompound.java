package com.dream.test.coordination;

import java.util.Set;

import com.dream.ExecutionEngine;
import com.dream.core.coordination.EntityInstanceActual;
import com.dream.core.coordination.AndRule;
import com.dream.core.coordination.Declaration;
import com.dream.core.coordination.FOILRule;
import com.dream.core.coordination.Quantifier;
import com.dream.core.coordination.Rule;
import com.dream.core.coordination.Term;
import com.dream.core.coordination.TypeRestriction;
import com.dream.core.coordination.constraints.And;
import com.dream.core.coordination.constraints.PortReference;
import com.dream.core.coordination.operations.*;
import com.dream.core.entities.AbstractLightComponent;
import com.dream.core.exec.GreedyStrategy;
import com.dream.core.expressions.*;
import com.dream.core.expressions.values.NumberValue;
import com.dream.core.output.ConsoleOutput;

public class SimpleCompound extends AbstractLightComponent {

	public SimpleCompound() {
		super();
		DummyComponent d1 = new DummyComponent(),
				d2 = new DummyComponent();
		d1.setPool(new DummyComponent(),new DummyComponent(),new DummyComponent());
		setPool(d1,d2);
		//		setRule(new AndR(
		//				new Term(
		//						new Assign(new ActualVariable(d1.getLocalVariable("x")), new Sum(new ActualVariable(d2.getLocalVariable("x")),new Constant(10)))),
		//				new Term(
		//						new Assign(new ActualVariable(d2.getLocalVariable("x")), new Sum(new ActualVariable(d1.getLocalVariable("x")),new Constant(10)))))
		//				);
		var d = new Declaration(
				Quantifier.EXISTS,
				new EntityInstanceActual(this),
				new TypeRestriction(DummyComponent.class));
		Rule target = new FOILRule(d,new AndRule(
				new Term(
						new PortReference(d.getVariable(),"p1"),
						new Assign(new ReferencedVariable(d.getVariable(),"x"), new Sum(new ReferencedVariable(d.getVariable(),"x"),new Constant(new NumberValue(1))))
						)
				)
				);
		setRule(target);
	}

	public static void main(String[] args) {
		AbstractLightComponent c = new SimpleCompound();

		//System.out.println(c.toString(true, "") + "\n");

		//System.out.println(c.getRule().toString());

		//		var r = c.getRule();
		//
		//		System.out.println(r.expandDeclarations().toString() + "\n");
		//
		//		for (Interaction i : c.getAllAllowedInteractions())
		//			System.out.println(i.toString());
		//
		ExecutionEngine ex = new ExecutionEngine(c,GreedyStrategy.getInstance(),new ConsoleOutput(),false,2);
		ex.setSnapshotSemantics(true);
		ex.run();
		//		//		
		//		//		Set<Component> components = c.getComponentsFromPool(new ComponentTypes(DummyComponent.class));
		//		//		System.out.println(components.size());
		//		//		components.stream().map(Component::toString).forEach(System.out::println);
		//
		//		var d0 = new Declaration(
		//				Quantifier.FORALL,
		//				new ActualComponentInstance(c));
		//		var d1 = new Declaration(
		//				Quantifier.EXISTS,
		//				new ActualComponentInstance(c));
		//
		//		Rule test = 
		//				new FOILRule(d0,
		//						new FOILRule(d1,
		//								new Term(
		//										new And(
		//												new PortReference(d1.getVariable(),"p1"),
		//												new PortReference(d0.getVariable(),"p1")
		//												)
		//										)
		//								)
		//						);
		//
		//		System.out.println(test.toString());
		//		System.out.println(test.expandDeclarations().toString()+"\n");

//		var dd1 = new Declaration(
//				Quantifier.EXISTS,
//				new ActualComponentInstance(c),
//				new TypeRestriction(DummyComponent.class));
//
//		var dd2 = new Declaration(
//				Quantifier.EXISTS,
//				dd1.getVariable());
//
//		Rule test2 = 
//				new FOILRule(dd1,
//						new FOILRule(dd2,
//								new Term(new PortReference(dd2.getVariable(),"p1")))
//						);
//		System.out.println(test2.toString());
//		var t2e = test2.expandDeclarations();
//		System.out.println(t2e.toString()+"\n");
//
//		ReferencedComponentInstance newComp = new ReferencedComponentInstance();
//		Rule test3 =
//				new FOILRule(dd1,
//						new Term(
//								new PortReference(dd1.getVariable(),"p1"),
//								new OperationsSequence(
//										new CreateInstance(
//												DummyComponent.class,
//												dd1.getScope(),
//												newComp,
//												new Assign(new ReferencedVariable(newComp,"x"),new Constant(5))),
//										Skip.getInstance())
//								)
//						);
//
//		System.out.println(test3.toString());
//		var t3e = test3.expandDeclarations();
//		System.out.println(t3e.toString()+"\n");
//
//		Rule test4 =
//				new FOILRule(dd1,
//						new Term(
//								new PortReference(dd1.getVariable(),"p1"),
//								new OperationsSequence(
//										new CreateInstance(
//												DummyComponent.class,
//												dd1.getScope()),
//										Skip.getInstance())
//								)
//						);
//		System.out.println(test4.toString());
//		var t4e = test4.expandDeclarations();
//		System.out.println(t4e.toString()+"\n");
	}

}
