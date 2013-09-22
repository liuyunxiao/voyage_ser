package com.voyage.facade;

import com.common.socket.message.IRequest;
import com.common.socket.message.IResponse;

public interface ITaskFacade {

	/**领取任务奖励*/
	void reap(IRequest request, IResponse response) throws Exception;

	/**进入任务界面*/
	void enter(IRequest request, IResponse response) throws Exception;

	/**领取活跃奖励*/
	void reapDaily(IRequest request, IResponse response) throws Exception;

	/**进入活跃奖励界面*/
	void enterDaily(IRequest request, IResponse response) throws Exception;
}