package com.voyage.webservice;

import javax.xml.ws.Endpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.voyage.config.PingyangConfig;
import com.voyage.webservice.impl.PyWebServiceImpl;

public class WebServiceServer {
	private Logger logger = LoggerFactory.getLogger(WebServiceServer.class);
	private static WebServiceServer instance;

	public static WebServiceServer getInstance() {
		if (instance == null)
			instance = new WebServiceServer();
		return instance;
	}

	public void start() throws Exception {
		final String wsUrl = PingyangConfig.getInstance().getWsUrl();
		new Thread() {
			@Override
			public void run() {
				Endpoint.publish(wsUrl + IPyWebService.class.getSimpleName(), PyWebServiceImpl.getInstance());
				logger.info("start webservice server... ");
			}
		}.start();
	}
}
