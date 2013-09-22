package com.voyage.data.vo;

import com.voyage.battle.player.AdditionFactor;

public class SysUserBattleCorpsVO {
	private Integer subcId;

	private Integer subcUserId;
	private CfgCorpsVO subcCorpsVO;

	private Integer subcPos;

	private Integer subcCaptain;

	private Integer subcShortAtk;

	private Integer subcShortDef;

	private Integer subcMagicAtk;

	private Integer subcSoma;

	public SysUserBattleCorpsVO() {
	}

	public SysUserBattleCorpsVO(SysUserCorpsVO corps) {
		this.subcUserId = corps.getSucUserId();
		this.subcCorpsVO = corps.getSucCorpsVO();
		this.subcShortAtk = corps.getSucShortAtk();
		this.subcShortDef = corps.getSucShortDef();
		this.subcMagicAtk = corps.getSucMagicAtk();
		this.subcSoma = corps.getSucSoma();
	}

	public SysUserBattleCorpsVO(int subcId, int subcCaptain, int subcPos) {
		this.subcId = subcId;
		this.subcCaptain = subcCaptain;
		this.subcPos = subcPos;
	}

	public Integer getSubcId() {
		return subcId;
	}

	public void setSubcId(Integer subcId) {
		this.subcId = subcId;
	}

	public Integer getSubcUserId() {
		return subcUserId;
	}

	public void setSubcUserId(Integer subcUserId) {
		this.subcUserId = subcUserId;
	}

	public CfgCorpsVO getSubcCorpsVO() {
		return subcCorpsVO;
	}

	public void setSubcCorpsVO(CfgCorpsVO subcCorpsVO) {
		this.subcCorpsVO = subcCorpsVO;
	}

	public Integer getSubcPos() {
		return subcPos;
	}

	public void setSubcPos(Integer subcPos) {
		this.subcPos = subcPos;
	}

	public Integer getSubcCaptain() {
		return subcCaptain;
	}

	public void setSubcCaptain(Integer subcCaptain) {
		this.subcCaptain = subcCaptain;
	}

	public Integer getSubcShortAtk() {
		return subcShortAtk;
	}

	public void setSubcShortAtk(Integer subcShortAtk) {
		this.subcShortAtk = subcShortAtk;
	}

	public Integer getSubcShortDef() {
		return subcShortDef;
	}

	public void setSubcShortDef(Integer subcShortDef) {
		this.subcShortDef = subcShortDef;
	}

	public Integer getSubcMagicAtk() {
		return subcMagicAtk;
	}

	public void setSubcMagicAtk(Integer subcMagicAtk) {
		this.subcMagicAtk = subcMagicAtk;
	}

	public Integer getSubcSoma() {
		return subcSoma;
	}

	public void setSubcSoma(Integer subcSoma) {
		this.subcSoma = subcSoma;
	}

	public AdditionFactor getAddi() {
		int propBase = subcCorpsVO.getPropBase(this.subcMagicAtk + this.subcShortAtk + this.subcShortDef + this.subcSoma);
		AdditionFactor cbAddi = new AdditionFactor();
		cbAddi.digit.magicAtkAdd = this.subcMagicAtk + propBase;
		cbAddi.digit.shortAtkAdd = this.subcShortAtk + propBase;
		cbAddi.digit.shortDefAdd = this.subcShortDef + propBase;
		cbAddi.digit.somaAdd = this.subcSoma + propBase;
		return cbAddi;
	}
}