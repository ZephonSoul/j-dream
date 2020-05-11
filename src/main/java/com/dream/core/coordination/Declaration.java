package com.dream.core.coordination;

import com.dream.core.Bindable;
import com.dream.core.Caching;
import com.dream.core.Instance;
import com.dream.core.coordination.constraints.Formula;
import com.dream.core.coordination.constraints.predicates.Tautology;
import com.dream.core.entities.CoordinatingEntity;

/**
 * @author Alessandro Maggi
 *
 */
public class Declaration implements Bindable<Declaration>, Caching {

	private Quantifier quantifier;
	private EntityInstance scope;
	private TypeRestriction type;
	private EntityInstanceRef variable;
	private Formula instanceFilter;

	/**
	 * @param quantifier
	 * @param scope
	 * @param type
	 * @param variable
	 * @param instanceFilter
	 */
	public Declaration(Quantifier quantifier, 
			EntityInstance scope, 
			TypeRestriction type,
			Formula instanceFilter,
			EntityInstanceRef variable) {

		this.quantifier = quantifier;
		this.scope = scope;
		this.type = type;
		this.variable = variable;
		this.instanceFilter = instanceFilter;
	}

	public Declaration(
			Quantifier quantifier, 
			EntityInstance scope, 
			TypeRestriction type, 
			EntityInstanceRef variable) {

		this(quantifier,scope,type,Tautology.getInstance(),variable);
	}

	public Declaration(
			Quantifier quantifier, 
			EntityInstance scope, 
			TypeRestriction type, 
			Formula instanceFilter) {

		this(quantifier,scope,type,instanceFilter,new EntityInstanceRef());
	}

	public Declaration(
			Quantifier quantifier, 
			EntityInstance scope) {

		this(
				quantifier,
				scope,
				new TypeRestriction(),
				Tautology.getInstance(),
				new EntityInstanceRef()
				);
	}

	public Declaration(
			Quantifier quantifier, 
			EntityInstance scope, 
			TypeRestriction type) {

		this(
				quantifier, 
				scope, 
				type,
				Tautology.getInstance(),
				new EntityInstanceRef()
				);
	}

	public Declaration(
			Quantifier quantifier,
			EntityInstance scope, 
			EntityInstanceRef variable) {

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
	public EntityInstance getScope() {
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
	public EntityInstanceRef getVariable() {
		return variable;
	}

	/**
	 * @param instanceFilter the instanceFilter to set
	 */
	public void setInstanceFilter(Formula instanceFilter) {
		this.instanceFilter = instanceFilter;
	}

	public EntityInstanceActual[] getActualEntities() {
		if (scope.getActual() instanceof CoordinatingEntity) {
			if (instanceFilter instanceof Tautology)
				return ((CoordinatingEntity) scope.getActual()).getPool().stream()
						.filter(e -> type.match(e))
						.map(e -> new EntityInstanceActual(e))
						.toArray(EntityInstanceActual[]::new);
			else
				return ((CoordinatingEntity) scope.getActual()).getPool().stream()
						.filter(e -> type.match(e))
						.map(e -> new EntityInstanceActual(e))
						.filter(actual -> instanceFilter.bindInstance(variable, actual).sat())
						.toArray(EntityInstanceActual[]::new);
		} else {
			throw new IllegalScopeException(scope.getActual());
		}
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
				scope.toString(),
				variable.getName(),
				type.toString(),
				filterString);
	}

	@Override
	public int hashCode() {
		return quantifier.hashCode() + scope.hashCode() + type.hashCode() + variable.hashCode();
	}

	@Override
	public void clearCache() {
		instanceFilter.clearCache();
	}

	@Override
	public <I> Declaration bindInstance(Instance<I> reference, Instance<I> actual) {
		//		if (scope.equals(reference))
		return new Declaration(
				quantifier,
				Bindable.bindInstance(scope, reference, actual),
				type,
				instanceFilter.bindInstance(reference, actual),
				variable
				);
		//		else
		//			return new Declaration(
		//					quantifier,
		//					scope,
		//					type,
		//					instanceFilter.bindInstance(reference, actual),
		//					variable
		//					);
	}

}
