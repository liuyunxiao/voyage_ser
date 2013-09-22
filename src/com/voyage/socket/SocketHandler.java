package com.voyage.socket;

import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.common.socket.SessionData;
import com.common.socket.SessionSocket;
import com.common.socket.message.IRequest;
import com.common.socket.message.IResponse;
import com.common.socket.message.impl.Request;
import com.common.socket.message.impl.Response;
import com.common.socket.vo.ReqResVO;
import com.common.socket.vo.ResultDataVO;
import com.common.util.ReflectUtil;
import com.voyage.SpringContext;
import com.voyage.constant.Const;
import com.voyage.constant.ErCode;
import com.voyage.exception.NotifyException;
import com.voyage.facade.impl.UserFacade;

@Component
public class SocketHandler extends IoHandlerAdapter {

	private Logger logger = LoggerFactory.getLogger(SocketHandler.class);

	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		try {
			if (message instanceof String) {
				messageReceived(session, (String) message);
			}
		} catch (Exception e) {
			logger.error("└message received", e);
		}
	}

	private void messageReceived(IoSession session, String s) throws Exception {
		JSONObject jo = new JSONObject(s);
		ReqResVO vo = new ReqResVO();
		vo.data = jo;
		String[] facadeKey = jo.getString(Const.FACADE_KEY).split(Const.DOT_SEP);
		vo.action = facadeKey[0];
		vo.command = facadeKey[1];
		vo.userId = jo.getInt(Const.USERID);

		messageReceived(session, vo);
	}

	private void messageReceived(IoSession session, ReqResVO vo) throws Exception {
		ApplicationContext springContext = SpringContext.getInstance().getSpringContext();
		if (!springContext.containsBean(vo.action)) {
			logger.error("└facade " + vo.action + " not exist!");
			return;
		}

		Object facade = springContext.getBean(vo.action);
		Object[] methodArgs = new Object[2];
		vo.ip = getSessionIp(session);
		IRequest request = new Request(vo);
		IResponse response = new Response(new SessionSocket(session), request);
		methodArgs[0] = request;
		methodArgs[1] = response;

		try {
			ReflectUtil.invokeMethod(facade, vo.command, methodArgs, true);

		} catch (Exception e) {
			int rtCode = ErCode.E_1;
			boolean ne = false;
			Throwable te = null;
			if (e instanceof InvocationTargetException) {
				te = ((InvocationTargetException) e).getTargetException();
				if (te instanceof NotifyException) {
					ResultDataVO rdVO = ((NotifyException) te).getRtVO();
					rtCode = rdVO.getRtCode();
					response.writeJson(rtCode, rdVO.getData());
					ne = true;
				}
			}
			if (!ne) {
				response.writeJson(rtCode, te == null ? e.getMessage() : te.getMessage());
			}
			String info = "└handle res(" + vo.action + "." + vo.command + "),ip:" + vo.ip + ",rtCode:" + rtCode;
			logger.error(info, e);
		}
	}

	private String getSessionIp(IoSession session) {
		InetSocketAddress address = (InetSocketAddress) session.getRemoteAddress();
		if (address != null && address.getAddress() != null)
			return address.getAddress().getHostAddress();
		else
			return "";
	}

	@Override
	public void sessionCreated(IoSession session) throws Exception {
		logger.info("sessionCreated() sessionId=" + session.getId());
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		SessionData sessionData = (SessionData) session.getAttribute("sessionData");
		if (sessionData != null && !sessionData.ignoreClosedDispose && (sessionData.userId != null || sessionData.accountId != null)) {
			UserFacade.getInstance().logout(sessionData.userId, sessionData.accountId);
		}

		logger.info("sessionClosed() sessionId=" + session.getId());
		session.close(true);
	}

	@Override
	public void sessionOpened(IoSession session) throws Exception {
		logger.info("sessionOpened() sessionId=" + session.getId());

		SessionData sessionData = (SessionData) session.getAttribute("sessionData");
		if (sessionData == null) {
			sessionData = new SessionData();
			session.setAttribute("sessionData", sessionData);
		}
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
		logger.info("sessionIdle() sessionId=" + session.getId());
		session.close(true);
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
		// logger.warn("exceptionCaught() " + cause.getLocalizedMessage(),
		// cause);
		logger.warn("exceptionCaught() " + cause.getLocalizedMessage());
		session.close(true);
	}

}
