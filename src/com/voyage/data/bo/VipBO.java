package com.voyage.data.bo;

import java.util.ArrayList;
import java.util.List;

import com.voyage.battle.bo.ExpBO;

/**
 * VIP界面信息
 * */
public class VipBO {
	public String img;//头像
	public int level;//等级
	public ExpBO vipExp;//VIP信息
	public List<Integer> vips = new ArrayList<Integer>(10);//VIP主键列表

	public ExpBO getVipExp() {
		return vipExp;
	}

	public void setVipExp(ExpBO vipExp) {
		this.vipExp = vipExp;
	}

	public List<Integer> getVips() {
		return vips;
	}

	public void setVips(List<Integer> vips) {
		this.vips = vips;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

}
