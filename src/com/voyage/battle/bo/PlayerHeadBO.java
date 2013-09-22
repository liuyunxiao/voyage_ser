package com.voyage.battle.bo;

import java.io.Serializable;

import com.voyage.data.vo.SysUserVO;

/**
 * 头像信息
 * */
public class PlayerHeadBO implements Serializable {
	private static final long serialVersionUID = -7034885564465378950L;
	public String name;//名字
	public int level;//等级
	public int vipLevel;//VIP等级
	public String img;//头像

	public PlayerHeadBO(SysUserVO suVO) {
		this.name = suVO.getSuName();
		this.level = suVO.getSuLevel();
		this.vipLevel = suVO.getSuVipLevel();
		this.img = suVO.getSuImg();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

}
