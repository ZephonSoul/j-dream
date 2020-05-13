/**
 * 
 */
package com.dream.core.expressions;

import com.dream.core.expressions.values.IncompatibleValueException;
import com.dream.core.expressions.values.NumberValue;
import com.dream.core.expressions.values.RandomNumberFactory;
import com.dream.core.expressions.values.Value;

/**
 * @author Alessandro Maggi
 *
 */
public class RandomNumber extends AbstractExpression {

	private Expression maxValue;
	private NumberValue value;
	
	public RandomNumber(Expression maxValue) {
		this.maxValue = maxValue;
	}
	
	public RandomNumber() {
		this(new NumberValue(1));
	}

	@Override
	public void evaluateOperands() {
		if (value == null) {
			maxValue.evaluateOperands();
			NumberValue max = (NumberValue)maxValue.eval();
			if (max instanceof NumberValue)
				value = new NumberValue(
						RandomNumberFactory.getInstance().getRandomDouble(
								max.getRawValue().doubleValue()));
			else
				throw new IncompatibleValueException(max, NumberValue.class);
		}
	}

	@Override
	public void clearCache() {
		maxValue.clearCache();
		value = null;
	}

	@Override
	public boolean equals(Expression ex) {
		return false;
	}

	@Override
	protected Value computeResult() {
		return value;
	}

	@Override
	public boolean allOperandsValued() {
		return value != null && maxValue.allOperandsValued();
	}
	
	public String toString() {
		if (value != null)
			return value.toString();
		else
			return String.format("Rand(%s)", maxValue.toString());
	}

}
