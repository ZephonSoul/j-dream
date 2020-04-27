/**
 * 
 */
package com.dream.core.entities;

import java.util.Set;

import com.dream.core.Caching;
import com.dream.core.Entity;
import com.dream.core.coordination.Rule;

/**
 * @author Aessandro Maggi
 *
 */
public interface CoordinatingEntity extends Entity,Caching {

	public Set<Entity> getPool();
	
	public void setPool(Set<Entity> pool);
	
	public void setPool(Entity... entities);
	
	public int getPoolSize();
	
	public void addToPool(Entity entity);
	
	public void addToPool(Entity... entities);
	
	public boolean hosts(Entity entity);
	
	public void removeFromPool(Entity entity);
	
	public Rule getRule();
	
	public void setRule(Rule rule);
	
	public Rule getExpandedRule();
	
}
