package com.ldream.ldream_core.coordination.constraints;

import com.ldream.ldream_core.components.Port;
import com.ldream.ldream_core.coordination.ActualComponentInstance;
import com.ldream.ldream_core.coordination.ComponentInstance;
import com.ldream.ldream_core.coordination.Interaction;
import com.ldream.ldream_core.coordination.TypeRestriction;
import com.ldream.ldream_core.expressions.Expression;
import com.ldream.ldream_core.values.IncompatibleValueException;
import com.ldream.ldream_core.values.NumberValue;

public class AtMost implements Formula {
	
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

	@Override
	public Formula bindActualComponent(ComponentInstance componentReference, ActualComponentInstance actualComponent) {
		return new AtMost(
				maxInstances.bindActualComponent(componentReference, actualComponent),
				types,
				portName);
	}

	@Override
	public boolean sat(Interaction i) {
		if (!(maxInstances.eval() instanceof NumberValue))
			throw new IncompatibleValueException(maxInstances.eval(),NumberValue.class);
		int occurrences = 0;
		int maxOccurrences = ((NumberValue) maxInstances.eval()).getRawValue().intValue();
		for (Port p : i.getPorts()) {
			if (types.match(p.getComponent()) 
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

}
