package com.voyage.enums;

/**
 * 关系处理操作类别
 * */
public enum RelationDealType {
	REFUSE(0), //拒绝
	ACCEPT(1), //同意
	APPLY(2), //发出好友申请
	DELETE(3);//删除
	private int v;

	public int getV() {
		return this.v;
	}

	private RelationDealType(int v) {
		this.v = v;
	}

	public static RelationDealType parse(int value) {
		RelationDealType[] enums = RelationDealType.values();
		for (RelationDealType tempEnum : enums) {
			if (tempEnum.getV() == value) {
				return tempEnum;
			}
		}
		return null;
	}
}
