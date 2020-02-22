/**
 * 
 */
package com.ldream.ldream_core.expressions;

import java.util.Arrays;
import java.util.List;

import com.ldream.ldream_core.coordination.ActualComponentInstance;
import com.ldream.ldream_core.coordination.ComponentInstance;
import com.ldream.ldream_core.values.FactorizableValue;
import com.ldream.ldream_core.values.IncompatibleValueException;
import com.ldream.ldream_core.values.Value;

/**
 * @author alessandro
 *
 */
public class Product extends AbstractEnnaryExpression {
	
	public static final int BASE_CODE = 55;
	
	public Product(Expression[] operands,Value[] operandsValue) {
		super(operands,operandsValue);
	}
	
	public Product(Expression... operands) {
		super(operands);
	}
	
	public Product(List<Expression> operands) {
		super(operands);
	}

	@Override
	public Value op(Value v1, Value v2) {
		if (!(v1 instanceof FactorizableValue))
			throw new IncompatibleValueException(v1,FactorizableValue.class);
		if (!(v2 instanceof FactorizableValue))
			throw new IncompatibleValueException(v2,FactorizableValue.class);

		return ((FactorizableValue) v1).multiplyBy((FactorizableValue) v2);
	}

	@Override
	public String getOperatorSymbol() {
		return "*";
	}

	@Override
	public Expression bindActualComponent(
			ComponentInstance componentVariable, 
			ActualComponentInstance actualComponent) {

		return new Product(Arrays.stream(operands)
				.map(e -> e.bindActualComponent(componentVariable, actualComponent))
				.toArray(Expression[]::new),
				operandsValue);
	}
	
	@Override
	public int hashCode() {
		return BASE_CODE;
	}
}
