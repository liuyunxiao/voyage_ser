package com.voyage.facade;

import com.common.socket.message.IRequest;
import com.common.socket.message.IResponse;

public interface IMarketFacade {
	/**
	 * 购买金币
	 * */
	void buyGold(IRequest request, IResponse response) throws Exception;

	/**
	 * 购买物品
	 * */
	void buyGoods(IRequest request, IResponse response) throws Exception;

	/**
	 * 出售物品
	 * */
	void sellGoods(IRequest request, IResponse response) throws Exception;
}
