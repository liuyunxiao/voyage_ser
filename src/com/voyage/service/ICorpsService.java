package com.voyage.service;

import java.util.List;

import org.json.JSONObject;

import com.voyage.data.bo.CorpsItemBO;
import com.voyage.data.bo.CorpsUnitBO;
import com.voyage.data.vo.CfgCorpsVO;

public interface ICorpsService {

	/**
	 * 将兵种从某个位置上移除
	 * 
	 */
	//	void takeOff(JSONObject jo) throws Exception;

	/**
	 * 重置或清空阵型（清空时不还钱),同时更新队长列表
	 * @param clean 是否是清空阵型(重置为false,清空为true)
	 */
	void resetCorps(boolean clean, int userId) throws Exception;

	/**
	 * 组装布阵界面数据
	 * */
	List<CorpsUnitBO> enterCorps(int userId) throws Exception;

	/**
	 * 获得最近一次非空阵型
	 * */
	List<CorpsUnitBO> getLastArmy(int userId) throws Exception;

	/**
	 * 招募兵种到某个位置上
	 * 
	 */
	//CorpsUnitBO hire(JSONObject jo) throws Exception;

	/**
	 * 英雄府 --洗点
	 * @return 兵种ID
	 */
	int resetDot(int sucId) throws Exception;

	/**
	 * 英雄府--加点
	 * @return 兵种ID
	 * */
	int addDot(int sucId, int shortAtk, int shortDef, int magicAtk, int soma) throws Exception;

	/**
	 * 进入英雄府界面
	 * */
	List<CorpsItemBO> enterDot(int userId, int corpsType) throws Exception;

	/**
	 * 保存阵型
	 * @param  格式: 最新布阵信息：{... lineup:[{subcId:1,corpsId:1, captain:1, pos:2},{subcId:2, corpsId:2,captain:0,pos:3}]}；
	 * subcId:出战兵种表主键
	 * corpsId:兵种表主键
	 * captain: 最新队长状态，即是否队长：0：否；1：是
	 * pos:最新的位置
	 * */
	void moveAndCaptain(JSONObject param) throws Exception;

	/**
	 * 添加兵种
	 * */
	boolean addCorpsIfNotExist(int userId, CfgCorpsVO ccVO) throws Exception;
}
