package com.voyage.battle.bo;

import java.io.Serializable;

import com.voyage.battle.enums.BenefitType;
import com.voyage.constant.Const;

/**
 *技能特效相关
 */
public class EffectBO implements Serializable {
	private static final long serialVersionUID = -2906398050654880319L;
	public String start;//起手特效
	public String bullet;//弹道
	public String end;//目标特效（目标是被攻击时才会播放）,以英文逗号分隔

	public int id;//技能ID
	public String bubble;//技能泡泡
	public String effect;//技能特效
	public int side;//技能特效显示位置（0：自身，1：目标）
	public int type;//技能声音类别(0:普通，1：增益，2：减益）

	public EffectBO(String start, String bullet, String end) {
		super();
		this.start = start;
		this.bullet = bullet;
		this.end = end;
	}

	public void setNoneSkill() {
		setSkillEffect(0, Const.NONE, Const.NONE, 0, BenefitType.COMMON.getV());
	}

	public void setSkillEffect(int id, String bubble, String effect, int side, int type) {
		this.id = id;
		this.bubble = bubble;
		this.effect = effect;
		this.side = side;
		this.type = type;
	}

	public String getBubble() {
		return bubble;
	}

	public void setBubble(String bubble) {
		this.bubble = bubble;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getBullet() {
		return bullet;
	}

	public void setBullet(String bullet) {
		this.bullet = bullet;
	}

	public String getEffect() {
		return effect;
	}

	public void setEffect(String effect) {
		this.effect = effect;
	}

	public int getSide() {
		return side;
	}

	public void setSide(int side) {
		this.side = side;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

}
