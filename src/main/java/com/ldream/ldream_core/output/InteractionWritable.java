package com.ldream.ldream_core.output;

import com.ldream.ldream_core.coordination.Interaction;

public class InteractionWritable implements Writable {
	
	private Interaction interaction;

	public InteractionWritable(Interaction interaction) {
		this.interaction = interaction;
	}
	
	public String getString() {
		return "Performed interaction = " + interaction.toString();
	}

}
