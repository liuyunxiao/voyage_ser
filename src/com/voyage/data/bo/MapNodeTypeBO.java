package com.voyage.data.bo;


/**
 * 副本节点类别
 * */
public class MapNodeTypeBO {
	public int cmntId;//副本节点类别表主键
	public String name;//副本类别名
	public int state;//0:未开启；1：未通关;2：已通关

	public MapNodeTypeBO(int cmntId, String name) {
		super();
		this.cmntId = cmntId;
		this.name = name;
	}

	public int getCmntId() {
		return cmntId;
	}

	public void setCmntId(int cmntId) {
		this.cmntId = cmntId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	@Override
	public int hashCode() {
		int h = 17 * 37 + this.cmntId;
		return h;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof MapNodeTypeBO)) {
			return false;
		}

		MapNodeTypeBO other = (MapNodeTypeBO) obj;

		return this.cmntId == other.cmntId;
	}

}
