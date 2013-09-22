package com.voyage.data.bo;

/**
 * 配置信息
 * */
public class ConfigBO {
	public int chatInterval;//聊天间隔(单位：秒)
	public double goodsSellFactor;//物品出售系数

	public int getChatInterval() {
		return chatInterval;
	}

	public void setChatInterval(int chatInterval) {
		this.chatInterval = chatInterval;
	}

	public double getGoodsSellFactor() {
		return goodsSellFactor;
	}

	public void setGoodsSellFactor(double goodsSellFactor) {
		this.goodsSellFactor = goodsSellFactor;
	}
}
