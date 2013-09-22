package com.common.socket.vo;

import com.voyage.constant.Const;
import com.voyage.constant.ErCode;

public class ResultDataVO {
	private int rtCode;// 0代表正常;-1代表默认失败；大于0参照具体的返回码
	private String event;//事件
	private Object data;
	private Integer total;//分页时表示总页数

	public void setPageTotal(int total) {
		this.total = total / Const.PAGE_LIMIT + (total % Const.PAGE_LIMIT == 0 ? 0 : 1);
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public void setRtCode(int rtCode) {
		this.rtCode = rtCode;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public String getEvent() {
		return event;
	}

	public Integer getRtCode() {
		return rtCode;
	}

	public void setRtCode(Integer rtCode) {
		this.rtCode = rtCode;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public ResultDataVO() {

	}

	public ResultDataVO(int rtCode) {
		this(rtCode, null);
	}

	public ResultDataVO(Object data) {
		this(ErCode.E0, data);
	}

	public ResultDataVO(int rtCode, Object data) {
		this.rtCode = rtCode;
		this.data = data;
	}
}
