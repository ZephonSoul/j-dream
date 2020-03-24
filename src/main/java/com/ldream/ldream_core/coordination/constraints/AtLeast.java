package com.ldream.ldream_core.coordination.constraints;

import com.ldream.ldream_core.components.Port;
import com.ldream.ldream_core.coordination.ActualComponentInstance;
import com.ldream.ldream_core.coordination.ComponentInstance;
import com.ldream.ldream_core.coordination.Interaction;
import com.ldream.ldream_core.coordination.TypeRestriction;
import com.ldream.ldream_core.expressions.Expression;
import com.ldream.ldream_core.expressions.values.IncompatibleValueException;
import com.ldream.ldream_core.expressions.values.NumberValue;

public class AtLeast extends AbstractFormula implements Formula {
	
	private static final int BASE_CODE = 43;
	
	private Expression minInstances;
	private TypeRestriction types;
	private String portName;
	
	public AtLeast(Expression minInstances, TypeRestriction types, String portName) {
		this.minInstances = minInstances;
		this.types = types;
		this.portName = portName;
	}
	
	public AtLeast(Expression minInstances, TypeRestriction types) {
		this(minInstances,types,null);
	}
	
	public AtLeast(Expression minInstances, String portName) {
		this(minInstances,TypeRestriction.anyType(),portName);
	}

	/**
	 * @return the maxInstances
	 */
	public Expression getMinInstances() {
		return minInstances;
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
		return new AtLeast(
				minInstances.bindActualComponent(componentReference, actualComponent),
				types,
				portName);
	}

	@Override
	public boolean sat(Interaction i) {
		if (!(minInstances.eval() instanceof NumberValue))
			throw new IncompatibleValueException(minInstances.eval(),NumberValue.class);
		int occurrences = 0;
		int minOccurrences = ((NumberValue) minInstances.eval()).getRawValue().intValue();
		for (Port p : i.getPorts()) {
			if (types.match(p.getComponent()) 
					&& (portName == null || portName.equals(p.getName())))
				occurrences++;
			if (occurrences >= minOccurrences)
				return true;
		}
		return false;
	}

	@Override
	public boolean equals(Formula formula) {
		return (formula instanceof AtLeast)
				&& minInstances == ((AtLeast) formula).getMinInstances()
				&& types.equals(((AtLeast) formula).getTypes())
				&& portName.equals(((AtLeast) formula).getPortName());
	}

	@Override
	public void clearCache() {
		minInstances.clearCache();
	}
	
	public String toString() {
		String port = "";
		if (portName != null)
			port = "." + portName;
		return String.format("AtLeast(%s,%s%s)", 
				minInstances.toString(),
				types.toString(),
				port);
	}

	@Override
	public int hashCode() {
		int portHash = 0;
		if (portName != null)
			portHash = portName.hashCode();
		return BASE_CODE + minInstances.hashCode() + types.hashCode() + portHash;
	}

}
