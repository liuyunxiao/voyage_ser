package com.voyage.enums;

/**
 * 消息格式(与cfg_msg_format表主键一致)
 * */
public enum MsgFormatType {
	DEFAULT(0), //默认
	GIVE_GOLD(1), //赠送金币
	BORROW(2), //借公主
	DEFEND_WIN(3), //守城成功
	DEFEND_LOST(4), //守城失败
	RANDOM_BATTLE(5), //随机战斗
	GIVE_GIFT(6), //赠送礼物
	GIVE_RECHARGE(7), //代充
	SLOTS_RP(8), //老虎机RP爆发
	SLOTS_POND(9), //老虎机中奖池
	WELCOME(10);//建号欢迎词
	private int v;

	public int getV() {
		return this.v;
	}

	private MsgFormatType(int v) {
		this.v = v;
	}

}
