package com.dream.core.coordination.constraints;

import com.dream.core.Instance;
import com.dream.core.coordination.Interaction;
import com.dream.core.coordination.TypeRestriction;
import com.dream.core.entities.Port;
import com.dream.core.expressions.Expression;
import com.dream.core.expressions.values.IncompatibleValueException;
import com.dream.core.expressions.values.NumberValue;

/**
 * @author Alessandro Maggi
 *
 */
public class AtMost extends AbstractFormula implements Formula {
	
	private static final int BASE_CODE = 23;
	
	private Expression maxInstances;
	private TypeRestriction types;
	private String portName;
	
	public AtMost(Expression maxInstances, TypeRestriction types, String portName) {
		this.maxInstances = maxInstances;
		this.types = types;
		this.portName = portName;
	}
	
	public AtMost(Expression maxInstances, TypeRestriction types) {
		this(maxInstances,types,null);
	}
	
	public AtMost(Expression maxInstances, String portName) {
		this(maxInstances,TypeRestriction.anyType(),portName);
	}

	/**
	 * @return the maxInstances
	 */
	public Expression getMaxInstances() {
		return maxInstances;
	}

	/**
	 * @return the types
	 */
	public TypeRestriction getTypes() {
		return types;
	}

	/**
	 * @return the portName
	 */
	public String getPortName() {
		return portName;
	}

//	@Override
//	public Formula bindEntityReference(EntityInstanceRef componentReference, EntityInstanceActual actualComponent) {
//		return new AtMost(
//				maxInstances.bindInstance(componentReference, actualComponent),
//				types,
//				portName);
//	}

	@Override
	public boolean sat(Interaction i) {
		if (!(maxInstances.eval() instanceof NumberValue))
			throw new IncompatibleValueException(maxInstances.eval(),NumberValue.class);
		int occurrences = 0;
		int maxOccurrences = ((NumberValue) maxInstances.eval()).getRawValue().intValue();
		for (Port p : i.getPorts()) {
			if (types.match(p.getEntity()) 
					&& (portName == null || portName.equals(p.getName())))
				occurrences++;
			if (occurrences > maxOccurrences)
				return false;
		}
		return true;
	}

	@Override
	public boolean equals(Formula formula) {
		return (formula instanceof AtMost)
				&& maxInstances == ((AtMost) formula).getMaxInstances()
				&& types.equals(((AtMost) formula).getTypes())
				&& portName.equals(((AtMost) formula).getPortName());
	}

	@Override
	public void clearCache() {
		maxInstances.clearCache();
	}
	
	public String toString() {
		String port = "";
		if (portName != null)
			port = "." + portName;
		return String.format("AtMost(%s,%s%s)", 
				maxInstances.toString(),
				types.toString(),
				port);
	}

	@Override
	public int hashCode() {
		int portHash = 0;
		if (portName != null)
			portHash = portName.hashCode();
		return BASE_CODE + maxInstances.hashCode() + types.hashCode() + portHash;
	}

	@Override
	public <I> Formula bindInstance(Instance<I> reference, Instance<I> actual) {
		return new AtMost(
				maxInstances.bindInstance(reference, actual),
				types,
				portName);
	}
	
	@Override
	public void evaluateExpressions() {
		maxInstances.evaluateOperands();
	}

}
