package com.voyage.cache;

public class WrapBoolean {
	private boolean va;//是否正在使用

	public WrapBoolean(boolean va) {
		this.va = va;
	}

	public boolean getVa() {
		return va;
	}

	public void setVa(boolean va) {
		this.va = va;
	}
}
