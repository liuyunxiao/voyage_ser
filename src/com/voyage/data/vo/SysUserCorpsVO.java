package com.voyage.data.vo;

public class SysUserCorpsVO {
	private Integer sucId;

	private CfgCorpsVO sucCorpsVO;

	private Integer sucShortAtk;

	private Integer sucShortDef;

	private Integer sucMagicAtk;

	private Integer sucSoma;
	private Integer sucUserId;

	public Integer getSucUserId() {
		return sucUserId;
	}

	public void setSucUserId(Integer sucUserId) {
		this.sucUserId = sucUserId;
	}

	public Integer getSucId() {
		return sucId;
	}

	public void setSucId(Integer sucId) {
		this.sucId = sucId;
	}

	public CfgCorpsVO getSucCorpsVO() {
		return sucCorpsVO;
	}

	public void setSucCorpsVO(CfgCorpsVO sucCorpsVO) {
		this.sucCorpsVO = sucCorpsVO;
	}

	public Integer getSucShortAtk() {
		return sucShortAtk;
	}

	public void setSucShortAtk(Integer sucShortAtk) {
		this.sucShortAtk = sucShortAtk;
	}

	public Integer getSucShortDef() {
		return sucShortDef;
	}

	public void setSucShortDef(Integer sucShortDef) {
		this.sucShortDef = sucShortDef;
	}

	public Integer getSucMagicAtk() {
		return sucMagicAtk;
	}

	public void setSucMagicAtk(Integer sucMagicAtk) {
		this.sucMagicAtk = sucMagicAtk;
	}

	public Integer getSucSoma() {
		return sucSoma;
	}

	public void setSucSoma(Integer sucSoma) {
		this.sucSoma = sucSoma;
	}

}