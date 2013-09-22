package com.voyage.data.bo;

import java.io.Serializable;

/**
 * 活动副本节点
 * */
public class ActivityMapBO implements Serializable {
	private static final long serialVersionUID = -1990484903915281582L;
	public int camId;//活动副本ID
	public int yet;//已挑战次数

	public ActivityMapBO() {
	}

	public ActivityMapBO(int camId, int yet) {
		super();
		this.camId = camId;
		this.yet = yet;
	}

	public int getCamId() {
		return camId;
	}

	public void setCamId(int camId) {
		this.camId = camId;
	}

	public int getYet() {
		return yet;
	}

	public void setYet(int yet) {
		this.yet = yet;
	}
}
