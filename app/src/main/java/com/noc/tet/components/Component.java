package com.noc.tet.components;

import com.noc.tet.activities.GameActivity;

public abstract class Component {

	protected GameActivity host;
	
	public Component(GameActivity ga) {
		host = ga;
	}

	public void reconnect(GameActivity ga) {
		host = ga;
	}

	public void disconnect() {
		host = null;
	}
	
}
