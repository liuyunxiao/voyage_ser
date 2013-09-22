package com.voyage.facade;

import com.common.socket.message.IRequest;
import com.common.socket.message.IResponse;

public interface ICorpsFacade {
	/**
	 * 进入布阵界面
	 * 
	 */
	void enterCorps(IRequest request, IResponse response) throws Exception;

	/**
	 * 将兵种从某个位置上移除
	 * 
	 */
	//void takeOff(IRequest request, IResponse response) throws Exception;

	/**
	 * 重置布阵
	 * 
	 */
	//void resetCorps(IRequest request, IResponse response) throws Exception;

	/**
	 * 招募兵种到某个位置上
	 * 
	 */
	//	void hire(IRequest request, IResponse response) throws Exception;

	/**
	 * 保存位置移动、设置或取消队长
	 * 
	 */
	void moveAndCaptain(IRequest request, IResponse response) throws Exception;

	/**
	 * 进入英雄府加点界面
	 * 
	 */
	void enterDot(IRequest request, IResponse response) throws Exception;

	/**
	 * 英雄府 --洗点
	 * 
	 */
	void resetDot(IRequest request, IResponse response) throws Exception;

	/**
	 * 英雄府 --加点
	 * 
	 */
	void addDot(IRequest request, IResponse response) throws Exception;
}
