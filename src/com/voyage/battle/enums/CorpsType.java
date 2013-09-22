package com.voyage.battle.enums;

/**
 * 兵种类别（与数据库保持一致）
 * */
public enum CorpsType {
	C1(1), //步兵    
	C2(2), //弓兵    
	C3(3), //骑兵    
	C4(4), //战车    
	C5(5), //器械    
	C6(6), //舞女
	C7(7);//诗人
	private int v;

	public int getV() {
		return v;
	}

	private CorpsType(int v) {
		this.v = v;
	}

	/**是否是纯BUFF兵种（特殊兵种）*/
	public static boolean isSpecailCorps(int v) {
		return v == C6.v || v == C7.v;
	}
}
