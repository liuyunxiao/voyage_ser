package com.voyage.battle.bo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.voyage.battle.board.BoardPoint;
import com.voyage.data.vo.CfgMonsterVO;
import com.voyage.data.vo.SysUserVO;

/**
 * 玩家相关
 * */
public class PlayerBO implements Serializable {
	private static final long serialVersionUID = 6018335996728285346L;
	public int id;//玩家ID
	public String name;//名字
	public int level;//等级
	public int vipLevel;//VIP等级
	public String img;//头像
	public int hp;//当前生命值
	public int hpMax;//最大生命值
	public List<BoardPoint> army = new ArrayList<BoardPoint>(1);//出战列表
	public List<PlayerHeadBO> borrowed = new ArrayList<PlayerHeadBO>(1);//被借公主信息

	public PlayerBO(SysUserVO suVO) {
		this.id = suVO.getSuId();
		this.name = suVO.getSuName();
		this.level = suVO.getSuLevel();
		this.vipLevel = suVO.getSuVipLevel();
		this.img = suVO.getSuImg();
	}

	public PlayerBO(CfgMonsterVO cmVO) {
		this.id = cmVO.getCmId();
		this.name = cmVO.getCmName();
		this.level = cmVO.getCmLevel();
		this.img = cmVO.getCmImg();
	}

	public List<PlayerHeadBO> getBorrowed() {
		return borrowed;
	}

	public void setBorrowed(List<PlayerHeadBO> borrowed) {
		this.borrowed = borrowed;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public int getHp() {
		return hp;
	}

	public void setHp(int hp) {
		this.hp = hp;
	}

	public int getHpMax() {
		return hpMax;
	}

	public void setHpMax(int hpMax) {
		this.hpMax = hpMax;
	}

	public List<BoardPoint> getArmy() {
		return army;
	}

	public void setArmy(List<BoardPoint> army) {
		this.army = army;
	}

}
