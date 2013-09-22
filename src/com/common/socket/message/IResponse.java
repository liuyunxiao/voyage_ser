package com.common.socket.message;

import org.json.JSONObject;

import com.common.socket.ISocket;
import com.common.socket.vo.ReqResVO;

public interface IResponse {
	public ISocket getSocket();

	public void writeRtCode(int rtCode) throws Exception;

	public void writeJson(Object o) throws Exception;

	public void writeJson(int rtCode, Object o) throws Exception;

	public void writeTheJson(JSONObject jo) throws Exception;

	public Object getData() throws Exception;

	public ReqResVO getResVO();
}
