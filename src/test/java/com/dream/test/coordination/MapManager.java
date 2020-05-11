package com.dream.test.coordination;

import java.util.HashSet;
import java.util.Set;

import com.dream.ExecutionEngine;
import com.dream.core.Entity;
import com.dream.core.coordination.Declaration;
import com.dream.core.coordination.EntityInstanceActual;
import com.dream.core.coordination.EntityInstanceRef;
import com.dream.core.coordination.FOILRule;
import com.dream.core.coordination.Quantifier;
import com.dream.core.coordination.Rule;
import com.dream.core.coordination.Term;
import com.dream.core.coordination.maps.MapNodeRef;
import com.dream.core.entities.AbstractMotif;
import com.dream.core.entities.CoordinatingEntity;
import com.dream.core.entities.maps.MapNode;
import com.dream.core.entities.maps.MotifMap;
import com.dream.core.entities.maps.predefined.ArrayMap;
import com.dream.core.exec.GreedyStrategy;
import com.dream.core.operations.CreateMapNode;
import com.dream.core.operations.MigrateMotif;
import com.dream.core.output.ConsoleOutput;

public class MapManager extends AbstractMotif {

	public MapManager(Entity parent, Set<Entity> pool, MotifMap map) {
		super(parent, pool, map);
		for (Entity e : pool) {
			MapNode node = map.createNode();
			setEntityPosition(e, node);
		}
		EntityInstanceActual scope = new EntityInstanceActual(this);
		// \forall c:* {true -> addNode(this,n)[migrate(c,this,n)]}
		Declaration allC = new Declaration(
				Quantifier.FORALL,
				scope);
		EntityInstanceRef c = allC.getVariable();
		MapNodeRef n = new MapNodeRef();
		Rule r = new FOILRule(allC,new Term(
				new CreateMapNode(scope, n,
						new MigrateMotif(c, scope, n))
				));
		setRule(r);
	}
	
	public static void main(String[] args) {
		DullComponent c = new DullComponent();
		Set<Entity> pool = new HashSet<>();
		pool.add(c);
		MotifMap map = new ArrayMap(0,null);
		CoordinatingEntity root = new MapManager(null,pool,map);
		
		ExecutionEngine ex = new ExecutionEngine(root,GreedyStrategy.getInstance(),new ConsoleOutput(),false,2);
		ex.setSnapshotSemantics(true);
		ex.run();
	}

}
