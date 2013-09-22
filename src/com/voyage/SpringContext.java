package com.voyage;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringContext {

	private static SpringContext instance;
	private ApplicationContext springContext;

	private SpringContext() {
		// springContext = new ClassPathXmlApplicationContext(new String[] {
		// "applicationContext*.xml" });
		springContext = new ClassPathXmlApplicationContext(
				"applicationContext.xml");
	}

	public static SpringContext getInstance() {
		if (instance == null) {
			instance = new SpringContext();
		}
		return instance;
	}

	public ApplicationContext getSpringContext() {
		return springContext;
	}
}
