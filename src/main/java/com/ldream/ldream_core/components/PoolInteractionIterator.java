package com.ldream.ldream_core.components;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import com.ldream.ldream_core.coordination.Interaction;

public class PoolInteractionIterator {

	private Component[] components;
	private int cursor;
	private List<Interaction>[] componentsInteractions;
	private Interaction[] lastInteraction;
	private int[] storedIndex;
	private boolean[] interactionsStored;

	public PoolInteractionIterator(Component[] components) {
		this.components = components;
		setup();
	}

	public PoolInteractionIterator(Set<Component> components) {
		this.components = (Component[]) components.toArray();
		setup();
	}

	@SuppressWarnings("unchecked")
	private void setup() {
		cursor = -1;
		componentsInteractions = new ArrayList[this.components.length];
		interactionsStored = new boolean[this.components.length];
		lastInteraction = new Interaction[this.components.length];
		Arrays.fill(interactionsStored, false);
		storedIndex = new int[this.components.length];
		Arrays.fill(storedIndex, 0);
	}

	public Interaction next() {
		Interaction interaction;
		if (cursor==-1) {
			cursor = 0;
			for (int i=0; i<components.length; i++) {
				interaction = components[i].getAllowedInteraction();
				componentsInteractions[i] = new ArrayList<Interaction>();
				componentsInteractions[i].add(interaction);
				lastInteraction[i] = interaction;
			}
			return Interaction.mergeAll(lastInteraction);
		} else {
			boolean check = false;
			while (!check) {
				if (cursor >= components.length) {
					// cycle back to first interaction
					cursor =  0;
					return Interaction.mergeAll(lastInteraction);
				}
				if (!lastInteraction[cursor].isEmpty())
					check = true;
				else {
					storedIndex[cursor] = 0;
					lastInteraction[cursor] = componentsInteractions[cursor].get(storedIndex[cursor]);
					cursor++;
				}
			}
			if (interactionsStored[cursor]) {
				storedIndex[cursor]++;
				interaction = componentsInteractions[cursor].get(storedIndex[cursor]);
				lastInteraction[cursor] = interaction;
				cursor = 0;
			} else {
				interaction = components[cursor].getAllowedInteraction();
				componentsInteractions[cursor].add(interaction);
				lastInteraction[cursor] = interaction;
				if (interaction.isEmpty())
					interactionsStored[cursor] = true;
				cursor = 0;
			}
			return Interaction.mergeAll(lastInteraction);
		}
	}

}
