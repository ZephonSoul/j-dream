/**
 * 
 */
package com.dream.core.coordination.maps;

import com.dream.core.Entity;
import com.dream.core.Instance;
import com.dream.core.OrphanEntityException;
import com.dream.core.coordination.UnboundReferenceException;
import com.dream.core.entities.AbstractMotif;
import com.dream.core.entities.maps.MapNode;

/**
 * @author Alessandro Maggi
 *
 */
public class MapNodeForEntity 
implements MapNodeInstance {

	final static int BASE_CODE = 471;

	private Instance<Entity> entity;

	public MapNodeForEntity(Instance<Entity> entity) {
		this.entity = entity;
	}

	public Instance<Entity> getEntityInstance() {
		return entity;
	}

	@Override
	public <I> MapNodeInstance bindInstance(
			Instance<I> reference,
			Instance<I> actual) {

		if (entity.equals(reference)) {
			try {
				Entity entityActual = (Entity) actual.getActual(); 
				Entity motif = entityActual.getParent();
				if (motif instanceof AbstractMotif)
					return new MapNodeActual(
							((AbstractMotif)motif).getMapNodeForEntity(
									entityActual));
				else
					throw new IllegalAddressingException(actual,"parent entity not a motif");
			} catch (OrphanEntityException ex) {
				throw new IllegalAddressingException(actual,"entity is orphan");
			}
		} else
			return this;
	}

	@Override
	public MapNode getActual() {
		throw new UnboundReferenceException(entity);
	}

	@Override
	public String toString() {
		return String.format("@(%s)", entity.getName());
	}

	@Override
	public int hashCode() {
		return BASE_CODE + entity.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		return (o instanceof MapNodeForEntity) &&
				entity.equals(((MapNodeForEntity)o).getEntityInstance());
	}

	@Override
	public String getName() {
		return toString();
	}

}
