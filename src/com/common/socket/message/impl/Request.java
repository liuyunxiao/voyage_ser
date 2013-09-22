package com.common.socket.message.impl;

import com.common.socket.message.IRequest;
import com.common.socket.vo.ReqResVO;

public class Request implements IRequest {
	private ReqResVO _reqResVO;

	public Request(ReqResVO vo) {
		_reqResVO = vo;
	}

	public String getIp() {
		return _reqResVO.ip;
	}

	public Object getData() {
		return _reqResVO.data;
	}

	public String getAction() {
		return _reqResVO.action;
	}

	public String getCommand() {
		return _reqResVO.command;
	}

	public Integer getAccountId() {
		return 0;
	}

	public Integer getUserId() {
		return _reqResVO.userId;
	}

	public Integer getSessionId() {
		return _reqResVO.sessionId;
	}

	public ReqResVO getReqResVO() {
		return _reqResVO;
	}
}
