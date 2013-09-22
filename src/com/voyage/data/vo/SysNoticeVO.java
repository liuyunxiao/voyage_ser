package com.voyage.data.vo;

import java.util.Date;

public class SysNoticeVO {
	private Integer snId;

	private Integer snType;

	private String snName;

	private Date snStart;

	private Date snEnd;

	private String snContent;

	public SysNoticeVO() {

	}

	public SysNoticeVO(Integer snId, Integer snType, String snName, Date snStart, Date snEnd, String snContent) {
		super();
		this.snId = snId;
		this.snType = snType;
		this.snName = snName;
		this.snStart = snStart;
		this.snEnd = snEnd;
		this.snContent = snContent;
	}

	public Integer getSnId() {
		return snId;
	}

	public void setSnId(Integer snId) {
		this.snId = snId;
	}

	public Integer getSnType() {
		return snType;
	}

	public void setSnType(Integer snType) {
		this.snType = snType;
	}

	public String getSnName() {
		return snName;
	}

	public void setSnName(String snName) {
		this.snName = snName;
	}

	public Date getSnStart() {
		return snStart;
	}

	public void setSnStart(Date snStart) {
		this.snStart = snStart;
	}

	public Date getSnEnd() {
		return snEnd;
	}

	public void setSnEnd(Date snEnd) {
		this.snEnd = snEnd;
	}

	public String getSnContent() {
		return snContent;
	}

	public void setSnContent(String snContent) {
		this.snContent = snContent;
	}
}