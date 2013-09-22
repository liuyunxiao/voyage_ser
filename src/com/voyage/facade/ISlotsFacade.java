package com.voyage.facade;

import com.common.socket.message.IRequest;
import com.common.socket.message.IResponse;

public interface ISlotsFacade {
	/**进入老虎机界面*/
	void enter(IRequest request, IResponse response) throws Exception;

	/**游戏开始*/
	void play(IRequest request, IResponse response) throws Exception;

	/**猜大小*/
	void roll(IRequest request, IResponse response) throws Exception;
}
