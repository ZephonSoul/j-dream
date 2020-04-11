package com.dream.core.coordination.constraints;

import com.dream.core.components.Port;
import com.dream.core.coordination.ActualComponentInstance;
import com.dream.core.coordination.ComponentInstance;
import com.dream.core.coordination.Interaction;
import com.dream.core.coordination.TypeRestriction;
import com.dream.core.expressions.Expression;
import com.dream.core.expressions.values.IncompatibleValueException;
import com.dream.core.expressions.values.NumberValue;

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
