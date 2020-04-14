/**
 * 
 */
package com.dream.core.expressions;

import java.util.Arrays;
import java.util.List;

import com.dream.core.coordination.EntityInstanceActual;
import com.dream.core.coordination.EntityInstanceReference;
import com.dream.core.expressions.values.FactorizableValue;
import com.dream.core.expressions.values.IncompatibleValueException;
import com.dream.core.expressions.values.Value;

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
	public Expression bindEntityReference(
			EntityInstanceReference componentVariable, 
			EntityInstanceActual actualComponent) {

		return new Product(Arrays.stream(operands)
				.map(e -> e.bindEntityReference(componentVariable, actualComponent))
				.toArray(Expression[]::new),
				operandsValue);
	}
	
	@Override
	public int hashCode() {
		return BASE_CODE;
	}
}
