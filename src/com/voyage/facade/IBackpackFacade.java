package com.voyage.facade;

import com.common.socket.message.IRequest;
import com.common.socket.message.IResponse;

public interface IBackpackFacade {

	/**
	 * 移动物品
	 * */
	void move(IRequest request, IResponse response) throws Exception;

	/**
	 * 进入礼物、农场、矿场等界面
	 * */
	void enter(IRequest request, IResponse response) throws Exception;

	/**
	 * 收获养殖
	 * */
	void reapBreed(IRequest request, IResponse response) throws Exception;
}
