package com.common.socket.vo;

public class ReqResVO {
	public Integer id;

	public String ip;

	// public Integer accountId;
	public Integer userId;
	public Integer sessionId;

	public String action;
	public String command;

	public Object data;

	@Override
	public String toString() {
		String dataStr = data != null ? data.toString() : "";
		return "{userId:" + userId + ",sessionId:" + sessionId + ",action:"
				+ action + ",command:" + command + ",data:" + dataStr + "}";
	}
}
