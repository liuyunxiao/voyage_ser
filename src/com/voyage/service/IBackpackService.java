package com.voyage.service;

import java.util.List;

import org.json.JSONObject;

import com.voyage.data.bo.GoodsBO;
import com.voyage.data.vo.CfgGoodsVO;
import com.voyage.data.vo.SysUserBackpackVO;
import com.voyage.enums.GoodsType;

public interface IBackpackService {
	/**添加到背包*/
	int addToBackpack(GoodsType gt, int userId, int gId) throws Exception;

	int addToBackpack(GoodsType gt, int userId, CfgGoodsVO cgVO) throws Exception;

	/**
	 * 从背包中删除某物
	 * @param subId 背包主键
	 * */
	void delFromBackpack(int userId, int subId) throws Exception;

	/**
	 * 查找指定类型的物品，并按成本降序排列
	 * */
	List<GoodsBO> getAllGoods(int userId, int subType) throws Exception;

	/**
	 * 获取玩家的背包列表
	 * */
	List<SysUserBackpackVO> selectAll(int userId) throws Exception;

	/**
	 * 移动物品位置
	 * */
	void move(JSONObject param) throws Exception;

	/**
	 * 组装礼物、农场、矿场界面数据
	 * */
	List<GoodsBO> enter(int userId, int subType) throws Exception;

	/**
	 * 收获养殖
	 * */
	void reapBreed(JSONObject param) throws Exception;

}
