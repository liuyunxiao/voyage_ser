package com.voyage.jmx;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.voyage.jmx.gamestate.GameStateAgent;

@Component("jmxServer")
public class JMXServer {

	@Autowired
	private GameStateAgent gameStateAgent;
	
	public void start() throws Exception {
		gameStateAgent.init();
	}
}
