package com.voyage.data.bo;

import java.io.Serializable;

import com.voyage.battle.enums.WhetherType;
import com.voyage.config.PingyangConfig;

/**
 * 獎池信息
 * */
public class PondBO implements Serializable {

	private static final long serialVersionUID = 6234283180509965351L;
	public int big;//大奖池
	public int small;//小奖池
	public int bigHit;//大獎是否已經中了(0:否，1：是)
	public int smallHit;//小獎是否已經中了(0:否，1：是)

	public PondBO() {
	}

	public PondBO(int big, int small) {
		this.big = big;
		this.small = small;
	}

	/**
	 * 重置为初始金
	 * */
	public void reset() {
		this.big = PingyangConfig.getInstance().getBigPondInit();
		this.small = PingyangConfig.getInstance().getSmallPondInit();
		clearHit();
	}

	/**
	 * 清除中奖状态
	 * */
	public void clearHit() {
		this.bigHit = WhetherType.NO.getV();
		this.smallHit = WhetherType.NO.getV();
	}

	/**
	 * 根据偏移量刷新奖池
	 * */
	public void updateByOffset(PondBO pond) {
		this.big += pond.big;
		this.small += pond.small;
		this.bigHit += pond.bigHit;
		this.smallHit += pond.smallHit;
	}

	public PondBO(int big, int small, int bigHit, int smallHit) {
		super();
		this.big = big;
		this.small = small;
		this.bigHit = bigHit;
		this.smallHit = smallHit;
	}

	public int getBig() {
		return big;
	}

	public void setBig(int big) {
		this.big = big;
	}

	public int getSmall() {
		return small;
	}

	public void setSmall(int small) {
		this.small = small;
	}

	public int getBigHit() {
		return bigHit;
	}

	public void setBigHit(int bigHit) {
		this.bigHit = bigHit;
	}

	public int getSmallHit() {
		return smallHit;
	}

	public void setSmallHit(int smallHit) {
		this.smallHit = smallHit;
	}

	/**
	 * 是否中奖
	 * */
	public boolean hit() {
		return bigHit + smallHit > 0;
	}
}
