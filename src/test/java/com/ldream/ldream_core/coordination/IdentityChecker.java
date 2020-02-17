package com.ldream.ldream_core.coordination;

import com.ldream.ldream_core.ExecutionEngine;
import com.ldream.ldream_core.components.AbstractComponent;
import com.ldream.ldream_core.components.Component;
import com.ldream.ldream_core.coordination.constraints.And;
import com.ldream.ldream_core.coordination.constraints.PortReference;
import com.ldream.ldream_core.coordination.guards.SameInstance;
import com.ldream.ldream_core.coordination.operations.Assign;
import com.ldream.ldream_core.exec.GreedyStrategy;
import com.ldream.ldream_core.expressions.Constant;
import com.ldream.ldream_core.expressions.ReferencedVariable;
import com.ldream.ldream_core.expressions.Sum;
import com.ldream.ldream_core.output.ConsoleOutput;
import com.ldream.ldream_core.values.NumberValue;

public class IdentityChecker extends AbstractComponent implements Component {

	public IdentityChecker() {
		Component reference = new DummyComponent();
		setPool(new DummyComponent(), reference, new DummyComponent());
		Declaration d = new Declaration(Quantifier.EXISTS,new ActualComponentInstance(this));
		setRule(new FOILRule(d, 
				new Term(
						new And(
								new SameInstance(new ActualComponentInstance(reference), d.getVariable()),
								new PortReference(d.getVariable(),"p1")
								),
						new Assign(new ReferencedVariable(d.getVariable(),"x"),
								new Sum(new ReferencedVariable(d.getVariable(),"x"),new Constant(new NumberValue(1))))
				)));
	}
	
	public static void main(String[] args) {
		Component c = new IdentityChecker();
		ExecutionEngine ex = new ExecutionEngine(c,GreedyStrategy.getInstance(),new ConsoleOutput(),false,3);
		ex.setSnapshotSemantics(true);
		ex.run();
	}
	
}
