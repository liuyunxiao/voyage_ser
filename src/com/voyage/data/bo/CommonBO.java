package com.voyage.data.bo;

import java.util.ArrayList;
import java.util.List;

/**
 * 公共信息更新
 * */
public class CommonBO {
	public Integer userId;//玩家ID
	public Integer gold;//玩家金币数
	public Integer level;//等级
	public Integer vipLevel;//VIP等级
	public Integer nb;//新战报条数（偏移量）
	public List<MsgBO> msgs = new ArrayList<MsgBO>(1);//新消息列表（偏移量）
	public Integer nt;///已完成的任务数(偏移量)
	public Integer ndt;//可领取的活跃奖励数(偏移量）

	public Integer getNdt() {
		return ndt;
	}

	public void setNdt(Integer ndt) {
		this.ndt = ndt;
	}

	public Integer getNt() {
		return nt;
	}

	public void setNt(Integer nt) {
		this.nt = nt;
	}

	public CommonBO(Integer userId) {
		super();
		this.userId = userId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public List<MsgBO> getMsgs() {
		return msgs;
	}

	public void setMsgs(List<MsgBO> msgs) {
		this.msgs = msgs;
	}

	public Integer getGold() {
		return gold;
	}

	public void setGold(Integer gold) {
		this.gold = gold;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Integer getVipLevel() {
		return vipLevel;
	}

	public void setVipLevel(Integer vipLevel) {
		this.vipLevel = vipLevel;
	}

	public Integer getNb() {
		return nb;
	}

	public void setNb(Integer nb) {
		this.nb = nb;
	}

}
