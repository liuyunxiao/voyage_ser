package com.common.socket.message;

import com.common.socket.vo.ReqResVO;

public interface IRequest {
	public Object getData();

	public String getIp();

	public String getAction();

	public String getCommand();

	public Integer getAccountId();

	public Integer getUserId();

	public Integer getSessionId();

	public ReqResVO getReqResVO();
}
