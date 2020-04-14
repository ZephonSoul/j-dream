package com.dream.test.coordination;

import com.dream.ExecutionEngine;
import com.dream.core.coordination.EntityInstanceActual;
import com.dream.core.coordination.Declaration;
import com.dream.core.coordination.FOILRule;
import com.dream.core.coordination.Quantifier;
import com.dream.core.coordination.Term;
import com.dream.core.coordination.constraints.predicates.Tautology;
import com.dream.core.coordination.operations.*;
import com.dream.core.entities.*;
import com.dream.core.exec.GreedyStrategy;
import com.dream.core.output.ConsoleOutput;

public class Eraser extends AbstractLightComponent {

	public Eraser() {
		super();
		DummyComponent c = new DummyComponent();
		setPool(c);
		var d = new Declaration(
				Quantifier.EXISTS,
				new EntityInstanceActual(this));
		setRule(new FOILRule(d,
				new Term(
						Tautology.getInstance(),
						new DeleteInstance(d.getVariable())
						))
				);
	}

	public static void main(String[] args) {
		var c = new Eraser();
		ExecutionEngine ex = new ExecutionEngine(c,GreedyStrategy.getInstance(),new ConsoleOutput(),false,2);
		ex.setSnapshotSemantics(true);
		ex.run();
	}


}
