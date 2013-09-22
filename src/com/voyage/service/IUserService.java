package com.voyage.service;

import java.util.Date;
import java.util.List;

import com.voyage.data.bo.NoticeBO;
import com.voyage.data.bo.UserExtBO;
import com.voyage.data.vo.CfgRewardVO;
import com.voyage.data.vo.SysAccountVO;
import com.voyage.data.vo.SysUserVO;
import com.voyage.enums.IncomeType;
import com.voyage.enums.MoneyType;

public interface IUserService {
	/** 账号登录 
	 * 优先以saUuid登录，若saUuid为NULL时才进行注册名登录
	 * */
	SysAccountVO loginAccount(String saUuid, String saName, String saPassword) throws Exception;

	List<SysUserVO> selectFullAll() throws Exception;

	/** 查询玩家信息 */
	public SysUserVO selectByPk(Integer suId) throws Exception;

	/** 查询玩家完整信息 */
	public SysUserVO selectFullByPk(Integer suId) throws Exception;

	/** 查询玩家完整且详细信息 */
	public SysUserVO selectWrapByPk(Integer suId) throws Exception;

	/**账号登出*/
	void logout(int userId) throws Exception;

	/**加钱*/
	void incMoney(IncomeType incomeType, int userId, MoneyType moneyType, int n) throws Exception;

	/**扣钱*/
	void decMoney(int userId, MoneyType moneyType, int n) throws Exception;

	/** 账号注册
	 * 默认账号直接以saUuid注册
	 *  */
	SysAccountVO register(String saUuid, String saName, String saPassword) throws Exception;

	/** 账号是否存在 */
	int isExist(String saName) throws Exception;

	/** 修改密码 */
	void changePwd(String saName, String oldPwd, String newPwd) throws Exception;

	/** 角色登录 */
	SysUserVO login(int saId, int serverId) throws Exception;

	/** 创建角色
	 * @return 新手教程奖励ID
	 *  */
	Integer create(int saId, String suName, int cpId) throws Exception;

	/**
	 * 创建测试账号及角色
	 * @param saName 账号名
	 * @param num 数量
	 * */
	void initTestUser(String saName, int num) throws Exception;

	/**
	 * 查询个人信息
	 * @idAlias 玩家id别名
	 * */
	UserExtBO info(int userId, String idAlias, int oppoId) throws Exception;

	/**
	 * 查询多人信息
	 * */
	List<UserExtBO> multiInfo(int userId, String userIds) throws Exception;

	/**
	 * 修改玩家信息
	 * */
	void changeInfo(int userId, String name, String remark) throws Exception;

	/**
	 * 领取奖励
	 * */
	void reapReward(IncomeType rt, int userId, int rewardId) throws Exception;

	/**
	 * 领取奖励
	 * */
	void reapReward(IncomeType rt, int userId, CfgRewardVO ctVO) throws Exception;

	/**
	 * GM发送奖励到指定用户
	 * 
	 * */
	void gmSendRewardTo(String userIds, int rewardId) throws Exception;

	/**
	 * GM发送奖励到所有用户
	 * */
	void gmSendRewardToAll(int rewardId) throws Exception;

	/**
	 * Gm发送消息到指定用户
	 * 
	 * */
	void gmSayTo(String userIds, String msg) throws Exception;

	/**
	 * Gm发送消息到所有用户
	 * */
	void gmSayToAll(String msg) throws Exception;

	/**
	 * GM冻结账号到指定日期
	 * 
	 * */
	void gmFreeze(String userIds, Date thaw) throws Exception;

	/**
	 * GM发送系统公告
	 * 
	 * */
	void gmNotice(int type, String name, Date start, Date end, String content) throws Exception;

	/**
	 * 领取新手奖励
	 * */
	void reapNew(int userId, int rewardId) throws Exception;

	/**
	 * 是否进行过充值
	 * */
	int didFirstCharge(int userId) throws Exception;

	/**
	 * 领取首充奖励
	 * */
	void reapFirst(int userId, int rewardId) throws Exception;

	/**
	 *获取系统公告列表（ 会删除过期的公告）
	 * */
	List<NoticeBO> getNotices() throws Exception;
}
