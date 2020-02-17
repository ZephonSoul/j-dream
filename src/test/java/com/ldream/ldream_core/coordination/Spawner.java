package com.ldream.ldream_core.coordination;

import com.ldream.ldream_core.ExecutionEngine;
import com.ldream.ldream_core.components.*;
import com.ldream.ldream_core.coordination.constraints.*;
import com.ldream.ldream_core.coordination.guards.LessThan;
import com.ldream.ldream_core.coordination.operations.*;
import com.ldream.ldream_core.exec.GreedyStrategy;
import com.ldream.ldream_core.expressions.*;
import com.ldream.ldream_core.output.ConsoleOutput;
import com.ldream.ldream_core.values.NumberValue;

public class Spawner extends AbstractComponent {

	public Spawner() {
		super();
		LocalVariable 	x = new LocalVariable("spawns",new NumberValue(5),this),
				y = new LocalVariable("spawned",new NumberValue(0),this);
		setStore(x,y);
		//ReferencedComponentInstance c = new ReferencedComponentInstance();
		var d = new Declaration(
				Quantifier.EXISTS,
				new ActualComponentInstance(this));
		//		setRule(new OrR (
		//				new Term(
		//						new LessThan(new ActualVariable(y),new Constant(5)),
		//						new OperationsSequence(
		//								new CreateInstance(DummyComponent.class,new ActualComponentInstance(this)),
		//								new Assign(new ActualVariable(x),new Difference(new ActualVariable(x),new Constant(1))),
		//								new Assign(new ActualVariable(y),new Sum(new ActualVariable(y),new Constant(1)))				)
		//						),
		//				new FOILRule(d,
		//						new Term(
		//								new Equals(new ActualVariable(y),new Constant(5)),
		//								new DeleteInstance(d.getVariable())
		//								))
		//				)
		//				);
		setRule(new ConjunctiveTerm(
				new LessThan(new ActualVariable(y),new Constant(new NumberValue(5))),
				new OperationsSequence(
						new CreateInstance(DummyComponent.class,new ActualComponentInstance(this)),
						new Assign(new ActualVariable(x),new Difference(new ActualVariable(x),new NumberValue(1))),
						new Assign(new ActualVariable(y),new Sum(new ActualVariable(y),new NumberValue(1)))				
						)
				));
	}

	public static void main(String[] args) {
		var c = new Spawner();
		System.out.println(c.toString(true, "") + "\n");

		ExecutionEngine ex = new ExecutionEngine(c,GreedyStrategy.getInstance(),new ConsoleOutput(),false,15);
		ex.setSnapshotSemantics(true);
		ex.run();
	}


}
