package com.dream.test.coordination;

import com.dream.ExecutionEngine;
import com.dream.core.coordination.EntityInstanceActual;
import com.dream.core.coordination.ConjunctiveTerm;
import com.dream.core.coordination.Declaration;
import com.dream.core.coordination.FOILRule;
import com.dream.core.coordination.Quantifier;
import com.dream.core.coordination.EntityInstanceReference;
import com.dream.core.coordination.Rule;
import com.dream.core.coordination.constraints.predicates.GreaterThan;
import com.dream.core.coordination.operations.Migrate;
import com.dream.core.entities.AbstractLightComponent;
import com.dream.core.exec.GreedyStrategy;
import com.dream.core.expressions.Constant;
import com.dream.core.expressions.PoolSize;
import com.dream.core.expressions.values.NumberValue;
import com.dream.core.output.ConsoleOutput;

public class Swapper extends AbstractLightComponent {

	public Swapper() {
		super();
		Spawner s1 = new Spawner();
		DummyComponent d1 = new DummyComponent();
		setPool(s1,d1);
		Declaration d = new Declaration(Quantifier.FORALL,new EntityInstanceActual(s1),new EntityInstanceReference());
		
		Rule cRule = new FOILRule(d, 
				new ConjunctiveTerm(
						new GreaterThan(new PoolSize(new EntityInstanceActual(s1)),new Constant(new NumberValue(4))),
						new Migrate(d.getVariable(),new EntityInstanceActual(d1))
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
