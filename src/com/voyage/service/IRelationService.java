package com.voyage.service;

import java.util.List;

import org.json.JSONObject;

import com.voyage.data.bo.CommonBO;
import com.voyage.data.bo.MsgBO;
import com.voyage.data.bo.UserExtBO;
import com.voyage.data.vo.SysUserRelationVO;

public interface IRelationService {
	/**
	 * 组装好友界面信息
	 * @param page 第N页
	 * */
	List<UserExtBO> enter(int userId, int page) throws Exception;

	/**
	 * 处理关系
	 * */
	void deal(JSONObject param) throws Exception;

	/**
	 * 赠送金币
	 * */
	void give(int userId, int oppoId, int gold) throws Exception;

	List<SysUserRelationVO> selectAll() throws Exception;

	/**获得所有离线消息
	 * @param clear 获取后是否清空
	 * */
	List<MsgBO> getAllOfflineMsg(int userId, boolean clear) throws Exception;

	/**
	 * 通知有1个新战报
	 * */
	void sendBrt(int userId);

	/**
	 * 发送消息
	 * */
	void sendMsg(MsgBO msgBO);

	/**
	 * 保存为离线消息
	 * */
	void saveOfflineMsg(List<MsgBO> msgs);

	/**
	 * 通知前台更新common信息
	 * */
	void sendCommon(CommonBO cb);
}
