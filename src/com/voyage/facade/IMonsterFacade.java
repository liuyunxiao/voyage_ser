package com.voyage.facade;

import com.common.socket.message.IRequest;
import com.common.socket.message.IResponse;

public interface IMonsterFacade {
	/**
	 * 挑战某个怪物
	 * 
	 */
	void challenge(IRequest request, IResponse response) throws Exception;

	/**
	 * 进入大关卡界面
	 * */
	void enterNodeType(IRequest request, IResponse response) throws Exception;

	/**
	 * 进入小关卡界面
	 * */
	void enterNode(IRequest request, IResponse response) throws Exception;

	/**
	 * 进入联盟界面
	 * */
	void enterAlliance(IRequest request, IResponse response) throws Exception;

	/**
	 * 结盟
	 * */
	void ally(IRequest request, IResponse response) throws Exception;
	
	/**
	 * 挑战某个怪物
	 * 
	 */
	void challengeAct(IRequest request, IResponse response) throws Exception;

	/**
	 * 进入活动副本界面
	 * */
	void enterActivity(IRequest request, IResponse response) throws Exception;
}
