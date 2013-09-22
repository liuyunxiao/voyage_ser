package com.common.socket;

import java.io.IOException;

import org.apache.mina.core.session.IoSession;
import org.json.JSONObject;

import com.common.socket.vo.ResultDataVO;

public class SessionSocket implements ISocket {
	private IoSession session;

	public SessionSocket(IoSession session) {
		this.session = session;
	}

	@Override
	public void sendJson(Object obj) throws Exception {
		sendTheJson(new JSONObject(new ResultDataVO(0, obj)));
	}

	@Override
	public void sendTheJson(JSONObject jo) throws Exception {
		if (!session.isConnected() || jo == null)
			return;

		// logger.debug("send bytes size :" + (int)bytes.length / 1024.0 + "k");
		// session.write(jo.toString().getBytes());
		session.write(jo);
	}

	@Override
	public void closeSocket() throws IOException {
		session.close(true);
	}

	@Override
	public void setAccountId(Integer accountId) {
		SessionData sessionData = (SessionData) session.getAttribute("sessionData");
		sessionData.accountId = accountId;
	}

	@Override
	public void setUserId(Integer userId) {
		SessionData sessionData = (SessionData) session.getAttribute("sessionData");
		sessionData.userId = userId;
	}

	@Override
	public IoSession getSession() {
		return this.session;
	}

}
