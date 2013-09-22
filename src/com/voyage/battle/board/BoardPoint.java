package com.voyage.battle.board;

import java.io.Serializable;

/**
 * 战斗沙盘上的点
 * */
public class BoardPoint implements Serializable {
	private static final long serialVersionUID = 8468249561335338941L;
	public int pos;// 位置 ：左侧 (x,y)+10; 右侧 (x,y)+20
	public int state;// -1: 空位置 0: 死亡 1: 健在

	public int id;//兵种ID
	public int hp;//当前血量
	public int hpMax;//最大血量
	public String img;//兵种头像
	public String bgImg;//兵种背景图片
	public int captain;//是否队长（0：否；1：是），不生成JSON

	public BoardPoint(int pos) {
		this(pos, -1);
	}

	public BoardPoint(int pos, int state) {
		this.pos = pos;
		this.state = state;
	}

	public boolean qEmpty() {
		return state == -1;
	}

	public void dead() {
		setState(0);
	}

	public void alive() {
		setState(1);
	}

	public boolean qAlive() {
		return state == 1;
	}

	public void validIt(int pId, int pHp, int pHpMax, String pImg, String bgImg, int pCaptain) {
		this.id = pId;
		this.hp = pHp;
		this.hpMax = pHpMax;
		this.img = pImg;
		this.bgImg = bgImg;
		this.state = 1;
		this.captain = pCaptain;
	}

	public String getBgImg() {
		return bgImg;
	}

	public void setBgImg(String bgImg) {
		this.bgImg = bgImg;
	}

	public int getPos() {
		return pos;
	}

	public void setPos(int pos) {
		this.pos = pos;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	@Override
	public int hashCode() {
		int h = 17 * 37 + pos;
		return h;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof BoardPoint)) {
			return false;
		}

		BoardPoint other = (BoardPoint) obj;
		return this.pos == other.pos;
	}

}
