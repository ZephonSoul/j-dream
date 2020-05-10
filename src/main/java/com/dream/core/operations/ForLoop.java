/**
 * 
 */
package com.dream.core.operations;

import com.dream.core.Instance;
import com.dream.core.expressions.Expression;
import com.dream.core.expressions.VariableActual;
import com.dream.core.expressions.VariableRef;
import com.dream.core.expressions.values.NumberValue;
import com.dream.core.localstore.LocalVariable;

/**
 * @author Alessandro Maggi
 *
 */
public class ForLoop extends AbstractOperation {
	
	private VariableRef iterVar;
	private Expression initExpression;
	private Expression stopExpression;
	private Operation iteration;

	public ForLoop(
			VariableRef iterVar,
			Expression initExpression,
			Expression stopExpression,
			Operation iteration) {
		
		this.iterVar = iterVar;
		this.initExpression = initExpression;
		this.stopExpression = stopExpression;
		this.iteration = iteration;
	}

	/**
	 * @return the iterVar
	 */
	public VariableRef getIterVar() {
		return iterVar;
	}

	/**
	 * @return the initExpression
	 */
	public Expression getInitExpression() {
		return initExpression;
	}

	/**
	 * @return the stopExpression
	 */
	public Expression getStopExpression() {
		return stopExpression;
	}

	/**
	 * @return the iteration
	 */
	public Operation getIteration() {
		return iteration;
	}

	@Override
	public void evaluateOperands() {
		initExpression.evaluateOperands();
		stopExpression.evaluateOperands();
		iteration.evaluateOperands();
	}

	@Override
	public void execute() {
		try {
			int start = ((NumberValue)initExpression.eval()).getRawValue().intValue();
			int stop = ((NumberValue)stopExpression.eval()).getRawValue().intValue();
			int step = 1;
			if (start > stop)
				step = -1;
			int current = start;
			while (Math.abs(current)-Math.abs(stop) != 0) {
				LocalVariable var = new LocalVariable(new NumberValue(current));
				Operation iterStep = iteration.bindInstance(iterVar, new VariableActual(var));
				iterStep.execute();
				current += step;
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		
	}

	@Override
	public boolean equals(Operation op) {
		ForLoop op2 = (ForLoop) op;
		return (op2 instanceof ForLoop) &&
				iterVar.equals(op2.getIterVar()) &&
				initExpression.equals(op2.getInitExpression()) &&
				stopExpression.equals(op2.getStopExpression()) &&
				iteration.equals(op2.getIteration())	;
	}

	@Override
	public void clearCache() {
		initExpression.clearCache();
		stopExpression.clearCache();
		iteration.clearCache();
	}
	
	@Override
	public <I> Operation bindInstance(
			Instance<I> reference, 
			Instance<I> actual) {
		
		return new ForLoop(
				iterVar,
				initExpression.bindInstance(reference, actual),
				stopExpression.bindInstance(reference, actual),
				iteration.bindInstance(reference, actual));
	}

	public String toString() {
		return String.format("FOR (%s in [%s,%s]) DO {%s}",
				iterVar.toString(),
				initExpression.toString(),
				stopExpression.toString(),
				iteration.toString());
	}
	
}
