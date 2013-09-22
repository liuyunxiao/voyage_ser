package com.voyage.socket;

import java.net.InetSocketAddress;

import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.filter.executor.OrderedThreadPoolExecutor;
import org.apache.mina.transport.socket.SocketAcceptor;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.common.socket.mina.codec.JSONCodecFactory;
import com.voyage.config.PingyangConfig;

@Component("socketServer")
public class SocketServer {
	private Logger logger = LoggerFactory.getLogger(SocketServer.class);
	@Autowired
	private SocketHandler handler;

	private SocketAcceptor socketAcceptor;

	public void start() throws Exception {
		socketAcceptor = new NioSocketAcceptor();
		socketAcceptor.setReuseAddress(true);
		socketAcceptor.setHandler(handler);
		socketAcceptor.getSessionConfig().setSoLinger(0);
		socketAcceptor.getFilterChain().addLast("threadPool", new ExecutorFilter(new OrderedThreadPoolExecutor(64)));
		socketAcceptor.getFilterChain().addLast("protocol", new ProtocolCodecFilter(new JSONCodecFactory()));
		socketAcceptor.bind(new InetSocketAddress(PingyangConfig.getInstance().getSocket_port()));
		logger.info("socketserver start @" + PingyangConfig.getInstance().getSocket_port() + " ...");
	}

	public void stop() {
		if (socketAcceptor != null) {
			socketAcceptor.unbind();
			socketAcceptor.dispose();
			socketAcceptor = null;
		}
	}
}
