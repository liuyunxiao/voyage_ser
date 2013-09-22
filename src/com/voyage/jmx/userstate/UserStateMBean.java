package com.voyage.jmx.userstate;

public interface UserStateMBean {

	/**
	 * 生成测试账号
	 * @param saName 账号名
	 * @param num		数量
	 * @throws Exception 
	 */
	void initTestUser(String saName, int num) throws Exception;

	/**
	 * 发送奖励到指定用户
	 * 
	 * */
	void sendRewardTo(String userIds, int rewardId) throws Exception;

	/**
	 * 发送奖励到所有用户
	 * */
	void sendRewardToAll(int rewardId) throws Exception;

	/**
	 * 发送消息到指定用户
	 * 
	 * */
	void sayTo(String userIds, String msg) throws Exception;

	/**
	 * 发送消息到所有用户
	 * */
	void sayToAll(String msg) throws Exception;

	/**
	 * 冻结账号到指定日期
	 * @param thaw 解冻日期(yyyy-MM-dd HH:mm:ss)
	 * */
	void freeze(String accountIds, String thaw) throws Exception;

	/**
	 * 发布系统公告
	 * @param type 见NoticeType
	 * @param start 起始时间(yyyy-MM-dd HH:mm:ss)
	 * @param end 截止时间(yyyy-MM-dd HH:mm:ss)
	 * */
	void systemNotice(int type, String name, String start, String end, String content) throws Exception;
}
