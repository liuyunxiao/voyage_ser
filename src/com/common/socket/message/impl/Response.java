package com.common.socket.message.impl;

import org.json.JSONObject;

import com.common.socket.ISocket;
import com.common.socket.message.IRequest;
import com.common.socket.message.IResponse;
import com.common.socket.vo.ReqResVO;
import com.common.socket.vo.ResultDataVO;

public class Response implements IResponse {
	private IRequest request;
	private ISocket socket;

	private ReqResVO resVO = null;

	public Response(ISocket nbcSocket, IRequest request) {
		this.request = request;
		this.socket = nbcSocket;
	}

	public void writeTheJson(JSONObject jo) throws Exception {
		ReqResVO vo = new ReqResVO();
		vo.id = request.getReqResVO().id;
		vo.action = request.getAction();
		vo.command = request.getCommand();
		vo.userId = request.getUserId();
		vo.sessionId = request.getSessionId();
		vo.data = jo;

		resVO = vo;
		socket.sendTheJson(jo);
	}

	public ISocket getSocket() {
		return socket;
	}

	public Object getData() throws Exception {
		return null;
	}

	public ReqResVO getResVO() {
		return resVO;
	}

	@Override
	public void writeRtCode(int rtCode) throws Exception {
		writeJson(rtCode, null);
	}

	@Override
	public void writeJson(Object o) throws Exception {
		writeJson(0, o);
	}

	@Override
	public void writeJson(int rtCode, Object o) throws Exception {
		writeTheJson(new JSONObject(new ResultDataVO(rtCode, o)));
	}
}
