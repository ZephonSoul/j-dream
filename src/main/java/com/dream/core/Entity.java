/**
 * 
 */
package com.dream.core;

import java.util.HashSet;
import java.util.Set;

import org.json.simple.JSONObject;

import com.dream.core.coordination.Interaction;
import com.dream.core.coordination.operations.OperationsSet;
import com.dream.core.entities.NoAdmissibleInteractionsException;

/**
 * @author Alessandro Maggi
 *
 */
public interface Entity {
	
	public int getId();

	public boolean isOrphan();
	
	public Entity getParent() throws OrphanEntityException;
	
	public void setParent(Entity entity);
	
	public String toString();
	
	public JSONObject getJSONDescriptor();	
	
	public OperationsSet getOperationsForInteraction(Interaction interaction);
	
	public Interaction getAllowedInteraction();

	public default boolean equals(Entity entity) {
		return getId() == entity.getId();
	}
	
	public default Interaction[] getAllowedInteractions() {
		Set<Interaction> localInteractions = new HashSet<>();
		Interaction i;
		boolean check = false;
		while (!check) {
			try {
				i = getAllowedInteraction();
				if (localInteractions.contains(i))
					check = true;
				else {
					localInteractions.add(i);
				}
			} catch (NoAdmissibleInteractionsException e) {
				break;
			}
		}
		return localInteractions.toArray(Interaction[]::new);
	}
	
}
