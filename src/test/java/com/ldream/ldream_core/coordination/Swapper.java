package com.ldream.ldream_core.coordination;

import com.dream.core.ExecutionEngine;
import com.dream.core.components.AbstractComponent;
import com.dream.core.components.Pool;
import com.dream.core.coordination.ActualComponentInstance;
import com.dream.core.coordination.ConjunctiveTerm;
import com.dream.core.coordination.Declaration;
import com.dream.core.coordination.FOILRule;
import com.dream.core.coordination.Quantifier;
import com.dream.core.coordination.ReferencedComponentInstance;
import com.dream.core.coordination.Rule;
import com.dream.core.coordination.constraints.predicates.GreaterThan;
import com.dream.core.coordination.operations.Migrate;
import com.dream.core.expressions.Constant;
import com.dream.core.expressions.PoolSize;
import com.dream.core.expressions.values.NumberValue;
import com.dream.exec.GreedyStrategy;
import com.dream.exec.output.ConsoleOutput;

public class Swapper extends AbstractComponent {

	public Swapper() {
		super();
		Spawner s1 = new Spawner();
		DummyComponent d1 = new DummyComponent();
		setPool(new Pool(s1,d1));
		Declaration d = new Declaration(Quantifier.FORALL,new ActualComponentInstance(s1),new ReferencedComponentInstance());
		
		Rule cRule = new FOILRule(d, 
				new ConjunctiveTerm(
						new GreaterThan(new PoolSize(new ActualComponentInstance(s1)),new Constant(new NumberValue(4))),
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
