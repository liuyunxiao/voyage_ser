package com.voyage.facade;

import com.common.socket.message.IRequest;
import com.common.socket.message.IResponse;

public interface IRelationFacade {
	/**进入好友界面*/
	void enter(IRequest request, IResponse response) throws Exception;

	/**处理好友关系*/
	void deal(IRequest request, IResponse response) throws Exception;

	/**进入赠送金币界面*/
	void enterGive(IRequest request, IResponse response) throws Exception;

	/**赠送金币*/
	void give(IRequest request, IResponse response) throws Exception;

	/**
	 * 向他人发消息
	 * */
	void say(IRequest request, IResponse response) throws Exception;
}
