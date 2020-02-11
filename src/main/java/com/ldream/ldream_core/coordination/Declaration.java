package com.ldream.ldream_core.coordination;

import java.util.Set;
import java.util.stream.Collectors;

import com.ldream.ldream_core.components.Component;

public class Declaration {

	private Quantifier quantifier;
	private Component scope;
	private TypeRestriction type;
	private ReferencedComponentInstance variable;


	/**
	 * @param quantifier
	 * @param scope
	 * @param type
	 */
	public Declaration(
			Quantifier quantifier, 
			Component scope, 
			TypeRestriction type, 
			ReferencedComponentInstance variable) {
		this.quantifier = quantifier;
		this.scope = scope;
		this.type = type;
		this.variable = variable;
	}

	public Declaration(Quantifier quantifier, Component scope) {
		this(
				quantifier, 
				scope, 
				new TypeRestriction(),
				new ReferencedComponentInstance()
				);
	}
	
	public Declaration(Quantifier quantifier, Component scope, TypeRestriction type) {
		this(
				quantifier, 
				scope, 
				type,
				new ReferencedComponentInstance()
				);
	}
	
	public Declaration(Quantifier quantifier, Component scope, ReferencedComponentInstance variable) {
		this(
				quantifier, 
				scope, 
				new TypeRestriction(),
				variable
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
	 * @return the variable
	 */
	public ComponentInstance getVariable() {
		return variable;
	}

	/**
	 * @param variable the variable to set
	 */
	public void setVariable(ReferencedComponentInstance variable) {
		this.variable = variable;
	}
	
	public Set<Component> getActualComponents() {
		return scope.getComponentsFromPool().stream().filter(c -> type.match(c)).collect(Collectors.toSet());
	}

	public boolean equals(Declaration cVar) {
		return quantifier.equals(cVar.getQuantifier())
				&& scope.equals(cVar.getScope())
				&& type.equals(cVar.getType())
				&& variable.equals(cVar.getVariable());
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Declaration)
			return equals((Declaration)o);
		else
			return false;
	}
	
	@Override
	public String toString() {
		return String.format("%s(%s.%s):[%s]",
				quantifier.toString(),
				scope.getInstanceName(),
				variable.getName(),
				type.toString());
	}
	
}
