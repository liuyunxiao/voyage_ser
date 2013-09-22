package com.voyage.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.voyage.data.vo.SysUserBattleRecordVO;

public interface SysUserBattleRecordVOMapper {
	int deleteByPrimaryKey(Integer subrId);

	int insertSelective(SysUserBattleRecordVO record);

	SysUserBattleRecordVO selectByPrimaryKey(Integer subrId);

	int updateByPrimaryKeySelective(SysUserBattleRecordVO record);

	/**
	 * 根据战斗记录最大保存数量，获取超出的记录ID起始值
	 */
	Integer selectLimitId(@Param("oppoId") int oppoId, @Param("subrType") int subrType, @Param("maxRecord") int maxRecord);

	/**
	 * 根据战斗记录最大保存数量，获取超出的记录
	 */
	List<SysUserBattleRecordVO> selectOutDateRecords(@Param("oppoId") int oppoId, @Param("subrType") int subrType, @Param("limitId") Integer limitId);

	/**
	 * 删除某被打玩家超出的战斗记录
	 * @param limitId 超出数量的起始ID
	 */
	public void deleteByLimitId(@Param("oppoId") int oppoId, @Param("subrType") int subrType, @Param("limitId") Integer limitId);

	/**
	 * 获得玩家的战报列表
	 */
	List<SysUserBattleRecordVO> selectBattleRecords(@Param("oppoId") int oppoId, @Param("maxRecord") int maxRecord);

	/**
	 * 更新玩家的所有战报为已读
	 * */
	void updateAllRead(@Param("oppoId") int oppoId, @Param("subrState") int subrState);

	/**
	 * 查询未读战报数
	 * */
	Integer selectUnreadCount(@Param("oppoId") int oppoId, @Param("subrState") int subrState);

}