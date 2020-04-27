package com.dream.test.coordination;

import com.dream.ExecutionEngine;
import com.dream.core.coordination.Term;
import com.dream.core.entities.AbstractLightComponent;
import com.dream.core.entities.CoordinatingEntity;
import com.dream.core.entities.LocalVariable;
import com.dream.core.exec.GreedyStrategy;
import com.dream.core.expressions.VariableActual;
import com.dream.core.expressions.Sum;
import com.dream.core.expressions.values.NumberValue;
import com.dream.core.operations.Assign;
import com.dream.core.output.ConsoleOutput;

public class AutoAdder extends AbstractLightComponent {

	public AutoAdder() {
		LocalVariable x = new LocalVariable("val",new NumberValue(0));
		setStore(x);
		setRule(new Term(new Assign(new VariableActual(x),new Sum(new VariableActual(x),new NumberValue(1)))));
	}
	
	public static void main(String[] args) {
		CoordinatingEntity c = new AutoAdder();
		ExecutionEngine ex = new ExecutionEngine(c, GreedyStrategy.getInstance(), new ConsoleOutput(),false,3);
		ex.setSnapshotSemantics(true);
		ex.run();
	}
	
}
