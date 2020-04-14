package com.dream.core.entities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import com.dream.core.Entity;
import com.dream.core.coordination.Interaction;

public class PoolInteractionsIterator implements InteractionsIterator {

	private Entity[] entities;
	private int cursor;
	private List<Interaction>[] entitiesInteractions;
	private Interaction[] lastInteraction;
	private int[] storedIndex;
	private boolean[] interactionsStored;

	public PoolInteractionsIterator(Entity[] pool) {
		this.entities = pool;
		setup();
	}

	public PoolInteractionsIterator(Set<Entity> entities) {
		this.entities = entities.toArray(Entity[]::new);
		setup();
	}

	@SuppressWarnings("unchecked")
	private void setup() {
		cursor = -1;
		entitiesInteractions = new ArrayList[this.entities.length];
		interactionsStored = new boolean[this.entities.length];
		lastInteraction = new Interaction[this.entities.length];
		Arrays.fill(interactionsStored, false);
		storedIndex = new int[this.entities.length];
		Arrays.fill(storedIndex, 0);
	}

	public Interaction next() {
		Interaction interaction;
		if (cursor==-1) {
			cursor = 0;
			for (int i=0; i<entities.length; i++) {
				interaction = entities[i].getAllowedInteraction();
				entitiesInteractions[i] = new ArrayList<Interaction>();
				entitiesInteractions[i].add(interaction);
				lastInteraction[i] = interaction;
			}
			return Interaction.mergeAll(lastInteraction);
		} else {
			boolean check = false;
			while (!check) {
				if (cursor >= entities.length) {
					// all possible interactions computed
					// -> cycle back to first interaction
					cursor =  0;
					//throw new NoMoreInteractions();
					return Interaction.mergeAll(lastInteraction);
				}
				if (!lastInteraction[cursor].isEmpty())
					check = true;
				else {
					storedIndex[cursor] = 0;
					lastInteraction[cursor] = entitiesInteractions[cursor].get(storedIndex[cursor]);
					cursor++;
				}
			}
			if (interactionsStored[cursor]) {
				storedIndex[cursor]++;
				interaction = entitiesInteractions[cursor].get(storedIndex[cursor]);
				lastInteraction[cursor] = interaction;
				cursor = 0;
			} else {
				interaction = entities[cursor].getAllowedInteraction();
				entitiesInteractions[cursor].add(interaction);
				lastInteraction[cursor] = interaction;
				if (interaction.isEmpty())
					interactionsStored[cursor] = true;
				cursor = 0;
			}
			return Interaction.mergeAll(lastInteraction);
		}
	}
}
