package com.ldream.ldream_core.components;

import java.util.Set;

import com.ldream.ldream_core.coordination.Interaction;
import com.ldream.ldream_core.coordination.Rule;
import com.ldream.ldream_core.coordination.operations.OperationsSet;

public interface Component {
	
	public Component getParent() throws OrphanComponentException;
	
	public void setParent(Component parent);

	public void setStoreVar(String varName,Number val) throws InvalidLocalVariableException;
	
	public LocalVariable getLocalVariable(String varName) throws InvalidLocalVariableException;
	
	public void activatePort(Port port);
	
	public void refresh();
	
	public boolean isAtomic();

	public Interaction getAllowedInteraction();
	
	public Interaction[] getAllAllowedInteractions();
	
	public OperationsSet getOperationsForInteraction(Interaction interaction);
	
	public Set<Component> getComponentsFromPool();
	
	public int getPoolSize();
	
	public void addToPool(Component component);

	public void removeFromPool(Component component);
	
	public Port getPortByName(String portName);
	
	public String getInstanceName();
	
	public String toString(boolean exhaustive,String offset);

	public int getId();
	
	public Rule getCurrentRule();

}
