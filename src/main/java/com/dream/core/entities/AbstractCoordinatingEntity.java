/**
 * 
 */
package com.dream.core.entities;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.dream.core.AbstractEntity;
import com.dream.core.Caching;
import com.dream.core.Entity;
import com.dream.core.OrphanEntityException;
import com.dream.core.coordination.DummyRule;
import com.dream.core.coordination.Interaction;
import com.dream.core.coordination.Rule;
import com.dream.core.operations.OperationsSet;

/**
 * @author Alessandro Maggi
 *
 */
public abstract class AbstractCoordinatingEntity 
extends AbstractEntity 
implements CoordinatingEntity {

	final static boolean greedy_cache_update = false;

	protected Set<Entity> pool;
	protected Rule rule;
	protected Rule cached_rule;
	protected PoolInteractionsIterator poolInteractionsIterator;
	protected boolean dirtyCache;
	protected Set<Interaction> forbiddenInteractions;

	/**
	 * @param parent
	 */
	public AbstractCoordinatingEntity(Entity parent,Set<Entity> pool,Rule rule) {
		super(parent);
		this.pool = pool;
		setPoolParent();
		this.rule = rule;
		clearCache();
	}

	/**
	 * @param parent
	 * @param pool
	 */
	public AbstractCoordinatingEntity(Entity parent,Set<Entity> pool) {
		this(parent,pool,DummyRule.getInstance());
	}

	/**
	 * @param pool
	 * @param rule
	 */
	public AbstractCoordinatingEntity(Set<Entity> pool,Rule rule) {
		this(null,pool,rule);
	}

	/**
	 * @param pool
	 */
	public AbstractCoordinatingEntity(Set<Entity> pool) {
		this(null,pool,DummyRule.getInstance());
	}

	/**
	 * @param parent
	 */
	public AbstractCoordinatingEntity(Entity parent) {
		this(parent,new HashSet<Entity>(),DummyRule.getInstance());
	}

	/**
	 * 
	 */
	public AbstractCoordinatingEntity() {
		this(null,new HashSet<Entity>(),DummyRule.getInstance());
	}

	/**
	 * @return the pool
	 */
	@Override
	public Set<Entity> getPool() {
		return pool;
	}

	/**
	 * @param pool the pool to set
	 */
	@Override
	public void setPool(Set<Entity> pool) {
		this.pool = pool;
		setPoolParent();
		clearCache();
	}

	/**
	 * @param entities
	 */
	@Override
	public void setPool(Entity... entities) {
		setPool(Arrays.stream(entities).collect(Collectors.toSet()));
	}

	@Override
	public int getPoolSize() {
		return this.pool.size();
	}

	/**
	 * @param entity 
	 */
	@Override
	public void addToPool(Entity entity) {
		entity.setParent(this);
		this.pool.add(entity);
		//clearCache();
	}

	/**
	 * @param entities 
	 */
	@Override
	public void addToPool(Entity... entities) {
		Arrays.stream(entities).forEach(
				e -> {
					e.setParent(this);
					this.pool.add(e);
				}
				);
		//clearCache();
	}

	/**
	 * @param entity 
	 */
	@Override
	public void removeFromPool(Entity entity) {
		this.pool.remove(entity);
		try {
			if (equals(entity.getParent()))
				entity.setParent(null);
		} catch (OrphanEntityException e) {
			// entity already orphan
		}
		//clearCache();
	}
	
	public boolean hosts(Entity entity) {
		return pool.contains(entity);
	}

	/**
	 * @return the rule
	 */
	@Override
	public Rule getRule() {
		return rule;
	}

	/**
	 * @param rule the rule to set
	 */
	@Override
	public void setRule(Rule rule) {
		this.rule = rule;
		clearCache();
	}

//	@Override
//	public int hashCode() {
//		return super.hashCode()
//				+ pool.stream().mapToInt(Entity::hashCode).sum()
//				+ rule.hashCode();
//	}

	@Override
	public boolean equals(Object o) {
		return (o instanceof AbstractCoordinatingEntity) &&
				equals((AbstractCoordinatingEntity) o);
	}

//	public boolean equals(AbstractCoordinatingEntity entity) {
//		return super.equals(entity) &&
//				pool.equals(entity.getPool()) &&
//				rule.equals(entity.getRule());
//	}

	protected void setPoolParent() {
		pool.stream().forEach(e -> e.setParent(this));
	}

	protected void updateCache() {
		pool.stream().forEach(
				e -> {
					if (e instanceof Caching) 
						((Caching) e).clearCache();
				}
				);
		rule.clearCache();
		cached_rule = rule.expandDeclarations();
		poolInteractionsIterator = new PoolInteractionsIterator(pool);
		forbiddenInteractions = new HashSet<>();
		dirtyCache = false;
	}

	@Override
	public void clearCache() {
		if (greedy_cache_update)
			updateCache();
		else {
			dirtyCache = true;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public JSONObject getJSONDescriptor() {		
		JSONObject descriptor = super.getJSONDescriptor();
		JSONArray poolDescriptor = new JSONArray();

		descriptor.put("rule", this.rule.toString());

		pool.stream().forEach(e -> poolDescriptor.add(e.getJSONDescriptor()));
		descriptor.put("pool", poolDescriptor);

		return descriptor;
	}

	@Override
	public Interaction getAllowedInteraction() {
		Interaction interaction;
		if (dirtyCache)
			updateCache();
		boolean sat = false;
		long startTime = System.nanoTime();
		long timeCap = 60000000000L;
		boolean fallbackUsed = false;
		do {
			if (!fallbackUsed && (System.nanoTime() - startTime >= timeCap)) {
				// fallback, try empty interaction if searching is taking too long
				fallbackUsed = true;
				interaction = new Interaction();
				System.out.println(String.format("WARNING: timecap of %d s reached in %s! Attempting fallback interaction {}", 
						timeCap/1000000000,toString()));
			} else
				interaction = poolInteractionsIterator.next();
			sat = cached_rule.sat(interaction);
			if (!sat)
				if (forbiddenInteractions.contains(interaction))
					throw new NoAdmissibleInteractionsException(this);
				else
					forbiddenInteractions.add(interaction);
		} while (!sat);
		return interaction;
	}

	@Override
	public OperationsSet getOperationsForInteraction(Interaction interaction) {
		if (dirtyCache)
			updateCache();
		OperationsSet operations = cached_rule.getOperationsForInteraction(interaction);
		pool.stream().forEach(
				e -> operations.addOperation(e.getOperationsForInteraction(interaction)));
		return operations;
	}

	@Override
	public Rule getExpandedRule() {
		if (dirtyCache)
			updateCache();
		return cached_rule;
	}
	
}
