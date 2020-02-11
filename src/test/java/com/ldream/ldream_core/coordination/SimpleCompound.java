package com.ldream.ldream_core.coordination;

import java.util.Set;

import com.ldream.ldream_core.ExecutionEngine;
import com.ldream.ldream_core.components.AbstractComponent;
import com.ldream.ldream_core.components.Component;
import com.ldream.ldream_core.components.Pool;
import com.ldream.ldream_core.coordination.interactions.PortReference;
import com.ldream.ldream_core.coordination.operations.Assign;
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
		setRule(new AndR(
				new Term(
						new Assign(new ActualVariable(d1.getLocalVariable("x")), new Sum(new ActualVariable(d2.getLocalVariable("x")),new Constant(10)))),
				new Term(
						new Assign(new ActualVariable(d2.getLocalVariable("x")), new Sum(new ActualVariable(d1.getLocalVariable("x")),new Constant(10)))))
				);
		var c = new ComponentVariable(Quantifier.EXISTS,this,new TypeRestriction(DummyComponent.class));
		Rule target = new FOILRule(c,new AndR(
				new Term(
						new PortReference(c,"p1"),
						new Assign(new ReferencedVariable(c,"x"), new Sum(new ReferencedVariable(c,"x"),new Constant(1)))
						)
				)
				);
		setRule(target);
	}
	
	public static void main(String[] args) {
		AbstractComponent c = new SimpleCompound();
		
		System.out.println(c.toString(true, "") + "\n");
		
		//System.out.println(c.getRule().toString());
		
		var r = c.getRule();
		
		System.out.println(r.getPILRule().toString() + "\n");
		
		for (Interaction i : c.getAllAllowedInteractions())
			System.out.println(i.toString());
		
		ExecutionEngine ex = new ExecutionEngine(c,GreedyStrategy.getInstance(),new ConsoleOutput(),false,10);
		ex.setSnapshotSemantics(true);
		ex.run();
//		
//		Set<Component> components = c.getComponentsFromPool(new ComponentTypes(DummyComponent.class));
//		System.out.println(components.size());
//		components.stream().map(Component::toString).forEach(System.out::println);
	}
	
}
