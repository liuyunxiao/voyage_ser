package com.voyage.exception;

import com.common.socket.vo.ResultDataVO;
import com.voyage.constant.ErCode;

public class NotifyException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3414704171949238199L;
	private ResultDataVO rtVO;
	private Integer userId;

	public NotifyException(ResultDataVO rtVO) {
		this(rtVO, null);
	}

	public NotifyException(int rtCode) {
		this(rtCode, null);
	}

	public NotifyException(String msg) {
		this(new ResultDataVO(ErCode.E_1, msg));
	}

	public NotifyException(int rtCode, String msg) {
		this(new ResultDataVO(rtCode, msg));
	}

	public NotifyException(ResultDataVO rtVO, Integer userId) {
		super();
		this.rtVO = rtVO;
		this.userId = userId;
	}

	public ResultDataVO getRtVO() {
		return rtVO;
	}

	public Integer getUserId() {
		return userId;
	}

	public String toString() {
		return this.getClass().getSimpleName() + ": " + rtVO.getRtCode();
	}
}
