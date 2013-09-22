package com.voyage.data.bo;

import java.util.List;

import com.voyage.battle.bo.ReplayBO;
import com.voyage.data.vo.SysUserVO;

/**
 * 玩家信息
 * */
public class UserBO {
	public int userId;//玩家ID
	public int gold;//玩家金币数
	public int level;//等级
	public int vipLevel;//VIP等级
	public String name;//名字
	public String img;//头像
	public int nb;//新战报条数
	public Integer zg;//分时间段登录时系统送的钱
	public List<MsgBO> msgs;//离线消息
	public ConfigBO config;//配置信息
	public int nt;//已完成的任务数
	public ReplayBO replay;//新手教程中播放的战报
	public int ndt;//可领取的活跃奖励数
	public int fc;//是否已领取首充奖励(0:否，1：是)

	public int getFc() {
		return fc;
	}

	public void setFc(int fc) {
		this.fc = fc;
	}

	public int getNdt() {
		return ndt;
	}

	public void setNdt(int ndt) {
		this.ndt = ndt;
	}

	public ReplayBO getReplay() {
		return replay;
	}

	public void setReplay(ReplayBO replay) {
		this.replay = replay;
	}

	public int getNt() {
		return nt;
	}

	public void setNt(int nt) {
		this.nt = nt;
	}

	public ConfigBO getConfig() {
		return config;
	}

	public void setConfig(ConfigBO config) {
		this.config = config;
	}

	public List<MsgBO> getMsgs() {
		return msgs;
	}

	public void setMsgs(List<MsgBO> msgs) {
		this.msgs = msgs;
	}

	public Integer getZg() {
		return zg;
	}

	public void setZg(Integer zg) {
		this.zg = zg;
	}

	public int getNb() {
		return nb;
	}

	public void setNb(int nb) {
		this.nb = nb;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getVipLevel() {
		return vipLevel;
	}

	public void setVipLevel(int vipLevel) {
		this.vipLevel = vipLevel;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getGold() {
		return gold;
	}

	public void setGold(int gold) {
		this.gold = gold;
	}

	public UserBO(SysUserVO suVO) {
		this.userId = suVO.getSuId();
		this.gold = suVO.getSuGoldVO().getSugStorage();
		this.level = suVO.getSuLevel();
		this.vipLevel = suVO.getSuVipLevel();
		this.name = suVO.getSuName();
		this.img = suVO.getSuImg();
		this.zg = suVO.getZg();
		this.fc = suVO.getSuFirstCharge();
	}
}
