package com.voyage.facade;

import com.common.socket.message.IRequest;
import com.common.socket.message.IResponse;

public interface IUserFacade {
	/**
	 * 返回服务器列表
	 * */
	void getServers(IRequest request, IResponse response) throws Exception;

	/**
	 * 账号登录
	 * 
	 * @param saName
	 *            账号名
	 * @param saPassward
	 *            账号密码
	 * @return rtCode: 0正常， 1:账号名或密码错误
	 */
	void loginAccount(IRequest request, IResponse response) throws Exception;

	/**角色登录*/
	void login(IRequest request, IResponse response) throws Exception;

	/**创建角色并登录*/
	void create(IRequest request, IResponse response) throws Exception;

	/**
	 * 账号注册
	 * 
	 * */
	void register(IRequest request, IResponse response) throws Exception;

	/**账号是否已存在*/
	void isExist(IRequest request, IResponse response) throws Exception;

	/**
	 * 修改密码
	 * */
	void changePwd(IRequest request, IResponse response) throws Exception;

	/**
	 * 账号退出
	 * */
	void logoutAccount(IRequest request, IResponse response) throws Exception;

	void logout(Integer userId, Integer accountId) throws Exception;

	/**
	 * 查询个人信息
	 * */
	void info(IRequest request, IResponse response) throws Exception;

	/**
	 * 查询多人信息
	 * */
	void multiInfo(IRequest request, IResponse response) throws Exception;

	/**
	 * 修改角色信息
	 * */
	void changeInfo(IRequest request, IResponse response) throws Exception;

	/**
	 * 进入VIP界面
	 * */
	void enterVip(IRequest request, IResponse response) throws Exception;

	/**
	 * 进入排行榜界面
	 * */
	void enterRank(IRequest request, IResponse response) throws Exception;

	/**
	 * 同步资源
	 * */
	void syncRs(IRequest request, IResponse response) throws Exception;

	/**
	 * 重新绑定socket
	 * */
	void rebind(IRequest request, IResponse response) throws Exception;

	/**
	 * 查询新手奖励信息
	 * */
	void getNew(IRequest request, IResponse response) throws Exception;

	/**
	 * 领取新手奖励
	 * */
	void reapNew(IRequest request, IResponse response) throws Exception;

	/**
	 * 查询首充奖励信息
	 * */
	void getFirst(IRequest request, IResponse response) throws Exception;

	/**
	 * 领取首充奖励
	 * */
	void reapFirst(IRequest request, IResponse response) throws Exception;
}
