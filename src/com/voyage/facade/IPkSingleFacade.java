package com.voyage.facade;

import com.common.socket.message.IRequest;
import com.common.socket.message.IResponse;

public interface IPkSingleFacade {
	/**
	 * 挑战某人
	 * 
	 */
	void challenge(IRequest request, IResponse response) throws Exception;

	/**进入对战界面*/
	void enter(IRequest request, IResponse response) throws Exception;

}
