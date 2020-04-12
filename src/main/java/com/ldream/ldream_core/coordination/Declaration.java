package com.ldream.ldream_core.coordination;

import com.ldream.ldream_core.Bindable;
import com.ldream.ldream_core.coordination.constraints.Formula;
import com.ldream.ldream_core.coordination.constraints.predicates.Tautology;

public class Declaration implements Bindable<Declaration> {

	private Quantifier quantifier;
	private ComponentInstance scope;
	private TypeRestriction type;
	private ReferencedComponentInstance variable;
	private Formula instanceFilter;

	/**
	 * @param quantifier
	 * @param scope
	 * @param type
	 * @param variable
	 * @param instanceFilter
	 */
	public Declaration(Quantifier quantifier, 
			ComponentInstance scope, 
			TypeRestriction type,
			Formula instanceFilter,
			ReferencedComponentInstance variable) {

		this.quantifier = quantifier;
		this.scope = scope;
		this.type = type;
		this.variable = variable;
		this.instanceFilter = instanceFilter;
	}

	public Declaration(
			Quantifier quantifier, 
			ComponentInstance scope, 
			TypeRestriction type, 
			ReferencedComponentInstance variable) {

		this(quantifier,scope,type,Tautology.getInstance(),variable);
	}
	
	public Declaration(
			Quantifier quantifier, 
			ComponentInstance scope, 
			TypeRestriction type, 
			Formula instanceFilter) {

		this(quantifier,scope,type,instanceFilter,new ReferencedComponentInstance());
	}

	public Declaration(
			Quantifier quantifier, 
			ComponentInstance scope) {

		this(
				quantifier,
				scope,
				new TypeRestriction(),
				Tautology.getInstance(),
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
				Tautology.getInstance(),
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
				Tautology.getInstance(),
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
	 * @return the scope
	 */
	public ComponentInstance getScope() {
		return scope;
	}

	/**
	 * @return the type
	 */
	public TypeRestriction getType() {
		return type;
	}

	/**
	 * @return the instanceFilter
	 */
	public Formula getInstanceFilter() {
		return instanceFilter;
	}

	/**
	 * @return the variable
	 */
	public ReferencedComponentInstance getVariable() {
		return variable;
	}

	/**
	 * @param instanceFilter the instanceFilter to set
	 */
	public void setInstanceFilter(Formula instanceFilter) {
		this.instanceFilter = instanceFilter;
	}

	public ActualComponentInstance[] getActualComponents() {
		if (instanceFilter instanceof Tautology)
			return scope.getComponent().getComponentsFromPool().stream()
					.filter(c -> type.match(c))
					.map(c -> new ActualComponentInstance(c))
					.toArray(ActualComponentInstance[]::new);
		else
			return scope.getComponent().getComponentsFromPool().stream()
					.filter(c -> type.match(c))
					.map(c -> new ActualComponentInstance(c))
					.filter(actual -> instanceFilter.bindActualComponent(variable, actual).sat())
					.toArray(ActualComponentInstance[]::new);
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
		String filterString = "";
		if (!(instanceFilter instanceof Tautology))
			filterString = "(" + instanceFilter.toString() + ")";
		return String.format("%s(%s.%s):[%s%s]",
				quantifier.toString(),
				scope.getName(),
				variable.getName(),
				type.toString(),
				filterString);
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
					instanceFilter.bindActualComponent(componentReference, actualComponent),
					variable
					);
		else
			return new Declaration(
					quantifier,
					scope,
					type,
					instanceFilter.bindActualComponent(componentReference, actualComponent),
					variable
					);

	}

	@Override
	public int hashCode() {
		return quantifier.hashCode() + scope.hashCode() + type.hashCode() + variable.hashCode();
	}
	
	public void clearCache() {
		instanceFilter.clearCache();
	}

}
