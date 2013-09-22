package com.voyage.service;

import com.voyage.enums.ExpenseType;

public interface IMarketService {
	/**
	 * 记录消费日志
	 * */
	void logExpense(ExpenseType et, int userId, int expenseId, int cost, String desc) throws Exception;

	/**
	 * 购买金币  并记录日志
	 * */
	void buyGold(int userId, int oppoId, int rechargeId, String desc) throws Exception;

	/**
	 * 购物品 并记录日志
	 * */
	void buyGoods(int userId, int oppoId, int goodsId) throws Exception;

	/**
	 * 出售物品
	 * */
	void sellGoods(int userId, int subId) throws Exception;
}
