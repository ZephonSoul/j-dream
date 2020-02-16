package com.ldream.ldream_core.components;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.ldream.ldream_core.coordination.Interaction;
import com.ldream.ldream_core.coordination.Rule;
import com.ldream.ldream_core.coordination.Term;
import com.ldream.ldream_core.coordination.interactions.Tautology;
import com.ldream.ldream_core.coordination.operations.OperationsSet;

public abstract class AbstractComponent implements Component {

	protected int cId;
	protected Interface cInterface;
	protected Store cStore;
	protected Pool cPool;
	protected Component parent;
	protected Rule cRule;
	protected Rule cRule_cached;
	protected InteractionsIterator interactionsIterator;
	protected ComponentInteractionIterator cIterator;
	protected Map<Port,Action> portActions;

	/**
	 * Constructors
	 */
	
	public AbstractComponent(
			Interface cInterface,
			Rule cRule,
			Store cStore,
			Pool cPool,
			Component parent,
			Map<Port,Action> portActions) {
		
		cId = ComponentsIDFactory.getInstance().getFreshId();
		this.cInterface = cInterface;
		this.cInterface.setOwner(this);
		this.cStore = cStore;
		this.cPool = cPool;
		this.cPool.setComponentsParent(this);
		this.parent = parent;
		this.cRule = cRule;
		this.cRule_cached = cRule.expandDeclarations();
		this.cIterator = new ComponentInteractionIterator(this.cInterface);
		this.interactionsIterator = new InteractionsIterator(this.cInterface);
		this.cIterator.setPoolIterator(this.cPool.getInteractionIterator());
		this.portActions = portActions;
	}

	public AbstractComponent() {
		this(
				new Interface(),
				new Term(new Tautology()), // default rule: allow everything
				new Store(),
				new Pool(),
				null,
				new HashMap<>()
				);
	}

	/**
	 * @return the cInterface
	 */
	public Interface getInterface() {
		return cInterface;
	}

	/**
	 * @return the cPool
	 */
	public Pool getPool() {
		return cPool;
	}

	/**
	 * @param cPool the cPool to set
	 */
	public void setPool(Pool pool) {
		this.cPool = pool;
		this.cPool.setComponentsParent(this);
		this.cIterator.setPoolIterator(this.cPool.getInteractionIterator());
		this.cRule_cached = cRule.expandDeclarations();
	}

	public void addToPool(Component component) {
		component.setParent(this);
		this.cPool.add(component);
	}
	
	public void removeFromPool(Component component) {
		component.setParent(null);
		this.cPool.remove(component);
	}

	public void setInterface(Interface cInterface) {
		this.cInterface = cInterface;
		this.cInterface.setOwner(this);
		this.interactionsIterator = new InteractionsIterator(cInterface);
		this.cIterator.setInterface(cInterface);
	}

	public int getId() {
		return cId;
	}

	/**
	 * @return the store
	 */
	public Store getStore() {
		return cStore;
	}

	public void setStore(Store cStore) {
		this.cStore = cStore;
	}

	/**
	 * @return the parent
	 * @throws OrphanComponentException 
	 */
	public Component getParent() throws OrphanComponentException {
		if (parent == null)
			throw new OrphanComponentException(this);
		return parent;
	}

	/**
	 * @param parent the parent to set
	 */
	public void setParent(Component parent) {
		this.parent = parent;
	}

	public Rule getRule() {
		return cRule;
	}

	public void setRule(Rule cRule) {
		this.cRule = cRule;
		this.cRule_cached = cRule.expandDeclarations();
	}

	public void setPortActions(Map<Port,Action> portActions) {
		this.portActions = portActions;
	}

	public void addPortAction(Port port,Action action) {
		portActions.put(port,action);
	}

	@Override
	public void setStoreVar(String varName, Number varValue) throws InvalidLocalVariableException {
		cStore.setVarValue(varName,varValue);
	}

	@Override
	public LocalVariable getLocalVariable(String varName) throws InvalidLocalVariableException {
		return cStore.getLocalVariable(varName);
	}

	@Override
	public boolean isAtomic() {
		return this.cPool.isEmpty();
	}
	
	@Override
	public int getPoolSize() {
		return this.cPool.size();
	}

	@Override
	public Interaction getAllowedInteraction() {
		Interaction interaction;
		Set<Interaction> forbiddenInteractions = new HashSet<>();
		if (cRule_cached == null)
			cRule_cached = cRule.expandDeclarations();
		boolean sat = false;
		do {
			interaction = cIterator.next();
			sat = cRule_cached.sat(interaction);
			if (!sat)
				if (forbiddenInteractions.contains(interaction))
					throw new NoAdmissibleInteractionsException(this);
				else
					forbiddenInteractions.add(interaction);
		} while (!sat);
		return interaction;
	}

	@Override
	public Interaction[] getAllAllowedInteractions() {
		//		Set<Interaction> localInteractions = interactionsIterator.getAll().stream()
		//				.filter(i -> cRule.sat(i))
		//				.collect(Collectors.toSet());
		//
		//		if (!isAtomic()) {
		//			Set<Interaction> poolInteractions = cPool.getAllowedInteractions();
		//			localInteractions = localInteractions.stream()
		//					.flatMap(i -> poolInteractions.stream()
		//							.map(pi -> Interaction.mergeAll(i,pi)))
		//					.collect(Collectors.toSet());
		//		}
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

	@Override
	public OperationsSet getOperationsForInteraction(Interaction interaction) {
		OperationsSet operations = cRule_cached.getOperationsForInteraction(interaction);
		operations.addOperationsSet(cPool.getOperationsForInteraction(interaction));
		return operations;
	}

	public String getInstanceName() {
		return String.format("%s[%s]",this.getClass().getSimpleName(),cId);
	}

	@Override
	public Port getPortByName(String portName) {
		return cInterface.getPortByName(portName);
	}

	public String toString() {
		String componentDescription = 
				String.format("%s[%s]:\nInterface={%s}\nStore={%s}\nPool={%s}\nRule=%s",
						this.getClass().getSimpleName(),
						cId,
						cInterface.toString(),
						cStore.toString(),
						cPool.toString(true,"\t"),
						cRule.toString());
		return componentDescription;
	}

	@Override
	public String toString(boolean exhaustive,String offset) {
		if (offset==null)
			offset = "";
		if (!exhaustive)
			return toString();
		else 
			return String.format("%1$s%2$s[%3$s]:\n%1$sInterface={%4$s}\n%1$sStore={%5$s}\n%1$sRule=%7$s\n%1$sPool={\n%6$s%1$s}",
					offset,								//1
					this.getClass().getSimpleName(),	//2
					cId,								//3
					cInterface.toString(),				//4
					cStore.toString(),					//5
					cPool.toString(true,offset+"\t"),	//6
					cRule.toString());					//7
	}

	public void refresh() {
		cIterator = new ComponentInteractionIterator(cInterface);
		cPool.refresh();
		cIterator.setPoolIterator(cPool.getInteractionIterator());
		cRule_cached = cRule.expandDeclarations();
	}

	public void activatePort(Port port) {
		if (portActions.containsKey(port))
			portActions.get(port).accept(this);
	}

	@Override
	public Set<Component> getComponentsFromPool() {
		return cPool.getComponents();
	}
	
	public Rule getCurrentRule() {
		return cRule_cached;
	}

}