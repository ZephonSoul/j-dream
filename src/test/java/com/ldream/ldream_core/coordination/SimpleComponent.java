package com.ldream.ldream_core.coordination;

import java.util.HashSet;
import java.util.Set;

import com.ldream.ldream_core.ExecutionEngine;
import com.ldream.ldream_core.components.*;
import com.ldream.ldream_core.coordination.interactions.*;
import com.ldream.ldream_core.coordination.operations.*;
import com.ldream.ldream_core.exec.GreedyStrategy;
import com.ldream.ldream_core.expressions.*;
import com.ldream.ldream_core.output.ConsoleOutput;

public class SimpleComponent extends AbstractComponent implements Component {

	public SimpleComponent() {
		super();
		Port p1 = new Port("p1");
		Port p2 = new Port("p2");
		LocalVariable x = new LocalVariable("x",5,this);
		setInterface(p1,p2);
		setRule(new OrR(
				new Term(
						Tautology.getInstance()
						),
				new Term(new PortAtom(p1),
						new Assign(new ActualVariable(x),new Sum(new Constant(10),new Constant(5),new ActualVariable(x)))
						),
				new Term(new PortAtom(p2),
						new Assign(new ActualVariable(x),new Product(new Constant(2),new Constant(3),new ActualVariable(x)))
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
		c_sub.setPool(new Pool(b_sub,d_sub));
		c.setPool(new Pool(c_sub));
		System.out.println(c.toString(true,""));
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
