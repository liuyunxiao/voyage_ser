package com.voyage.jmx.gamestate;

import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.ObjectName;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.voyage.config.PingyangConfig;
import com.voyage.constant.Const;
import com.voyage.jmx.userstate.UserState;
import com.voyage.jmx.userstate.UserStateMBean;
import com.sun.jdmk.comm.AuthInfo;
import com.sun.jdmk.comm.HtmlAdaptorServer;

@Component
public class GameStateAgent {
	private Logger logger = LoggerFactory.getLogger(GameStateAgent.class);

	public void init() throws Exception {

		MBeanServer mbs = MBeanServerFactory.createMBeanServer();
		final HtmlAdaptorServer htmlAdaptor = new HtmlAdaptorServer();

		ObjectName adaptorName = new ObjectName("GameStateAgent:name=HtmlAdaptorServer");
		mbs.registerMBean(htmlAdaptor, adaptorName);

		AuthInfo authInfo = new AuthInfo("admin", Const.DEFAULT_PWD_JMX);
		htmlAdaptor.addUserAuthenticationInfo(authInfo);
		htmlAdaptor.setPort(PingyangConfig.getInstance().getJmx_port());
		htmlAdaptor.start();

		GameStateMBean operator = new GameState();
		ObjectName name = new ObjectName("GameStateAgent:name=GameState Tool");
		mbs.registerMBean(operator, name);

		UserStateMBean usBean = new UserState();
		ObjectName usName = new ObjectName("UserState:name=UserState Tool");
		mbs.registerMBean(usBean, usName);

		logger.info("JMX GameStateAgent start @" + PingyangConfig.getInstance().getJmx_port() + " ...");
	}
}
