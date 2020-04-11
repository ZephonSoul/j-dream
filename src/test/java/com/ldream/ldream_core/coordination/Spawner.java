package com.ldream.ldream_core.coordination;

import com.dream.core.ExecutionEngine;
import com.dream.core.components.*;
import com.dream.core.coordination.ActualComponentInstance;
import com.dream.core.coordination.ConjunctiveTerm;
import com.dream.core.coordination.Declaration;
import com.dream.core.coordination.Quantifier;
import com.dream.core.coordination.constraints.*;
import com.dream.core.coordination.constraints.predicates.LessThan;
import com.dream.core.coordination.operations.*;
import com.dream.core.expressions.*;
import com.dream.core.expressions.values.NumberValue;
import com.dream.exec.GreedyStrategy;
import com.dream.exec.output.ConsoleOutput;

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
