package com.ldream.ldream_core.coordination.constraints;

import com.ldream.ldream_core.components.Port;
import com.ldream.ldream_core.coordination.ActualComponentInstance;
import com.ldream.ldream_core.coordination.ComponentInstance;
import com.ldream.ldream_core.coordination.Interaction;
import com.ldream.ldream_core.coordination.TypeRestriction;
import com.ldream.ldream_core.expressions.Expression;
import com.ldream.ldream_core.expressions.values.IncompatibleValueException;
import com.ldream.ldream_core.expressions.values.NumberValue;

public class Exactly extends AbstractFormula implements Formula {
	
	private static final int BASE_CODE = 63;
	
	private Expression nInstances;
	private TypeRestriction types;
	private String portName;
	
	public Exactly(Expression nInstances, TypeRestriction types, String portName) {
		this.nInstances = nInstances;
		this.types = types;
		this.portName = portName;
	}
	
	public Exactly(Expression nInstances, TypeRestriction types) {
		this(nInstances,types,null);
	}
	
	public Exactly(Expression nInstances, String portName) {
		this(nInstances,TypeRestriction.anyType(),portName);
	}

	/**
	 * @return the maxInstances
	 */
	public Expression getNInstances() {
		return nInstances;
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
		return new Exactly(
				nInstances.bindActualComponent(componentReference, actualComponent),
				types,
				portName);
	}

	@Override
	public boolean sat(Interaction i) {
		if (!(nInstances.eval() instanceof NumberValue))
			throw new IncompatibleValueException(nInstances.eval(),NumberValue.class);
		int occurrences = 0;
		int nOccurrences = ((NumberValue) nInstances.eval()).getRawValue().intValue();
		for (Port p : i.getPorts()) {
			if (types.match(p.getComponent()) 
					&& (portName == null || portName.equals(p.getName())))
				occurrences++;
			if (occurrences == nOccurrences)
				return true;
		}
		return false;
	}

	@Override
	public boolean equals(Formula formula) {
		return (formula instanceof Exactly)
				&& nInstances == ((Exactly) formula).getNInstances()
				&& types.equals(((Exactly) formula).getTypes())
				&& portName.equals(((Exactly) formula).getPortName());
	}

	@Override
	public void clearCache() {
		nInstances.clearCache();
	}
	
	public String toString() {
		String port = "";
		if (portName != null)
			port = "." + portName;
		return String.format("Exactly(%s,%s%s)", 
				nInstances.toString(),
				types.toString(),
				port);
	}

	@Override
	public int hashCode() {
		int portHash = 0;
		if (portName != null)
			portHash = portName.hashCode();
		return BASE_CODE + nInstances.hashCode() + types.hashCode() + portHash;
	}

}
