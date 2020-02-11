package com.ldream.ldream_core.coordination;

import java.util.Set;
import java.util.stream.Collectors;

import com.ldream.ldream_core.components.Component;

public class ComponentVariable {

	private Quantifier quantifier;
	private Component scope;
	private TypeRestriction type;
	private String name;


	/**
	 * @param quantifier
	 * @param scope
	 * @param type
	 */
	public ComponentVariable(Quantifier quantifier, Component scope, TypeRestriction type, String name) {
		this.quantifier = quantifier;
		this.scope = scope;
		this.type = type;
		this.name = name;
	}

	public ComponentVariable(Quantifier quantifier, Component scope, TypeRestriction typeRestriction) {
		this(
				quantifier, 
				scope, 
				typeRestriction,
				ComponentVariableNamesFactory.getInstance().getFreshName()
				);
	}

	public ComponentVariable(Quantifier quantifier, Component scope) {
		this(
				quantifier, 
				scope, 
				new TypeRestriction(),
				ComponentVariableNamesFactory.getInstance().getFreshName()
				);
	}

	/**
	 * @return the quantifier
	 */
	public Quantifier getQuantifier() {
		return quantifier;
	}

	/**
	 * @param quantifier the quantifier to set
	 */
	public void setQuantifier(Quantifier quantifier) {
		this.quantifier = quantifier;
	}

	/**
	 * @return the scope
	 */
	public Component getScope() {
		return scope;
	}

	/**
	 * @param scope the scope to set
	 */
	public void setScope(Component scope) {
		this.scope = scope;
	}

	/**
	 * @return the type
	 */
	public TypeRestriction getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(TypeRestriction type) {
		this.type = type;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	public Set<Component> getActualComponents() {
		return scope.getComponentsFromPool().stream().filter(c -> type.match(c)).collect(Collectors.toSet());
	}

	public boolean equals(ComponentVariable cVar) {
		return quantifier.equals(cVar.getQuantifier())
				&& scope.equals(cVar.getScope())
				&& type.equals(cVar.getType());
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof ComponentVariable)
			return equals((ComponentVariable)o);
		else
			return true;
	}
	
	@Override
	public String toString() {
		return String.format("%s(%s.%s):[%s]",
				quantifier.toString(),
				scope.getInstanceName(),
				name,
				type.toString());
	}
	
}
