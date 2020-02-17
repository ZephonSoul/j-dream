package com.ldream.ldream_core.coordination;

import com.ldream.ldream_core.ExecutionEngine;
import com.ldream.ldream_core.components.AbstractComponent;
import com.ldream.ldream_core.components.Component;
import com.ldream.ldream_core.components.LocalVariable;
import com.ldream.ldream_core.coordination.operations.Assign;
import com.ldream.ldream_core.exec.GreedyStrategy;
import com.ldream.ldream_core.expressions.ActualVariable;
import com.ldream.ldream_core.expressions.Constant;
import com.ldream.ldream_core.expressions.Sum;
import com.ldream.ldream_core.output.ConsoleOutput;

public class AutoAdder extends AbstractComponent {

	public AutoAdder() {
		LocalVariable x = new LocalVariable("val",0);
		setStore(x);
		setRule(new Term(new Assign(new ActualVariable(x),new Sum(new ActualVariable(x),new Constant(1)))));
	}
	
	public static void main(String[] args) {
		Component c = new AutoAdder();
		ExecutionEngine ex = new ExecutionEngine(c, GreedyStrategy.getInstance(), new ConsoleOutput(),false,3);
		ex.setSnapshotSemantics(true);
		ex.run();
	}
	
}
