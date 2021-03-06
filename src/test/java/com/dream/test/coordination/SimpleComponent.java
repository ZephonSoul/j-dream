package com.dream.test.coordination;

import java.util.HashSet;
import java.util.Set;

import com.dream.ExecutionEngine;
import com.dream.core.coordination.Interaction;
import com.dream.core.coordination.OrRule;
import com.dream.core.coordination.Term;
import com.dream.core.coordination.constraints.*;
import com.dream.core.coordination.constraints.predicates.Tautology;
import com.dream.core.entities.*;
import com.dream.core.exec.GreedyStrategy;
import com.dream.core.expressions.*;
import com.dream.core.expressions.values.NumberValue;
import com.dream.core.localstore.LocalVariable;
import com.dream.core.operations.Assign;
import com.dream.core.operations.OperationsSet;
import com.dream.core.output.ConsoleOutput;

public class SimpleComponent extends AbstractLightComponent {

	public SimpleComponent() {
		super();
		Port p1 = new Port("p1");
		Port p2 = new Port("p2");
		LocalVariable x = new LocalVariable("x",new NumberValue(5),this);
		setInterface(p1,p2);
		setRule(new OrRule(
				new Term(
						Tautology.getInstance()
						),
				new Term(new PortAtom(p1),
						new Assign(new VariableActual(x),
								new Sum(
										new NumberValue(10),
										new NumberValue(5),
										new VariableActual(x)))
						),
				new Term(new PortAtom(p2),
						new Assign(new VariableActual(x),
								new Product(
										new NumberValue(2),
										new NumberValue(3),
										new VariableActual(x)))
						)
				)
				);
		setStore(x);
	}

	public static void main(String[] args) {
		SimpleComponent c = new SimpleComponent(),
				c_sub = new SimpleComponent(),
				b_sub = new SimpleComponent(),
				d_sub = new SimpleComponent();
		c_sub.setPool(b_sub,d_sub);
		c.setPool(c_sub);
		System.out.println(c.toString());
//		System.out.println((new Sum(new Constant(10),new Difference(new Constant(3),new Constant(1)))).equals(
//				new Sum(new Constant(10),new Difference(new Constant(3),new Constant(1)))));
//		Interaction[] interactions = c.getAllowedInteractions();
//		for (Interaction inter : interactions)
//			System.out.println(inter.toString());
//		System.out.println("===================");
//		for (int i=0;i<10;i++)
//			System.out.println(c.getAllowedInteraction().toString());
		boolean check = false;
		Set<Interaction> foundInt = new HashSet<Interaction>();
		while (!check) {
			Interaction i = c.getAllowedInteraction();
			if (foundInt.contains(i))
				check = true;
			else {
				foundInt.add(i);
				System.out.println(i.toString());
			}
		}
		
//		for (Port p : c.getInterface().getPorts()) {
//			System.out.println(p.toString() + "\t" + p.hashCode());
//		}
//		System.out.println();
//		for (Port p : c_sub.getInterface().getPorts()) {
//			System.out.println(p.toString() + "\t" + p.hashCode());
//		}
		System.out.println("===================");
		
		for (Interaction i : foundInt) {
			OperationsSet opSet = c.getOperationsForInteraction(i);
			boolean consistencyCheck = i.size() == opSet.size()-1;
			System.out.println(consistencyCheck + "\t" + i.toString() + " -> " + opSet.toString());
		}
		
		ExecutionEngine ex = new ExecutionEngine(c,GreedyStrategy.getInstance(),new ConsoleOutput(),false,10);
		ex.run();
	}

}
