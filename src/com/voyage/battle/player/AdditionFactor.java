package com.voyage.battle.player;

import java.io.Serializable;

/**加成相关*/
public class AdditionFactor implements Serializable {
	private static final long serialVersionUID = -5418086542996635050L;

	public AdditionDigit digit = new AdditionDigit();// 数值加成
	public AdditionDigit factor = new AdditionDigit();// 系数加成

	public boolean dizzy;//眩晕

	public AdditionFactor add(AdditionFactor af) {
		digit.add(af.digit);
		factor.add(af.factor);

		this.dizzy = this.dizzy || af.dizzy;
		return this;
	}

}
