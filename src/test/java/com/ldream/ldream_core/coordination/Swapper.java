package com.ldream.ldream_core.coordination;

import com.ldream.ldream_core.ExecutionEngine;
import com.ldream.ldream_core.components.AbstractComponent;
import com.ldream.ldream_core.components.Pool;
import com.ldream.ldream_core.coordination.interactions.GreaterThan;
import com.ldream.ldream_core.coordination.operations.Migrate;
import com.ldream.ldream_core.exec.GreedyStrategy;
import com.ldream.ldream_core.expressions.Constant;
import com.ldream.ldream_core.expressions.PoolSize;
import com.ldream.ldream_core.output.ConsoleOutput;

public class Swapper extends AbstractComponent {

	public Swapper() {
		super();
		Spawner s1 = new Spawner();
		DummyComponent d1 = new DummyComponent();
		setPool(new Pool(s1,d1));
		Declaration d = new Declaration(Quantifier.FORALL,new ActualComponentInstance(s1),new ReferencedComponentInstance());
		
		Rule cRule = new FOILRule(d, 
				new ConjunctiveTerm(
						new GreaterThan(new PoolSize(new ActualComponentInstance(s1)),new Constant(4)),
						new Migrate(d.getVariable(),new ActualComponentInstance(d1))
						));
		setRule(cRule);
	}
	
	public static void main(String[] args) {
		var c = new Swapper();

		ExecutionEngine ex = new ExecutionEngine(c,GreedyStrategy.getInstance(),new ConsoleOutput(),false,7);
		ex.setSnapshotSemantics(true);
		ex.run();
	}

}
