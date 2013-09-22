package com.voyage.data.vo;


public class SysUserActivityMapVO {
	private Integer suamId;

	private Integer suamUserId;

	private Integer suamActivityMapId;

	private Integer suamN;

	public Integer getSuamId() {
		return suamId;
	}

	public void setSuamId(Integer suamId) {
		this.suamId = suamId;
	}

	public Integer getSuamUserId() {
		return suamUserId;
	}

	public void setSuamUserId(Integer suamUserId) {
		this.suamUserId = suamUserId;
	}

	public Integer getSuamActivityMapId() {
		return suamActivityMapId;
	}

	public void setSuamActivityMapId(Integer suamActivityMapId) {
		this.suamActivityMapId = suamActivityMapId;
	}

	public Integer getSuamN() {
		return suamN;
	}

	public void setSuamN(Integer suamN) {
		this.suamN = suamN;
	}

	@Override
	public int hashCode() {
		int h = 17 * 37 + this.suamActivityMapId;
		return h;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof SysUserActivityMapVO)) {
			return false;
		}

		SysUserActivityMapVO other = (SysUserActivityMapVO) obj;

		return this.suamActivityMapId == other.suamActivityMapId.intValue();
	}
}