package com.common.socket;

import java.io.IOException;

import org.apache.mina.core.session.IoSession;
import org.json.JSONObject;

public interface ISocket {

	public void sendJson(Object obj) throws Exception;

	public void sendTheJson(JSONObject jo) throws Exception;

	public void closeSocket() throws IOException;

	public void setAccountId(Integer accountId);

	public void setUserId(Integer userId);

	IoSession getSession();
}
