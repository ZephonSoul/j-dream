package com.ldream.ldream_core.coordination;

import java.util.Set;
import java.util.stream.Collectors;

import com.ldream.ldream_core.Bindable;

public class Declaration implements Bindable<Declaration> {

	private Quantifier quantifier;
	private ComponentInstance scope;
	private TypeRestriction type;
	private ReferencedComponentInstance variable;

	/**
	 * @param quantifier
	 * @param scope
	 * @param type
	 */
	public Declaration(
			Quantifier quantifier, 
			ComponentInstance scope, 
			TypeRestriction type, 
			ReferencedComponentInstance variable) {
		
		this.quantifier = quantifier;
		this.scope = scope;
		this.type = type;
		this.variable = variable;
	}

	public Declaration(
			Quantifier quantifier, 
			ComponentInstance scope) {
		
		this(
				quantifier,
				scope,
				new TypeRestriction(),
				new ReferencedComponentInstance()
				);
	}

	public Declaration(
			Quantifier quantifier, 
			ComponentInstance scope, 
			TypeRestriction type) {
		
		this(
				quantifier, 
				scope, 
				type,
				new ReferencedComponentInstance()
				);
	}

	public Declaration(
			Quantifier quantifier,
			ComponentInstance scope, 
			ReferencedComponentInstance variable) {
		
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
	public ComponentInstance getScope() {
		return scope;
	}

	/**
	 * @param scope the scope to set
	 */
	public void setScope(ComponentInstance scope) {
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
	public ReferencedComponentInstance getVariable() {
		return variable;
	}

	/**
	 * @param variable the variable to set
	 */
	public void setVariable(ReferencedComponentInstance variable) {
		this.variable = variable;
	}

	public Set<ActualComponentInstance> getActualComponents() {
		return scope.getComponent().getComponentsFromPool().stream()
				.filter(c -> type.match(c))
				.map(c -> new ActualComponentInstance(c))
				.collect(Collectors.toSet());
	}

	public boolean equals(Declaration cVar) {
		return quantifier.equals(cVar.getQuantifier())
				&& scope.equals(cVar.getScope())
				&& type.equals(cVar.getType())
				&& variable.equals(cVar.getVariable());
	}

	@Override
	public boolean equals(Object o) {
		return (o instanceof Declaration)
				&& equals((Declaration)o);
	}

	@Override
	public String toString() {
		return String.format("%s(%s.%s):[%s]",
				quantifier.toString(),
				scope.getName(),
				variable.getName(),
				type.toString());
	}

	@Override
	public Declaration bindActualComponent(
			ComponentInstance componentReference, 
			ActualComponentInstance actualComponent) {

		if (scope.equals(componentReference))
			return new Declaration(
					quantifier,
					actualComponent,
					type,
					variable
					);
		else
			return this;

	}

}
