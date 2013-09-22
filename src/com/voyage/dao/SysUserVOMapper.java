package com.voyage.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.voyage.data.vo.SysUserVO;

public interface SysUserVOMapper {
	int deleteByPrimaryKey(Integer suId);

	int insertSelective(SysUserVO record);

	List<Integer> selectAllUserIds();

	List<SysUserVO> selectFullAll();

	SysUserVO selectByPk(Integer suId);

	SysUserVO selectFullByPk(Integer suId);

	int updateByPrimaryKeySelective(SysUserVO record);

	Integer selectCountByName(String suName);

	SysUserVO selectByAccountId(Integer accountId);

	/**查找gGold左侧离线活跃玩家
	 * @param gHour 几个小时内上过线的玩家
	 * @param gN  返回gN条纪录
	 * */
	List<SysUserVO> selectLeftOfflineLive(@Param("gGold") int gGold, @Param("gHour") int gHour, @Param("gN") int gN);

	/**查找gGold右侧离线活跃玩家
	 * @param gLeft 左端点
	 * @param gHour 几个小时内上过线的玩家
	 * @param gN  返回gN条纪录
	 * */
	List<SysUserVO> selectRightOfflineLive(@Param("gGold") int gGold, @Param("gHour") int gHour, @Param("gN") int gN);

	/**
	 * 查找N个活跃的玩家好友
	 * */
	List<SysUserVO> selectActiveFriend(@Param("userId") int userId, @Param("nHour") int gHour, @Param("nActive") int nActive);

	/**
	 * 查找N个活跃的非玩家好友的玩家
	 * */
	List<SysUserVO> selectActiveNotFriend(@Param("userId") int userId, @Param("nHour") int gHour, @Param("nActive") int nActive);

	/**
	 * 查找N个在线好友玩家
	 * */
	List<SysUserVO> selectOnlineFriend(@Param("userId") int userId, @Param("nActive") int nActive);

	/**
	 * 查找N个在线非好友玩家
	 * */
	List<SysUserVO> selectOnlineNotFriend(@Param("userId") int userId, @Param("nActive") int nActive);

	/**
	 * 查找N个非好友玩家
	 * */
	List<SysUserVO> selectNotFriend(@Param("userId") int userId, @Param("nActive") int nActive);

	/**
	 * 查找玩家的好友(带分页)
	 * */
	List<SysUserVO> selectFriend(@Param("userId") int userId, @Param("pStart") int pStart, @Param("pLimit") int pLimit);

	/**
	 * 根据ID别名号查找
	 * */
	SysUserVO selectFullByIdAlias(@Param("idAlias") String idAlias);

	/**
	 * 根据一系列主键查询
	 * */
	List<SysUserVO> selectFullByUserIds(@Param("userIds") String userIds);

	/**
	 * 查询服务器总人数
	 * */
	Integer selectAllCount();

	/**
	 * 重置所有玩家的活跃奖励为未领取
	 * */
	void resetAllDaily();
}