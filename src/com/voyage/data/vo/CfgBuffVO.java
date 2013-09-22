package com.voyage.data.vo;

import java.io.Serializable;

import com.voyage.battle.buff.Buff;
import com.voyage.battle.enums.WhetherType;
import com.voyage.battle.player.AdditionFactor;

public class CfgBuffVO implements Serializable {
	private static final long serialVersionUID = 7848627681685183479L;

	private Integer cbId;

	private String cbName;

	private Integer cbType;

	private String cbDesc;

	private String cbBattleImg;

	private Integer cbCalcType;

	private Integer cbContinues;

	private Double cbIncAtk;

	private Double cbIncDef;

	private Double cbIncDam;

	private Double cbDecAtk;

	private Double cbDecDef;

	private Double cbDecDam;
	private Double cbAntiDam;
	private Double cbIncHp;

	private Integer cbDizzy;
	private Integer cbKeep;

	public Double getCbAntiDam() {
		return cbAntiDam;
	}

	public void setCbAntiDam(Double cbAntiDam) {
		this.cbAntiDam = cbAntiDam;
	}

	public Integer getCbKeep() {
		return cbKeep;
	}

	public void setCbKeep(Integer cbKeep) {
		this.cbKeep = cbKeep;
	}

	public Integer getCbId() {
		return cbId;
	}

	public void setCbId(Integer cbId) {
		this.cbId = cbId;
	}

	public String getCbName() {
		return cbName;
	}

	public void setCbName(String cbName) {
		this.cbName = cbName;
	}

	public Integer getCbType() {
		return cbType;
	}

	public void setCbType(Integer cbType) {
		this.cbType = cbType;
	}

	public String getCbDesc() {
		return cbDesc;
	}

	public void setCbDesc(String cbDesc) {
		this.cbDesc = cbDesc;
	}

	public String getCbBattleImg() {
		return cbBattleImg;
	}

	public void setCbBattleImg(String cbBattleImg) {
		this.cbBattleImg = cbBattleImg;
	}

	public Integer getCbCalcType() {
		return cbCalcType;
	}

	public void setCbCalcType(Integer cbCalcType) {
		this.cbCalcType = cbCalcType;
	}

	public Integer getCbContinues() {
		return cbContinues;
	}

	public void setCbContinues(Integer cbContinues) {
		this.cbContinues = cbContinues;
	}

	public Double getCbIncAtk() {
		return cbIncAtk;
	}

	public void setCbIncAtk(Double cbIncAtk) {
		this.cbIncAtk = cbIncAtk;
	}

	public Double getCbIncDef() {
		return cbIncDef;
	}

	public void setCbIncDef(Double cbIncDef) {
		this.cbIncDef = cbIncDef;
	}

	public Double getCbIncDam() {
		return cbIncDam;
	}

	public void setCbIncDam(Double cbIncDam) {
		this.cbIncDam = cbIncDam;
	}

	public Double getCbDecAtk() {
		return cbDecAtk;
	}

	public void setCbDecAtk(Double cbDecAtk) {
		this.cbDecAtk = cbDecAtk;
	}

	public Double getCbDecDef() {
		return cbDecDef;
	}

	public void setCbDecDef(Double cbDecDef) {
		this.cbDecDef = cbDecDef;
	}

	public Double getCbDecDam() {
		return cbDecDam;
	}

	public void setCbDecDam(Double cbDecDam) {
		this.cbDecDam = cbDecDam;
	}

	public Double getCbIncHp() {
		return cbIncHp;
	}

	public void setCbIncHp(Double cbIncHp) {
		this.cbIncHp = cbIncHp;
	}

	public Integer getCbDizzy() {
		return cbDizzy;
	}

	public void setCbDizzy(Integer cbDizzy) {
		this.cbDizzy = cbDizzy;
	}

	public Buff getBuff(boolean advanced) {
		AdditionFactor cbAddi = new AdditionFactor();
		cbAddi.dizzy = this.getCbDizzy() == WhetherType.YES.getV();
		cbAddi.factor.magicAtkAdd = this.getCbIncAtk() - this.getCbDecAtk();
		cbAddi.factor.shortAtkAdd = this.getCbIncAtk() - this.getCbDecAtk();
		cbAddi.factor.shortDefAdd = this.getCbIncDef() - this.getCbDecDef();
		cbAddi.factor.damAdd = this.getCbIncDam();
		cbAddi.factor.damImmuneAdd = this.getCbDecDam();
		cbAddi.factor.damAntiAdd = this.getCbAntiDam();
		cbAddi.factor.hpAdd = this.getCbIncHp();
		return new Buff(this.cbId, this.cbContinues, this.cbCalcType, this.cbType, this.cbKeep, this.cbBattleImg, cbAddi, advanced);
	}
}