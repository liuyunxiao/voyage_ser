package com.voyage.data.vo;

import java.util.Date;

public class SysAccountVO {
	private Integer saId;

	private String saName;

	private String saPassword;

	private Date saCreate;
	private String saUuid;
	private Date saThaw;

	public Date getSaThaw() {
		return saThaw;
	}

	public void setSaThaw(Date saThaw) {
		this.saThaw = saThaw;
	}

	public String getSaUuid() {
		return saUuid;
	}

	public void setSaUuid(String saUuid) {
		this.saUuid = saUuid;
	}

	public SysAccountVO(int saId) {
		this.saId = saId;
	}

	public Date getSaCreate() {
		return saCreate;
	}

	public void setSaCreate(Date saCreate) {
		this.saCreate = saCreate;
	}

	public SysAccountVO() {
	}

	public Integer getSaId() {
		return saId;
	}

	public void setSaId(Integer saId) {
		this.saId = saId;
	}

	public String getSaName() {
		return saName;
	}

	public void setSaName(String saName) {
		this.saName = saName;
	}

	public String getSaPassword() {
		return saPassword;
	}

	public void setSaPassword(String saPassword) {
		this.saPassword = saPassword;
	}

}