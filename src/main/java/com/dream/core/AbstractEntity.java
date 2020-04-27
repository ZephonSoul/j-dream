package com.dream.core;

import org.json.simple.JSONObject;

/**
 * @author Alessandro Maggi
 *
 */
public abstract class AbstractEntity implements Entity {

	final static int BASE_CODE = 1;

	protected int id;
	protected Entity parent;

	/**
	 * @param parent
	 */
	public AbstractEntity(Entity parent) {
		this.id = IDFactory.getInstance().getFreshId();
		this.parent = parent;
	}

	public AbstractEntity() {
		this(null);
	}

	public boolean isOrphan() {
		return parent == null;
	}

	/**
	 * @return the parent
	 * @throws OrphanEntityException 
	 */
	@Override
	public Entity getParent() throws OrphanEntityException {
		if (isOrphan())
			throw new OrphanEntityException(this);
		else
			return parent;
	}

	/**
	 * @param parent the parent to set
	 */
	public void setParent(Entity parent) {
		this.parent = parent;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	@Override
	public int hashCode() {
		return BASE_CODE + id;
	}

	@Override
	public boolean equals(Object o) {
		return (o instanceof AbstractEntity) &&
				equals((AbstractEntity) o);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public JSONObject getJSONDescriptor() {
		JSONObject descriptor = new JSONObject();
		
		descriptor.put("name", this.toString());
		descriptor.put("id", this.id);
		descriptor.put("type", this.getClass().getSimpleName());
		String parentName;
		if (!isOrphan())
			parentName = this.parent.toString();
		else
			parentName = "";
		descriptor.put("parent", parentName);
		
		return descriptor;
	}
	
	@Override
	public String toString() {
		return String.format("%s[%s]",this.getClass().getSimpleName(),id);
	}

}
