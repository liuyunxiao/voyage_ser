package com.voyage.jmx.userstate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.voyage.constant.Const;
import com.voyage.service.impl.UserService;
import com.voyage.util.CommonUtil;

public class UserState implements UserStateMBean {
	private final Logger logger = LoggerFactory.getLogger(UserState.class);

	@Override
	public void initTestUser(String saName, int num) throws Exception {
		saName = toUTF8(saName);
		UserService.getInstance().initTestUser(saName, num);
		logger.warn(CommonUtil.getGmDo("initTestUser", new Object[] { saName, num }).toString());
	}

	@Override
	public void sendRewardTo(String userIds, int rewardId) throws Exception {
		UserService.getInstance().gmSendRewardTo(userIds, rewardId);
		logger.warn(CommonUtil.getGmDo("sendRewardTo", new Object[] { userIds, rewardId }).toString());
	}

	@Override
	public void sendRewardToAll(int rewardId) throws Exception {
		UserService.getInstance().gmSendRewardToAll(rewardId);
		logger.warn(CommonUtil.getGmDo("sendRewardToAll", new Object[] { rewardId }).toString());
	}

	@Override
	public void sayTo(String userIds, String msg) throws Exception {
		msg = toUTF8(msg);
		UserService.getInstance().gmSayTo(userIds, msg);
		logger.warn(CommonUtil.getGmDo("sayTo", new Object[] { userIds, msg }).toString());
	}

	@Override
	public void sayToAll(String msg) throws Exception {
		msg = toUTF8(msg);
		UserService.getInstance().gmSayToAll(msg);
		logger.warn(CommonUtil.getGmDo("sayToAll", new Object[] { msg }).toString());
	}

	@Override
	public void freeze(String accountIds, String thaw) throws Exception {
		UserService.getInstance().gmFreeze(accountIds, CommonUtil.toDate(Const.DEFAULT_TIME_FORMAT, thaw));
		logger.warn(CommonUtil.getGmDo("freeze", new Object[] { accountIds, thaw }).toString());
	}

	@Override
	public void systemNotice(int type, String name, String start, String end, String content) throws Exception {
		name = toUTF8(name);
		content = toUTF8(content);

		UserService.getInstance().gmNotice(type, name, CommonUtil.toDate(Const.DEFAULT_TIME_FORMAT, start), CommonUtil.toDate(Const.DEFAULT_TIME_FORMAT, end),
				content);
		logger.warn(CommonUtil.getGmDo("systemNotice", new Object[] { type, name, start, end, content }).toString());
	}

	/**
	 * 转换成utf-8
	 * */
	private String toUTF8(String str) throws Exception {
		return new String(str.getBytes("ISO8859-1"), "UTF-8");
	}
}
