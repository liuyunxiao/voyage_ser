package com.voyage.cache;

/**
 * 消息KEY
 * */
public class MsgAttrKey {
	private int uId1;//玩家1主键
	private int uId2;//玩家2ID

	public MsgAttrKey(int uId1, int uId2) {
		super();
		this.uId1 = uId1;
		this.uId2 = uId2;
	}

	public int getuId1() {
		return uId1;
	}

	public void setuId1(int uId1) {
		this.uId1 = uId1;
	}

	public int getuId2() {
		return uId2;
	}

	public void setuId2(int uId2) {
		this.uId2 = uId2;
	}

	@Override
	public int hashCode() {
		int h = 17 * 37 + this.uId1 + this.uId2;
		return h;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof MsgAttrKey)) {
			return false;
		}

		MsgAttrKey other = (MsgAttrKey) obj;

		return (this.uId1 == other.uId1 && this.uId2 == other.uId2) || (this.uId1 == other.uId2 && this.uId2 == other.uId1);
	}

}
