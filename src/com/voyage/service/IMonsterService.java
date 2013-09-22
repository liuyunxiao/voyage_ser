package com.voyage.service;

import java.util.List;

import com.voyage.data.bo.ActivityMapBO;
import com.voyage.data.bo.BorrowUserBO;
import com.voyage.data.bo.MapNodeBO;
import com.voyage.data.bo.MapNodeTypeBO;
import com.voyage.data.bo.UserExtBO;
import com.voyage.data.vo.CfgMapNodeVO;
import com.voyage.data.vo.SysUserActivityMapVO;

public interface IMonsterService {
	/**
	 * 获得副本节点列表
	 * */
	List<MapNodeBO> enterNode(int userId, int cmntId) throws Exception;

	/**
	 * 获得副本节点类别列表
	 * */
	List<MapNodeTypeBO> enterNodeType(int userId) throws Exception;

	/**
	 * 开启cmnId的下一个节点
	 * @param cmnId 副本节点表主键(为cmnId0时表示开启第1个节点)
	 * */
	void openNextMapNode(int userId, int cmnId) throws Exception;

	/**
	 * 判断玩家是否已开启某个关卡
	 * */
	boolean isNodeOpen(int userId, int cmnId) throws Exception;

	/**获得玩家所借的公主*/
	BorrowUserBO getBorrowUser(int userId) throws Exception;

	/**
	 * 开启指定节点
	 * */
	void openMapNode(int userId, CfgMapNodeVO nextNodeVO) throws Exception;

	/**
	 * 将otherId作为userId的盟友
	 * */
	BorrowUserBO ally(int userId, int otherId) throws Exception;

	/**
	 * 清除玩家的盟友
	 * */
	void breakAlliance(int userId) throws Exception;

	/**组装联盟界面数据*/
	List<UserExtBO> enterAlliance(int userId) throws Exception;

	/**
	 * 組裝活動副本界面數據
	 * */
	List<ActivityMapBO> enterActivity(int userId) throws Exception;

	/**
	 * 如果滿足开启新的活动FB则开启
	 * */
	void openActivityMap(int userId, int level) throws Exception;

	/**
	 * 查找指定活动副本
	 * */
	SysUserActivityMapVO selectByActivityId(int userId, int activityId) throws Exception;

	/**
	 * 重置活动副本次数
	 * */
	void resetAllActivityMap() throws Exception;
}
