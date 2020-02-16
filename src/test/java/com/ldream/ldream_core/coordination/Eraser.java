package com.ldream.ldream_core.coordination;

import com.ldream.ldream_core.ExecutionEngine;
import com.ldream.ldream_core.components.*;
import com.ldream.ldream_core.coordination.interactions.*;
import com.ldream.ldream_core.coordination.operations.*;
import com.ldream.ldream_core.exec.GreedyStrategy;
import com.ldream.ldream_core.output.ConsoleOutput;

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
