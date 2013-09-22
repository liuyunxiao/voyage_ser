package com.common.socket;

public class SessionData {
	public Integer userId;
	public Integer accountId;
	public boolean inited = false;
	public boolean ignoreClosedDispose = false;//忽略sessionClosed时是否忽略处理方法
}
