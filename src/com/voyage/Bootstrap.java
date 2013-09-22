package com.voyage;

import org.apache.log4j.PropertyConfigurator;
import org.springframework.context.ApplicationContext;

import com.voyage.battle.entry.Battle;
import com.voyage.battle.thread.ReplayWriteThread;
import com.voyage.cache.CfgDataMgr;
import com.voyage.cache.CommonMgr;
import com.voyage.cache.RankMgr;
import com.voyage.cache.RelationMgr;
import com.voyage.cache.SlotsMgr;
import com.voyage.config.BattleConfig;
import com.voyage.config.PingyangConfig;
import com.voyage.constant.Const;
import com.voyage.jmx.JMXServer;
import com.voyage.schedule.ScheduleDefault;
import com.voyage.socket.SocketServer;
import com.voyage.webservice.WebServiceServer;

public class Bootstrap {

	public static void main(String[] args) throws Exception {

		PropertyConfigurator.configure(Thread.currentThread().getContextClassLoader().getResource(Const.LOG4J_CONFIG).getPath());

		ApplicationContext springContext = SpringContext.getInstance().getSpringContext();
		final SocketServer socketServer = (SocketServer) springContext.getBean("socketServer");
		JMXServer jmxServer = (JMXServer) springContext.getBean("jmxServer");
		try {
			PingyangConfig.getInstance();
			BattleConfig.getInstance();
			CfgDataMgr.getInstance().reloadCache();// 加载CFG数据

			ReplayWriteThread.getInstance().startMe();
			SlotsMgr.getInstance().reload();
			RankMgr.getInstance().reload();
			RelationMgr.getInstance().reload();
			CommonMgr.getInstance().reload();
			Battle.getInstance();//加载战斗相关
			ScheduleDefault.getInstance().start();//定时作业
			jmxServer.start();// JMX
			socketServer.start();
			WebServiceServer.getInstance().start();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
				public void run() {
					socketServer.stop();
				}
			}));
		}
	}
}
