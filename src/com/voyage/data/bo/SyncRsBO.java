package com.voyage.data.bo;

/**
 * db同步信息
 * */
public class SyncRsBO {
	public String rsVer;//版本号
	public byte[] dbData;//数据

	public SyncRsBO(String rsVer) {
		super();
		this.rsVer = rsVer;
	}

	public String getRsVer() {
		return rsVer;
	}

	public void setRsVer(String rsVer) {
		this.rsVer = rsVer;
	}

	public byte[] getDbData() {
		return dbData;
	}

	public void setDbData(byte[] dbData) {
		this.dbData = dbData;
	}

}
