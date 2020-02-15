package com.ldream.ldream_core.coordination;

import java.util.Set;

import com.ldream.ldream_core.ExecutionEngine;
import com.ldream.ldream_core.components.AbstractComponent;
import com.ldream.ldream_core.components.Component;
import com.ldream.ldream_core.components.Pool;
import com.ldream.ldream_core.coordination.interactions.And;
import com.ldream.ldream_core.coordination.interactions.PortReference;
import com.ldream.ldream_core.coordination.operations.*;
import com.ldream.ldream_core.exec.GreedyStrategy;
import com.ldream.ldream_core.expressions.*;
import com.ldream.ldream_core.output.ConsoleOutput;

public class SimpleCompound extends AbstractComponent {

	public SimpleCompound() {
		super();
		DummyComponent d1 = new DummyComponent(),
				d2 = new DummyComponent();
		d1.setPool(new Pool(new DummyComponent(),new DummyComponent(),new DummyComponent()));
		setPool(new Pool(d1,d2));
//		setRule(new AndR(
//				new Term(
//						new Assign(new ActualVariable(d1.getLocalVariable("x")), new Sum(new ActualVariable(d2.getLocalVariable("x")),new Constant(10)))),
//				new Term(
//						new Assign(new ActualVariable(d2.getLocalVariable("x")), new Sum(new ActualVariable(d1.getLocalVariable("x")),new Constant(10)))))
//				);
		var d = new Declaration(
				Quantifier.EXISTS,
				new ActualComponentInstance(this),
				new TypeRestriction(DummyComponent.class));
		Rule target = new FOILRule(d,new AndR(
				new Term(
						new PortReference(d.getVariable(),"p1"),
						new Assign(new ReferencedVariable(d.getVariable(),"x"), new Sum(new ReferencedVariable(d.getVariable(),"x"),new Constant(1)))
						)
				)
				);
		setRule(target);
	}

	public static void main(String[] args) {
		AbstractComponent c = new SimpleCompound();

		System.out.println(c.toString(true, "") + "\n");

		//System.out.println(c.getRule().toString());

		//		var r = c.getRule();
		//
		//		System.out.println(r.expandDeclarations().toString() + "\n");
		//
		//		for (Interaction i : c.getAllAllowedInteractions())
		//			System.out.println(i.toString());
		//
		//		ExecutionEngine ex = new ExecutionEngine(c,GreedyStrategy.getInstance(),new ConsoleOutput(),false,10);
		//		ex.setSnapshotSemantics(true);
		//		ex.run();
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

		var dd1 = new Declaration(
				Quantifier.EXISTS,
				new ActualComponentInstance(c),
				new TypeRestriction(DummyComponent.class));

		var dd2 = new Declaration(
				Quantifier.EXISTS,
				dd1.getVariable());

		Rule test2 = 
				new FOILRule(dd1,
						new FOILRule(dd2,
								new Term(new PortReference(dd2.getVariable(),"p1")))
						);
		System.out.println(test2.toString());
		var t2e = test2.expandDeclarations();
		System.out.println(t2e.toString()+"\n");

		ReferencedComponentInstance newComp = new ReferencedComponentInstance();
		Rule test3 =
				new FOILRule(dd1,
						new Term(
								new PortReference(dd1.getVariable(),"p1"),
								new OperationsSequence(
										new CreateInstance(
												DummyComponent.class,
												dd1.getScope(),
												newComp,
												new Assign(new ReferencedVariable(newComp,"x"),new Constant(5))),
										new Skip())
								)
						);
		
		System.out.println(test3.toString());
		var t3e = test3.expandDeclarations();
		System.out.println(t3e.toString()+"\n");
		
		Rule test4 =
				new FOILRule(dd1,
						new Term(
								new PortReference(dd1.getVariable(),"p1"),
								new OperationsSequence(
										new CreateInstance(
												DummyComponent.class,
												dd1.getScope()),
										new Skip())
								)
						);
		System.out.println(test4.toString());
		var t4e = test4.expandDeclarations();
		System.out.println(t4e.toString()+"\n");
	}

}
