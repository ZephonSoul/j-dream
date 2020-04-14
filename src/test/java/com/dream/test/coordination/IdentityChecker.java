package com.dream.test.coordination;

import com.dream.ExecutionEngine;
import com.dream.core.coordination.EntityInstanceActual;
import com.dream.core.coordination.Declaration;
import com.dream.core.coordination.FOILRule;
import com.dream.core.coordination.Quantifier;
import com.dream.core.coordination.Term;
import com.dream.core.coordination.constraints.And;
import com.dream.core.coordination.constraints.PortReference;
import com.dream.core.coordination.constraints.predicates.SameInstance;
import com.dream.core.coordination.operations.Assign;
import com.dream.core.entities.AbstractLightComponent;
import com.dream.core.exec.GreedyStrategy;
import com.dream.core.expressions.Constant;
import com.dream.core.expressions.ReferencedVariable;
import com.dream.core.expressions.Sum;
import com.dream.core.expressions.values.NumberValue;
import com.dream.core.output.ConsoleOutput;

public class IdentityChecker extends AbstractLightComponent {

	public IdentityChecker() {
		AbstractLightComponent reference = new DummyComponent();
		setPool(new DummyComponent(), reference, new DummyComponent());
		Declaration d = new Declaration(Quantifier.EXISTS,new EntityInstanceActual(this));
		setRule(new FOILRule(d, 
				new Term(
						new And(
								new SameInstance(new EntityInstanceActual(reference), d.getVariable()),
								new PortReference(d.getVariable(),"p1")
								),
						new Assign(new ReferencedVariable(d.getVariable(),"x"),
								new Sum(new ReferencedVariable(d.getVariable(),"x"),new Constant(new NumberValue(1))))
				)));
	}
	
	public static void main(String[] args) {
		AbstractLightComponent c = new IdentityChecker();
		ExecutionEngine ex = new ExecutionEngine(c,GreedyStrategy.getInstance(),new ConsoleOutput(),false,3);
		ex.setSnapshotSemantics(true);
		ex.run();
	}
	
}
