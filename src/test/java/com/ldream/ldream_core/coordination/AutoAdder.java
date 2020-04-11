package com.ldream.ldream_core.coordination;

import com.dream.core.ExecutionEngine;
import com.dream.core.components.AbstractComponent;
import com.dream.core.components.Component;
import com.dream.core.components.LocalVariable;
import com.dream.core.coordination.Term;
import com.dream.core.coordination.operations.Assign;
import com.dream.core.expressions.ActualVariable;
import com.dream.core.expressions.Constant;
import com.dream.core.expressions.Sum;
import com.dream.core.expressions.values.NumberValue;
import com.dream.exec.GreedyStrategy;
import com.dream.exec.output.ConsoleOutput;

public class AutoAdder extends AbstractComponent {

	public AutoAdder() {
		LocalVariable x = new LocalVariable("val",new NumberValue(0));
		setStore(x);
		setRule(new Term(new Assign(new ActualVariable(x),new Sum(new ActualVariable(x),new Constant(new NumberValue(1))))));
	}
	
	public static void main(String[] args) {
		Component c = new AutoAdder();
		ExecutionEngine ex = new ExecutionEngine(c, GreedyStrategy.getInstance(), new ConsoleOutput(),false,3);
		ex.setSnapshotSemantics(true);
		ex.run();
	}
	
}
