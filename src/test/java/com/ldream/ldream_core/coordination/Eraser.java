package com.ldream.ldream_core.coordination;

import com.dream.core.ExecutionEngine;
import com.dream.core.components.*;
import com.dream.core.coordination.ActualComponentInstance;
import com.dream.core.coordination.Declaration;
import com.dream.core.coordination.FOILRule;
import com.dream.core.coordination.Quantifier;
import com.dream.core.coordination.Term;
import com.dream.core.coordination.constraints.*;
import com.dream.core.coordination.constraints.predicates.Tautology;
import com.dream.core.coordination.operations.*;
import com.dream.exec.GreedyStrategy;
import com.dream.exec.output.ConsoleOutput;

public class Eraser extends AbstractComponent {

	public Eraser() {
		super();
		DummyComponent c = new DummyComponent();
		setPool(new Pool(c));
		var d = new Declaration(
				Quantifier.EXISTS,
				new ActualComponentInstance(this));
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
